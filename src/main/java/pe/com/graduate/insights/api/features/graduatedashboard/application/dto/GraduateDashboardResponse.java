package pe.com.graduate.insights.api.features.graduatedashboard.application.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.features.graduatesurveys.application.dto.GraduateSurveyListResponse;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersResponse;
import pe.com.graduate.insights.api.features.jobs.application.dto.JobResponse;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduateDashboardResponse {
  private SurveyStats surveyStats;
  private JobStats jobStats;
  private List<GraduateSurveyListResponse> pendingSurveys;
  private List<GraduateSurveyListResponse> completedSurveys;
  private List<JobResponse> jobs;
  private List<JobOffersResponse> jobOffers;
  private Boolean profileComplete;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SurveyStats {
    private int total;
    private int completed;
    private int pending;
    private int completionRate;
  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class JobStats {
    private int totalJobs;
    private int activeJobs;
  }
}
