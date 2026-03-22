package pe.com.graduate.insights.api.features.userrole.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.userrole.application.ports.output.UserRoleRepositoryPort;
import pe.com.graduate.insights.api.features.director.infrastructure.jpa.DirectorRepository;
import pe.com.graduate.insights.api.features.employer.infrastructure.jpa.EmployerRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.shared.security.UserRole;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@Component
@RequiredArgsConstructor
public class UserRoleRepositoryAdapter implements UserRoleRepositoryPort {

  private final DirectorRepository directorRepository;
  private final EmployerRepository employerRepository;
  private final GraduateRepository graduateRepository;

  @Override
  public UserRole getUserRole(Long userId) {
    if (directorRepository.existsByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)) {
      return UserRole.DIRECTOR;
    }

    if (employerRepository.existsByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)) {
      return UserRole.EMPLOYER;
    }

    if (graduateRepository.existsByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)) {
      return UserRole.GRADUATE;
    }

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



