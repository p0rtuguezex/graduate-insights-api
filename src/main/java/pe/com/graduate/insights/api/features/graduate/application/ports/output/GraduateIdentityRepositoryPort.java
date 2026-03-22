package pe.com.graduate.insights.api.features.graduate.application.ports.output;

public interface GraduateIdentityRepositoryPort {

  Long getActiveGraduateIdByUserId(Long userId);
}