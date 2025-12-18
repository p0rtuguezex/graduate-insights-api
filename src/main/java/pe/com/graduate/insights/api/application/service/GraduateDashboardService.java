package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.GraduateDashboardUseCase;
import pe.com.graduate.insights.api.application.ports.input.GraduateSurveyUseCase;
import pe.com.graduate.insights.api.application.ports.output.JobOffersRepositoryPort;
import pe.com.graduate.insights.api.application.ports.output.JobRepositoryPort;
import pe.com.graduate.insights.api.domain.models.response.GraduateDashboardResponse;
import pe.com.graduate.insights.api.domain.models.response.GraduateSurveyListResponse;
import pe.com.graduate.insights.api.domain.models.response.JobOffersResponse;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;

@Service
@RequiredArgsConstructor
public class GraduateDashboardService implements GraduateDashboardUseCase {

  private static final int JOB_PREVIEW_LIMIT = 5;
  private static final int JOB_OFFER_LIMIT = 3;

  private final GraduateSurveyUseCase graduateSurveyUseCase;
  private final JobRepositoryPort jobRepositoryPort;
  private final JobOffersRepositoryPort jobOffersRepositoryPort;

  @Override
  public GraduateDashboardResponse getDashboard(Long graduateId) {
    List<GraduateSurveyListResponse> surveys =
        graduateSurveyUseCase.getAllSurveysForGraduate(graduateId);

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

    List<JobOffersResponse> jobOffers =
        jobOffersRepositoryPort.getRecentActiveOffers(JOB_OFFER_LIMIT);

    return new GraduateDashboardResponse(
        surveyStats, jobStats, pendingSurveys, completedSurveys, jobs, jobOffers);
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
