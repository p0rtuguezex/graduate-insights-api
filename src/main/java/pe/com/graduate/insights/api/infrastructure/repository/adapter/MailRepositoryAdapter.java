package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.MailRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.MailException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.ChangePasswordRequest;
import pe.com.graduate.insights.api.domain.models.request.MailRequest;
import pe.com.graduate.insights.api.domain.models.request.ValidateCodeRequest;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.domain.utils.GraduateUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.UserRepository;

@Component
@RequiredArgsConstructor
public class MailRepositoryAdapter implements MailRepositoryPort {

  private final PasswordEncoder passwordEncoder;
  private final JavaMailSender javaMailSender;
  private final UserRepository userRepository;

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
    try {
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

      templateEmail =
          GraduateUtils.templateWithVariables(templateSend, userEntity.getNombres(), code);
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      mimeMessage.setSubject(subject);
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, Boolean.TRUE);
      mimeMessageHelper.setTo(mailRequest.getEmail());
      mimeMessageHelper.setText(templateEmail, Boolean.TRUE);
      mimeMessageHelper.setFrom(sender, ConstantsUtils.SISEG);
      javaMailSender.send(mimeMessage);

    } catch (Exception e) {
      throw new MailException("Error al enviar correo: " + e.getMessage());
    }
  }

  @Override
  public void validateCode(ValidateCodeRequest validateCodeRequest) {}

  @Override
  public void changePassword(ChangePasswordRequest changePasswordRequest) {}
}
