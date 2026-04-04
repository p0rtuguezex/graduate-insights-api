package pe.com.graduate.insights.api.shared.infrastructure.scheduling;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus;
import pe.com.graduate.insights.api.features.survey.infrastructure.jpa.SurveyRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SurveyExpirationScheduler {

  private final SurveyRepository surveyRepository;

  /** Runs every day at midnight. Closes ACTIVE surveys whose end date has passed. */
  @Scheduled(cron = "0 0 0 * * *")
  @Transactional
  public void closeSurveysExpiredToday() {
    LocalDate today = LocalDate.now();
    List<SurveyEntity> expiredSurveys =
        surveyRepository.findActiveSurveysWithEndDateBefore(today, SurveyStatus.ACTIVE);

    if (expiredSurveys.isEmpty()) {
      log.debug("No hay encuestas para cerrar el dia de hoy ({})", today);
      return;
    }

    expiredSurveys.forEach(
        survey -> {
          survey.setStatus(SurveyStatus.CLOSED);
          log.info(
              "Encuesta '{}' (id={}) cerrada automaticamente el {}",
              survey.getTitle(),
              survey.getId(),
              today);
        });

    surveyRepository.saveAll(expiredSurveys);
    log.info("Se cerraron {} encuesta(s) vencida(s)", expiredSurveys.size());
  }
}
