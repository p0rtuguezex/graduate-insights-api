package pe.com.graduate.insights.api.features.jobs.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.jobs.domain.exception.JobException;
import pe.com.graduate.insights.api.shared.security.UserContext;
import pe.com.graduate.insights.api.features.jobs.application.dto.JobRequest;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateIdentityRepositoryPort;
import pe.com.graduate.insights.api.features.jobs.application.ports.output.JobRepositoryPort;

@ExtendWith(MockitoExtension.class)
class JobUseCaseHandlerTest {

  @Mock private JobRepositoryPort jobRepositoryPort;
  @Mock private GraduateIdentityRepositoryPort graduateIdentityRepositoryPort;

  @InjectMocks private JobUseCaseHandler jobUseCaseHandler;

  @Test
  void createJobAsDirectorShouldRequireGraduateId() {
    UserContext directorContext = new UserContext(1L, pe.com.graduate.insights.api.shared.security.UserRole.DIRECTOR);
    JobRequest request = new JobRequest();

    JobException ex =
        assertThrows(JobException.class, () -> jobUseCaseHandler.createJob(request, directorContext));

    assertEquals(pe.com.graduate.insights.api.shared.utils.ConstantsUtils.JOB_GRADUATE_REQUIRED, ex.getMessage());
  }

  @Test
  void createJobAsGraduateShouldResolveGraduateIdAndSave() {
    UserContext graduateContext = new UserContext(7L, pe.com.graduate.insights.api.shared.security.UserRole.GRADUATE);
    JobRequest request = new JobRequest();
    when(graduateIdentityRepositoryPort.getActiveGraduateIdByUserId(7L)).thenReturn(99L);

    jobUseCaseHandler.createJob(request, graduateContext);

    assertEquals(99L, request.getGraduateId());
    verify(graduateIdentityRepositoryPort).getActiveGraduateIdByUserId(7L);
    verify(jobRepositoryPort).save(request);
  }

  @Test
  void deleteJobAsGraduateShouldDeleteByGraduate() {
    UserContext graduateContext = new UserContext(9L, pe.com.graduate.insights.api.shared.security.UserRole.GRADUATE);
    when(graduateIdentityRepositoryPort.getActiveGraduateIdByUserId(9L)).thenReturn(15L);

    jobUseCaseHandler.deleteJob(44L, graduateContext);

    verify(jobRepositoryPort).deleteByGraduate(44L, 15L);
  }
}


