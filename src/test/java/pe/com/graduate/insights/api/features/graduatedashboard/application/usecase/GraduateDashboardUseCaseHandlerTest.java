package pe.com.graduate.insights.api.features.graduatedashboard.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersResponse;
import pe.com.graduate.insights.api.features.jobs.application.dto.JobResponse;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;
import pe.com.graduate.insights.api.features.graduatedashboard.application.dto.GraduateDashboardResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyListResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.input.GraduateSurveyUseCase;
import pe.com.graduate.insights.api.features.joboffers.application.ports.output.JobOffersRepositoryPort;
import pe.com.graduate.insights.api.features.jobs.application.ports.output.JobRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GraduateDashboardUseCaseHandlerTest {

  @Mock private GraduateSurveyUseCase graduateSurveyUseCase;
  @Mock private JobRepositoryPort jobRepositoryPort;
  @Mock private JobOffersRepositoryPort jobOffersRepositoryPort;

  @InjectMocks private GraduateDashboardUseCaseHandler useCaseHandler;

  @Test
  void getDashboardShouldAggregateSurveyJobsAndOffers() {
    Long graduateId = 10L;

    GraduateSurveyListResponse completedSurvey =
      GraduateSurveyListResponse.builder().surveyId(1L).completed(true).build();
    GraduateSurveyListResponse pendingSurvey =
      GraduateSurveyListResponse.builder().surveyId(2L).completed(false).build();
    when(graduateSurveyUseCase.getAllSurveysForGraduate(graduateId))
        .thenReturn(List.of(completedSurvey, pendingSurvey));

    JobResponse activeJob = JobResponse.builder().estado(ConstantsUtils.STATUS_ACTIVE).fechaFin("").build();
    JobResponse inactiveJob = JobResponse.builder().estado("inactive").fechaFin("").build();
    Page<JobResponse> jobsPage = new PageImpl<>(List.of(activeJob, inactiveJob));
    when(jobRepositoryPort.getPaginationByGraduate(org.mockito.ArgumentMatchers.eq(""), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(graduateId)))
        .thenReturn(jobsPage);

    JobOffersResponse offer = JobOffersResponse.builder().jobOffersId(99L).build();
    when(jobOffersRepositoryPort.getRecentActiveOffers(3)).thenReturn(List.of(offer));

    GraduateDashboardResponse response = useCaseHandler.getDashboard(graduateId);

    assertEquals(2, response.getSurveyStats().getTotal());
    assertEquals(1, response.getSurveyStats().getCompleted());
    assertEquals(1, response.getSurveyStats().getPending());
    assertEquals(50, response.getSurveyStats().getCompletionRate());
    assertEquals(2, response.getJobStats().getTotalJobs());
    assertEquals(1, response.getJobStats().getActiveJobs());
    assertEquals(1, response.getPendingSurveys().size());
    assertEquals(1, response.getCompletedSurveys().size());
    assertEquals(2, response.getJobs().size());
    assertEquals(1, response.getJobOffers().size());
  }

  @Test
  void getDashboardShouldHandleNoSurveys() {
    Long graduateId = 20L;

    when(graduateSurveyUseCase.getAllSurveysForGraduate(graduateId)).thenReturn(List.of());
    when(jobRepositoryPort.getPaginationByGraduate(org.mockito.ArgumentMatchers.eq(""), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(graduateId)))
        .thenReturn(Page.empty());
    when(jobOffersRepositoryPort.getRecentActiveOffers(3)).thenReturn(List.of());

    GraduateDashboardResponse response = useCaseHandler.getDashboard(graduateId);

    assertEquals(0, response.getSurveyStats().getTotal());
    assertEquals(0, response.getSurveyStats().getCompletionRate());
    assertEquals(0, response.getJobStats().getTotalJobs());
    assertEquals(0, response.getJobStats().getActiveJobs());
  }
}


