package pe.com.graduate.insights.api.features.graduatedashboard.application.ports.input;

import pe.com.graduate.insights.api.features.graduatedashboard.application.dto.GraduateDashboardResponse;

public interface GraduateDashboardUseCase {

  GraduateDashboardResponse getDashboard(Long graduateId);
}
