package pe.com.graduate.insights.api.features.mail.infrastructure.adapter;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.auth.domain.exception.InvalidCodeException;
import pe.com.graduate.insights.api.features.emailconfig.infrastructure.entity.EmailConfigEntity;
import pe.com.graduate.insights.api.features.emailconfig.infrastructure.jpa.EmailConfigRepository;
import pe.com.graduate.insights.api.features.mail.application.dto.ChangePasswordRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.MailRequest;
import pe.com.graduate.insights.api.features.mail.application.dto.ValidateCodeRequest;
import pe.com.graduate.insights.api.features.mail.application.ports.output.MailRepositoryPort;
import pe.com.graduate.insights.api.features.mail.domain.exception.MailException;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.jpa.UserRepository;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;
import pe.com.graduate.insights.api.shared.utils.GraduateUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailRepositoryAdapter implements MailRepositoryPort {

  private final PasswordEncoder passwordEncoder;
  private final EmailConfigRepository emailConfigRepository;
  private final UserRepository userRepository;

  @Qualifier("mailExecutor")
  private final TaskExecutor mailExecutor;

  @Override
  public void sendCode(MailRequest mailRequest) {
    UserEntity userEntity =
        userRepository
            .findByCorreoAndEstado(mailRequest.getEmail(), ConstantsUtils.STATUS_ACTIVE)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        String.format(
                            ConstantsUtils.USER_NOT_FOUND_BY_EMAIL, mailRequest.getEmail())));
    String templateEmail;
    String code = GraduateUtils.generateRandomSixDigitNumber();
    String templateSend = "";
    String subject = null;

    if (ConstantsUtils.RESET_PASSWORD.equals(mailRequest.getType())) {
      templateSend = ConstantsUtils.TEMPLATE_EMAIL_HTML;
      subject = ConstantsUtils.SUBJECT_EMAIL;
      userRepository.updateRecoveryCodeByUserId(code, userEntity.getId());
    } else if (ConstantsUtils.SENT_CODE_VALIDATED.equals(mailRequest.getType())) {
      templateSend = ConstantsUtils.TEMPLATE_EMAIL_HTML_USER_VALIDATED;
      subject = ConstantsUtils.SUBJECT_USER_VALIDATED;
      userRepository.updateCodeConfirmByUserId(code, userEntity.getId());
    }

    if (subject == null) {
      log.warn("Tipo de correo no soportado: {}", mailRequest.getType());
      return;
    }

    templateEmail =
        GraduateUtils.templateWithVariables(templateSend, userEntity.getNombres(), code);

    EmailConfigEntity config =
        emailConfigRepository
            .findByActivoTrue()
            .orElseThrow(
                () ->
                    new MailException(
                        "No hay configuracion de email activa. Configure Resend desde el panel de administracion."));

    final String subjectToSend = subject;
    final String templateToSend = templateEmail;
    mailExecutor.execute(
        () -> sendViaResend(config, mailRequest.getEmail(), subjectToSend, templateToSend));
  }

  private void sendViaResend(EmailConfigEntity config, String to, String subject, String htmlBody) {
    try {
      Resend resend = new Resend(config.getApiKey());
      CreateEmailOptions params =
          CreateEmailOptions.builder()
              .from(config.getNombreRemitente() + " <" + config.getEmailRemitente() + ">")
              .to(to)
              .subject(subject)
              .html(htmlBody)
              .build();
      resend.emails().send(params);
      log.info("Email enviado exitosamente a {} via Resend", to);
    } catch (Exception e) {
      log.error("Error enviando email a {} via Resend: {}", to, e.getMessage(), e);
    }
  }

  @Override
  public void sendTestEmail(String toEmail) {
    EmailConfigEntity config =
        emailConfigRepository
            .findByActivoTrue()
            .orElseThrow(
                () ->
                    new MailException(
                        "No hay configuracion de email activa. Configure Resend desde el panel de administracion."));

    String htmlBody =
        "<!DOCTYPE html><html><body>"
            + "<h2>Correo de prueba</h2>"
            + "<p>Este es un correo de prueba enviado desde GraduateInsights "
            + "para verificar la configuracion de Resend.</p>"
            + "<p>Si recibiste este correo, la configuracion es correcta.</p>"
            + "</body></html>";

    mailExecutor.execute(
        () -> sendViaResend(config, toEmail, "Correo de prueba - GraduateInsights", htmlBody));
  }

  @Override
  public void sendGenericEmail(String to, String subject, String htmlBody) {
    EmailConfigEntity config =
        emailConfigRepository
            .findByActivoTrue()
            .orElseThrow(
                () ->
                    new MailException(
                        "No hay configuracion de email activa. Configure Resend desde el panel de administracion."));

    mailExecutor.execute(() -> sendViaResend(config, to, subject, htmlBody));
  }

  @Override
  public void validateCode(ValidateCodeRequest validateCodeRequest) {
    userRepository
        .findByCorreoAndEstado(validateCodeRequest.getEmail(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            userEntity -> {
              String requestCode = validateCodeRequest.getCode();

              boolean matchesRecoveryCode =
                  userEntity.getPasswordRecoveryCode() != null
                      && userEntity.getPasswordRecoveryCode().equals(requestCode);
              boolean matchesConfirmationCode =
                  userEntity.getCodeConfirm() != null
                      && userEntity.getCodeConfirm().equals(requestCode);

              if (matchesRecoveryCode) {
                userRepository.updateRecoveryCodeByUserId(null, userEntity.getId());
              } else if (matchesConfirmationCode) {
                userRepository.updateVerifiedTrueByUserId(userEntity.getId());
                userRepository.updateCodeConfirmByUserId(null, userEntity.getId());
              } else {
                throw new InvalidCodeException("El codigo ingresado es invalido o ha expirado.");
              }
            },
            () -> {
              throw new NotFoundException(
                  String.format(
                      ConstantsUtils.USER_NOT_FOUND_BY_EMAIL, validateCodeRequest.getEmail()));
            });
  }

  @Override
  public void changePassword(ChangePasswordRequest changePasswordRequest) {
    userRepository
        .findByCorreoAndEstado(changePasswordRequest.getEmail(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            userEntity -> {
              String savedRecoveryCode = userEntity.getPasswordRecoveryCode();
              if (savedRecoveryCode == null
                  || !savedRecoveryCode.equals(changePasswordRequest.getCode())) {
                throw new InvalidCodeException(
                    "El codigo de recuperacion es invalido o ha expirado.");
              }

              String newPasswordEncode =
                  passwordEncoder.encode(changePasswordRequest.getNewPassword());
              userRepository.updatePasswordByUserId(newPasswordEncode, userEntity.getId());
              userRepository.updateRecoveryCodeByUserId(null, userEntity.getId());
            },
            () -> {
              throw new NotFoundException(
                  String.format(
                      ConstantsUtils.USER_NOT_FOUND_BY_EMAIL, changePasswordRequest.getEmail()));
            });
  }
}
