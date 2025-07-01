package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.DirectorEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EmployerEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.DirectorRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EmployerRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;

@Component
@RequiredArgsConstructor
public class ValidationUtilsAdapter {
  private final DirectorRepository directorRepository;


  private Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }

    return authentication;
  }

  // Método público para obtener la autenticación
  public Authentication getCurrentAuthentication() {
    return getAuthentication();
  }

  // Método para verificar si el usuario es director
  public boolean isUserDirector(Long userId) {
    return directorRepository.existsByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE);
  }
}
