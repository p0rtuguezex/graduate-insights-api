package pe.com.graduate.insights.api.features.survey.application.usecase;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.mail.application.ports.input.MailUseCase;
import pe.com.graduate.insights.api.features.notification.infrastructure.jpa.NotificationRepository;
import pe.com.graduate.insights.api.features.survey.application.dto.SurveyResponse;
import pe.com.graduate.insights.api.features.survey.application.ports.output.SurveyRepositoryPort;
import pe.com.graduate.insights.api.features.survey.domain.model.SurveyStatus;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@ExtendWith(MockitoExtension.class)
class SurveyUpdateStatusTest {

  private static final Long SURVEY_ID = 42L;
  private static final String FRONTEND_URL = "https://egresadosunu.com";

  @Mock private SurveyRepositoryPort surveyRepositoryPort;
  @Mock private GraduateRepository graduateRepository;
  @Mock private NotificationRepository notificationRepository;
  @Mock private MailUseCase mailUseCase;

  @InjectMocks private SurveyUseCaseHandler handler;

  @BeforeEach
  void injectFrontendUrl() {
    ReflectionTestUtils.setField(handler, "frontendUrl", FRONTEND_URL);
  }

  // --- helpers ---

  private GraduateEntity graduateWith(Long id, String correo, String nombres) {
    UserEntity user = new UserEntity();
    user.setId(id);
    user.setCorreo(correo);
    user.setNombres(nombres);

    GraduateEntity graduate = new GraduateEntity();
    graduate.setId(id);
    graduate.setUser(user);
    return graduate;
  }

  private SurveyResponse activeSurvey() {
    return SurveyResponse.builder()
        .id(SURVEY_ID)
        .title("Encuesta de seguimiento")
        .description("Descripcion de la encuesta")
        .status(SurveyStatus.ACTIVE)
        .build();
  }

  // --- tests: ACTIVE path ---

  @Test
  void updateStatusToActiveDelegatesToRepositoryPort() {
    when(surveyRepositoryPort.getDomain(SURVEY_ID)).thenReturn(activeSurvey());
    when(graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(List.of());

    handler.updateStatus(SURVEY_ID, "ACTIVE");

    verify(surveyRepositoryPort).updateStatus(SURVEY_ID, SurveyStatus.ACTIVE);
  }

  @Test
  void updateStatusToActiveCreatesOneNotificationPerGraduate() {
    GraduateEntity g1 = graduateWith(1L, "ana@mail.com", "Ana Gomez");
    GraduateEntity g2 = graduateWith(2L, "luis@mail.com", "Luis Rios");

    when(surveyRepositoryPort.getDomain(SURVEY_ID)).thenReturn(activeSurvey());
    when(graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(List.of(g1, g2));

    handler.updateStatus(SURVEY_ID, "ACTIVE");

    verify(notificationRepository, times(2)).save(any());
  }

  @Test
  void updateStatusToActiveSendsOneEmailPerGraduate() {
    GraduateEntity g1 = graduateWith(1L, "ana@mail.com", "Ana Gomez");
    GraduateEntity g2 = graduateWith(2L, "luis@mail.com", "Luis Rios");

    when(surveyRepositoryPort.getDomain(SURVEY_ID)).thenReturn(activeSurvey());
    when(graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(List.of(g1, g2));

    handler.updateStatus(SURVEY_ID, "ACTIVE");

    verify(mailUseCase).sendGenericEmail(
        eq("ana@mail.com"),
        eq(ConstantsUtils.SUBJECT_SURVEY_NOTIFICATION),
        anyString());
    verify(mailUseCase).sendGenericEmail(
        eq("luis@mail.com"),
        eq(ConstantsUtils.SUBJECT_SURVEY_NOTIFICATION),
        anyString());
  }

  @Test
  void updateStatusToActiveEmailBodyContainsSurveyTitleAndFrontendUrl() {
    GraduateEntity g1 = graduateWith(1L, "ana@mail.com", "Ana Gomez");

    when(surveyRepositoryPort.getDomain(SURVEY_ID)).thenReturn(activeSurvey());
    when(graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(List.of(g1));

    handler.updateStatus(SURVEY_ID, "ACTIVE");

    verify(mailUseCase).sendGenericEmail(
        eq("ana@mail.com"),
        eq(ConstantsUtils.SUBJECT_SURVEY_NOTIFICATION),
        contains(FRONTEND_URL));
  }

  @Test
  void updateStatusToActiveWithNoGraduatesSkipsNotificationsAndEmails() {
    when(surveyRepositoryPort.getDomain(SURVEY_ID)).thenReturn(activeSurvey());
    when(graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(List.of());

    handler.updateStatus(SURVEY_ID, "ACTIVE");

    verify(notificationRepository, never()).save(any());
    verify(mailUseCase, never()).sendGenericEmail(anyString(), anyString(), anyString());
  }

  // --- tests: DRAFT path (no side-effects) ---

  @Test
  void updateStatusToDraftOnlyDelegatesToRepositoryPortWithoutSideEffects() {
    handler.updateStatus(SURVEY_ID, "DRAFT");

    verify(surveyRepositoryPort).updateStatus(SURVEY_ID, SurveyStatus.DRAFT);
    verify(surveyRepositoryPort, never()).getDomain(any());
    verify(graduateRepository, never()).findAllByUserEstado(anyString());
    verify(notificationRepository, never()).save(any());
    verify(mailUseCase, never()).sendGenericEmail(anyString(), anyString(), anyString());
  }

  // --- status normalization ---

  @Test
  void updateStatusAcceptsLowercaseStatusWithoutThrowingException() {
    when(surveyRepositoryPort.getDomain(SURVEY_ID)).thenReturn(activeSurvey());
    when(graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(List.of());

    assertThatNoException().isThrownBy(() -> handler.updateStatus(SURVEY_ID, "active"));
    verify(surveyRepositoryPort).updateStatus(SURVEY_ID, SurveyStatus.ACTIVE);
  }
}
