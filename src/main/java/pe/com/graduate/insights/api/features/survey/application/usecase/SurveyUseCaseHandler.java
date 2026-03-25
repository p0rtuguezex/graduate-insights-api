package pe.com.graduate.insights.api.features.survey.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.mail.application.ports.input.MailUseCase;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyRequest;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;
import pe.com.graduate.insights.api.features.survey.application.ports.input.SurveyUseCase;
import pe.com.graduate.insights.api.features.survey.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.features.survey.domain.exception.SurveyException;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyUseCaseHandler implements SurveyUseCase {

  private final SurveyRepositoryPort surveyRepositoryPort;
  private final GraduateRepository graduateRepository;
  private final MailUseCase mailUseCase;

  @Override
  public void save(SurveyRequest request) {
    surveyRepositoryPort.save(request);
  }

  @Override
  public Page<SurveyResponse> getPagination(String search, Pageable pageable) {
    return surveyRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public SurveyResponse getDomain(Long id) {
    return surveyRepositoryPort.getDomain(id);
  }

  @Override
  public void update(SurveyRequest request, Long id) {
    surveyRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    surveyRepositoryPort.delete(id);
  }

  @Override
  public void updateStatus(Long id, String status) {
    SurveyStatus surveyStatus = SurveyStatus.valueOf(status.toUpperCase());
    surveyRepositoryPort.updateStatus(id, surveyStatus);
  }

  @Override
  public List<SurveyResponse> getActiveSurveys() {
    return surveyRepositoryPort.getActiveSurveys();
  }

  @Override
  public List<SurveyResponse> getSurveysByStatus(String status) {
    SurveyStatus surveyStatus = SurveyStatus.valueOf(status.toUpperCase());
    return surveyRepositoryPort.getSurveysByStatus(surveyStatus);
  }

  @Override
  public void notifyGraduates(Long surveyId) {
    SurveyResponse survey = surveyRepositoryPort.getDomain(surveyId);

    if (survey.getStatus() != SurveyStatus.ACTIVE) {
      throw new SurveyException(
          String.format("La encuesta con ID %d no esta activa. Solo se pueden notificar encuestas activas.", surveyId));
    }

    List<GraduateEntity> activeGraduates =
        graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE);

    log.info("Enviando notificacion de encuesta '{}' a {} graduados activos",
        survey.getTitle(), activeGraduates.size());

    for (GraduateEntity graduate : activeGraduates) {
      try {
        String email = graduate.getUser().getCorreo();
        String name = graduate.getUser().getNombres();
        sendSurveyNotificationEmail(email, name, survey.getTitle(), survey.getDescription());
      } catch (Exception e) {
        log.error("Error al enviar notificacion de encuesta al graduado con ID {}: {}",
            graduate.getId(), e.getMessage(), e);
      }
    }
  }

  private void sendSurveyNotificationEmail(
      String toEmail, String userName, String surveyTitle, String surveyDescription) {
    String htmlBody = ConstantsUtils.TEMPLATE_EMAIL_SURVEY_NOTIFICATION
        .replace("{{user}}", userName != null ? userName : "Graduado")
        .replace("{{surveyTitle}}", surveyTitle != null ? surveyTitle : "")
        .replace("{{surveyDescription}}", surveyDescription != null ? surveyDescription : "Sin descripcion")
        .replace("{{appUrl}}", "https://graduate.local");

    mailUseCase.sendGenericEmail(toEmail, ConstantsUtils.SUBJECT_SURVEY_NOTIFICATION, htmlBody);
  }
}

