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

  private final GraduateRepository graduateRepository;
  private final EmployerRepository employerRepository;
  private final DirectorRepository directorRepository;
  private final AuthRepositoryAdapter authRepositoryAdapter;

  public GraduateEntity getAuthenticatedGraduateId(Long graduateId) {
    if (graduateId != null) {
      return graduateRepository
          .findByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)
          .orElseThrow(
              () ->
                  new NotFoundException(
                      String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId)));
    }
    Authentication authentication = getAuthentication();
    var user = authRepositoryAdapter.getCurrentUser(authentication);

    // Obtener el graduado asociado al usuario autenticado
    return graduateRepository
        .findByUserIdAndUserEstado(user.getId(), ConstantsUtils.STATUS_ACTIVE)
        .orElseThrow(
            () ->
                new NotFoundException("El usuario autenticado no es un graduado o no está activo"));
  }

  public EmployerEntity getAuthenticatedEmployerId(Long employerId) {
    if (employerId != null) {
      return employerRepository
          .findByIdAndUserEstado(employerId, ConstantsUtils.STATUS_ACTIVE)
          .orElseThrow(
              () ->
                  new NotFoundException(
                      String.format(ConstantsUtils.EMPLOYER_NOT_FOUND, employerId)));
    }
    Authentication authentication = getAuthentication();
    var user = authRepositoryAdapter.getCurrentUser(authentication);

    // Obtener el empleador asociado al usuario autenticado
    return employerRepository
        .findByUserIdAndUserEstado(user.getId(), ConstantsUtils.STATUS_ACTIVE)
        .orElseThrow(
            () ->
                new NotFoundException(
                    "El usuario autenticado no es un empleador o no está activo"));
  }

  public DirectorEntity getAuthenticatedDirectorId(Long directorId) {
    if (directorId != null) {
      return directorRepository
          .findByIdAndUserEstado(directorId, ConstantsUtils.STATUS_ACTIVE)
          .orElseThrow(
              () ->
                  new NotFoundException(
                      String.format(ConstantsUtils.EMPLOYER_NOT_FOUND, directorId)));
    }

    Authentication authentication = getAuthentication();
    var user = authRepositoryAdapter.getCurrentUser(authentication);

    // Obtener el director asociado al usuario autenticado
    return directorRepository
        .findByUserIdAndUserEstado(user.getId(), ConstantsUtils.STATUS_ACTIVE)
        .orElseThrow(
            () ->
                new NotFoundException("El usuario autenticado no es un director o no está activo"));
  }

  private Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
    }

    return authentication;
  }
}
