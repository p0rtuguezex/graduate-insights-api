package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.MailRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.InvalidCodeException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.ChangePasswordRequest;
import pe.com.graduate.insights.api.domain.models.request.MailRequest;
import pe.com.graduate.insights.api.domain.models.request.ValidateCodeRequest;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.domain.utils.GraduateUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailRepositoryAdapter implements MailRepositoryPort {

  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender javaMailSender;
  private final UserRepository userRepository;
  @Qualifier("mailExecutor")
  private final TaskExecutor mailExecutor;

  @Value("${spring.mail.username}")
  private String sender;

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

    final String subjectToSend = subject;
    final String templateToSend = templateEmail;
    mailExecutor.execute(
        () -> sendMailAsync(mailRequest.getEmail(), subjectToSend, templateToSend));
  }

  private void sendMailAsync(String email, String subject, String templateEmail) {
    try {
      log.info("Enviando correo async a: {} con asunto: {}", email, subject);
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      mimeMessage.setSubject(subject);
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE);
      mimeMessageHelper.setTo(email);
      mimeMessageHelper.setText(templateEmail, Boolean.TRUE);
      mimeMessageHelper.setFrom(sender, ConstantsUtils.SISEG);

      javaMailSender.send(mimeMessage);
      log.info("Correo enviado exitosamente a: {}", email);
    } catch (Exception e) {
      log.warn("No se pudo enviar el correo a {}: {}", email, e.getMessage(), e);
    }
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
                throw new InvalidCodeException("El código ingresado es inválido o ha expirado.");
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
                    "El código de recuperación es inválido o ha expirado.");
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
