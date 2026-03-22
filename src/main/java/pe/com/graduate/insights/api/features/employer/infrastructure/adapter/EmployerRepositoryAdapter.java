package pe.com.graduate.insights.api.features.employer.infrastructure.adapter;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerRequest;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerResponse;
import pe.com.graduate.insights.api.features.employer.application.ports.output.EmployerRepositoryPort;
import pe.com.graduate.insights.api.features.graduate.domain.exception.GraduateException;
import pe.com.graduate.insights.api.features.mail.application.dto.MailRequest;
import pe.com.graduate.insights.api.features.mail.application.ports.output.MailRepositoryPort;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.features.employer.infrastructure.entity.EmployerEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.features.employer.infrastructure.jpa.EmployerRepository;
import pe.com.graduate.insights.api.features.user.infrastructure.jpa.UserRepository;
import pe.com.graduate.insights.api.features.employer.infrastructure.mapper.EmployerMapper;
import pe.com.graduate.insights.api.features.user.infrastructure.mapper.UserMapper;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployerRepositoryAdapter implements EmployerRepositoryPort {

  private final EmployerRepository employerRepository;
  private final EmployerMapper employerMapper;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final MailRepositoryPort mailRepositoryPort;

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
    sendVerificationCode(request.getCorreo());
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
    Comparator<EmployerEntity> comparator =
        Comparator.comparing(
                EmployerEntity::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder()))
            .reversed();

    return employerRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .sorted(comparator)
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

  private void sendVerificationCode(String email) {
    MailRequest mailRequest =
        MailRequest.builder().email(email).type(ConstantsUtils.SENT_CODE_VALIDATED).build();
    try {
      mailRepositoryPort.sendCode(mailRequest);
    } catch (RuntimeException ex) {
      log.warn(
          "No se pudo enviar el código de verificación al empleador {}: {}",
          email,
          ex.getMessage(),
          ex);
    }
  }
}



