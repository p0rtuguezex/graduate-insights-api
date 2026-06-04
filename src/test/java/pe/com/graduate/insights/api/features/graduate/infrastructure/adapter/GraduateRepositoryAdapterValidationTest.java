package pe.com.graduate.insights.api.features.graduate.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.mapper.GraduateMapper;
import pe.com.graduate.insights.api.features.mail.application.ports.output.MailRepositoryPort;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.jpa.UserRepository;
import pe.com.graduate.insights.api.features.user.infrastructure.mapper.UserMapper;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@ExtendWith(MockitoExtension.class)
class GraduateRepositoryAdapterValidationTest {

  private static final Long GRADUATE_ID = 10L;
  private static final Long USER_ID = 20L;
  private static final String CORREO = "egresado@mail.com";
  private static final String NOMBRES = "Juan Perez";
  private static final String FRONTEND_URL = "https://egresadosunu.com";

  @Mock private GraduateRepository graduateRepository;
  @Mock private UserRepository userRepository;
  @Mock private GraduateMapper graduateMapper;
  @Mock private UserMapper userMapper;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private MailRepositoryPort mailRepositoryPort;

  @InjectMocks private GraduateRepositoryAdapter adapter;

  @BeforeEach
  void injectFrontendUrl() {
    ReflectionTestUtils.setField(adapter, "frontendUrl", FRONTEND_URL);
  }

  // --- helpers ---

  private GraduateEntity graduateWith(Boolean validated) {
    UserEntity user = new UserEntity();
    user.setId(USER_ID);
    user.setCorreo(CORREO);
    user.setNombres(NOMBRES);

    GraduateEntity graduate = new GraduateEntity();
    graduate.setId(GRADUATE_ID);
    graduate.setUser(user);
    graduate.setValidated(validated);
    return graduate;
  }

  // --- updateValidationStatus: false → true (sends email) ---

  @Test
  void updateValidationStatusSavesGraduateWhenTransitioningFromFalseToTrue() {
    GraduateEntity graduate = graduateWith(Boolean.FALSE);
    when(graduateRepository.findByIdAndUserEstado(GRADUATE_ID, ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(Optional.of(graduate));

    adapter.updateValidationStatus(GRADUATE_ID, Boolean.TRUE);

    verify(graduateRepository).save(graduate);
  }

  @Test
  void updateValidationStatusSendsActivationEmailWhenTransitioningFromFalseToTrue() {
    GraduateEntity graduate = graduateWith(Boolean.FALSE);
    when(graduateRepository.findByIdAndUserEstado(GRADUATE_ID, ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(Optional.of(graduate));

    adapter.updateValidationStatus(GRADUATE_ID, Boolean.TRUE);

    verify(mailRepositoryPort).sendGenericEmail(
        eq(CORREO),
        eq(ConstantsUtils.SUBJECT_ACCOUNT_ACTIVATED),
        anyString());
  }

  // --- updateValidationStatus: true → true (no email) ---

  @Test
  void updateValidationStatusSavesButSkipsEmailWhenAlreadyValidated() {
    GraduateEntity graduate = graduateWith(Boolean.TRUE);
    when(graduateRepository.findByIdAndUserEstado(GRADUATE_ID, ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(Optional.of(graduate));

    adapter.updateValidationStatus(GRADUATE_ID, Boolean.TRUE);

    verify(graduateRepository).save(graduate);
    verify(mailRepositoryPort, never()).sendGenericEmail(anyString(), anyString(), anyString());
  }

  // --- updateValidationStatus: false → false (no email) ---

  @Test
  void updateValidationStatusSavesButSkipsEmailWhenSettingFalseOnAlreadyFalse() {
    GraduateEntity graduate = graduateWith(Boolean.FALSE);
    when(graduateRepository.findByIdAndUserEstado(GRADUATE_ID, ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(Optional.of(graduate));

    adapter.updateValidationStatus(GRADUATE_ID, Boolean.FALSE);

    verify(graduateRepository).save(graduate);
    verify(mailRepositoryPort, never()).sendGenericEmail(anyString(), anyString(), anyString());
  }

  // --- updateValidationStatus: graduate not found ---

  @Test
  void updateValidationStatusThrowsNotFoundExceptionWhenGraduateDoesNotExist() {
    when(graduateRepository.findByIdAndUserEstado(GRADUATE_ID, ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> adapter.updateValidationStatus(GRADUATE_ID, Boolean.TRUE))
        .isInstanceOf(NotFoundException.class);

    verify(graduateRepository, never()).save(any());
    verify(mailRepositoryPort, never()).sendGenericEmail(anyString(), anyString(), anyString());
  }

  // --- email body content ---

  @Test
  void updateValidationStatusEmailBodyContainsFrontendUrl() {
    GraduateEntity graduate = graduateWith(Boolean.FALSE);
    when(graduateRepository.findByIdAndUserEstado(GRADUATE_ID, ConstantsUtils.STATUS_ACTIVE))
        .thenReturn(Optional.of(graduate));

    adapter.updateValidationStatus(GRADUATE_ID, Boolean.TRUE);

    verify(mailRepositoryPort).sendGenericEmail(
        eq(CORREO),
        eq(ConstantsUtils.SUBJECT_ACCOUNT_ACTIVATED),
        org.mockito.ArgumentMatchers.contains(FRONTEND_URL));
  }
}
