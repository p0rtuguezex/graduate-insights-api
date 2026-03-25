package pe.com.graduate.insights.api.features.graduatedashboard.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;
import pe.com.graduate.insights.api.features.graduatedashboard.application.dto.GraduateDashboardResponse;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.application.ports.input.GraduateReadUseCase;
import pe.com.graduate.insights.api.features.graduatedashboard.application.ports.input.GraduateDashboardUseCase;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyListResponse;
import pe.com.graduate.insights.api.features.graduatesurveys.application.ports.input.GraduateSurveyUseCase;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersResponse;
import pe.com.graduate.insights.api.features.joboffers.application.ports.output.JobOffersRepositoryPort;
import pe.com.graduate.insights.api.features.jobs.application.dto.JobResponse;
import pe.com.graduate.insights.api.features.jobs.application.ports.output.JobRepositoryPort;

@Service
@RequiredArgsConstructor
public class GraduateDashboardUseCaseHandler implements GraduateDashboardUseCase {

  private static final int JOB_PREVIEW_LIMIT = 5;
  private static final int JOB_OFFER_LIMIT = 3;

  private final GraduateSurveyUseCase graduateSurveyUseCase;
  private final JobRepositoryPort jobRepositoryPort;
  private final JobOffersRepositoryPort jobOffersRepositoryPort;
  private final GraduateReadUseCase graduateReadUseCase;

  @Override
  public GraduateDashboardResponse getDashboard(Long graduateId) {
    List<GraduateSurveyListResponse> surveys = graduateSurveyUseCase.getAllSurveysForGraduate(graduateId);

    List<GraduateSurveyListResponse> completedSurveys =
      surveys.stream().filter(GraduateSurveyListResponse::isCompleted).toList();
    List<GraduateSurveyListResponse> pendingSurveys =
      surveys.stream().filter(survey -> !survey.isCompleted()).toList();

    GraduateDashboardResponse.SurveyStats surveyStats =
        new GraduateDashboardResponse.SurveyStats(
            surveys.size(),
            completedSurveys.size(),
            pendingSurveys.size(),
            calculateCompletionRate(completedSurveys.size(), surveys.size()));

    Page<JobResponse> jobPage =
        jobRepositoryPort.getPaginationByGraduate(
            "",
            PageRequest.of(0, JOB_PREVIEW_LIMIT, Sort.by(Sort.Direction.DESC, "createdDate")),
            graduateId);
    List<JobResponse> jobs = jobPage.getContent();

    GraduateDashboardResponse.JobStats jobStats =
        new GraduateDashboardResponse.JobStats(
            safeLongToInt(jobPage.getTotalElements()), calculateActiveJobs(jobs));

    List<JobOffersResponse> jobOffers = jobOffersRepositoryPort.getRecentActiveOffers(JOB_OFFER_LIMIT);

    GraduateResponse graduate = graduateReadUseCase.getDomain(graduateId);
    boolean profileComplete =
        graduate.getEscuelaProfesionalId() != null
            && graduate.getGrados() != null
            && !graduate.getGrados().isEmpty();

    return GraduateDashboardResponse.builder()
        .surveyStats(surveyStats)
        .jobStats(jobStats)
        .pendingSurveys(pendingSurveys)
        .completedSurveys(completedSurveys)
        .jobs(jobs)
        .jobOffers(jobOffers)
        .profileComplete(profileComplete)
        .build();
  }

  private int calculateCompletionRate(int completed, int total) {
    if (total == 0) {
      return 0;
    }
    return Math.round((completed * 100f) / total);
  }

  private int calculateActiveJobs(List<JobResponse> jobs) {
    return (int)
        jobs.stream()
            .filter(job -> ConstantsUtils.STATUS_ACTIVE.equals(job.getEstado()))
            .filter(job -> job.getFechaFin() == null || job.getFechaFin().isBlank())
            .count();
  }

  private int safeLongToInt(long value) {
    return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
  }
}


