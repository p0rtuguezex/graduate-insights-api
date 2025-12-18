package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.domain.models.response.GraduateDashboardResponse;

public interface GraduateDashboardUseCase {

  GraduateDashboardResponse getDashboard(Long graduateId);
}
