package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.UserRoleRepositoryPort;
import pe.com.graduate.insights.api.domain.models.enums.UserRole;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.DirectorRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EmployerRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;

@Component
@RequiredArgsConstructor
public class UserRoleRepositoryAdapter implements UserRoleRepositoryPort {

  private final DirectorRepository directorRepository;
  private final EmployerRepository employerRepository;
  private final GraduateRepository graduateRepository;

  @Override
  public UserRole getUserRole(Long userId) {
    // Verificar si es director
    if (directorRepository.existsByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)) {
      return UserRole.DIRECTOR;
    }

    // Verificar si es empleador
    if (employerRepository.existsByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)) {
      return UserRole.EMPLOYER;
    }

    // Verificar si es graduado
    if (graduateRepository.existsByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)) {
      return UserRole.GRADUATE;
    }

    // Por defecto, si no encuentra ningún rol específico, retorna graduado
    return UserRole.GRADUATE;
  }

  @Override
  public String getUserRoleDisplayName(Long userId) {
    return getUserRole(userId).getDisplayName();
  }

  @Override
  public String getUserRoleAuthority(Long userId) {
    return getUserRole(userId).getAuthority();
  }
}
