package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.EmployerRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.GraduateException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.EmployerRequest;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.EmployerResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.EmployerEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EmployerRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.UserRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.EmployerMapper;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class EmployerRepositoryAdapter implements EmployerRepositoryPort {

  private final EmployerRepository employerRepository;
  private final EmployerMapper employerMapper;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void save(EmployerRequest request) {
    userRepository
        .findByCorreoAndEstado(request.getCorreo(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            user -> {
              throw new GraduateException(
                  String.format(ConstantsUtils.USER_CONFLICT, user.getCorreo()));
            },
            () -> {
              UserRequest userRequest = employerMapper.toEmployerRequest(request);
              UserEntity userEntity = userMapper.toEntity(userRequest);
              userEntity.setContrasena(passwordEncoder.encode(userEntity.getContrasena()));
              userEntity = userRepository.save(userEntity);
              EmployerEntity employerEntity = employerMapper.toEntity(request, userEntity);
              employerRepository.save(employerEntity);
            });
  }

  @Override
  public void delete(Long id) {
    employerRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            graduate -> employerRepository.deactivateUserByEmployerId(id),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, id));
            });
  }

  @Override
  public List<KeyValueResponse> getList() {
    return employerRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .map(employerMapper::toKeyValueResponse)
        .toList();
  }

  @Override
  public Page<EmployerResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<EmployerEntity> employerEntities =
        hasSearch
            ? employerRepository.findAllByUserEstadoSearch(
                search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : employerRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE, pageable);
    List<EmployerResponse> graduateResponseList =
        employerEntities.getContent().stream().map(employerMapper::toDomain).toList();
    return new PageImpl<>(graduateResponseList, pageable, employerEntities.getTotalElements());
  }

  @Override
  public EmployerResponse getDomain(Long id) {
    return employerRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(employerMapper::toDomain)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, id)));
  }

  @Override
  public void update(EmployerRequest request, Long id) {
    employerRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            graduateEntity -> {
              employerMapper.updateEmployerEntity(request, graduateEntity);
              graduateEntity
                  .getUser()
                  .setContrasena(passwordEncoder.encode(request.getContrasena()));
              return employerRepository.save(graduateEntity);
            })
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, id)));
  }
}
