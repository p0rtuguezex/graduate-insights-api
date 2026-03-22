package pe.com.graduate.insights.api.features.graduate.infrastructure.adapter;

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
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateRequest;
import pe.com.graduate.insights.api.features.graduate.application.dto.GraduateResponse;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateIdentityRepositoryPort;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateReadRepositoryPort;
import pe.com.graduate.insights.api.features.graduate.application.ports.output.GraduateWriteRepositoryPort;
import pe.com.graduate.insights.api.features.graduate.domain.exception.GraduateException;
import pe.com.graduate.insights.api.features.graduateselfregistration.application.dto.GraduateSelfRegistrationRequest;
import pe.com.graduate.insights.api.features.graduateselfregistration.application.ports.output.GraduateSelfRegistrationRepositoryPort;
import pe.com.graduate.insights.api.features.mail.application.dto.MailRequest;
import pe.com.graduate.insights.api.features.mail.application.ports.output.MailRepositoryPort;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.user.infrastructure.jpa.UserRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.mapper.GraduateMapper;
import pe.com.graduate.insights.api.features.user.infrastructure.mapper.UserMapper;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class GraduateRepositoryAdapter
    implements
        GraduateSelfRegistrationRepositoryPort,
        GraduateIdentityRepositoryPort,
        GraduateReadRepositoryPort,
        GraduateWriteRepositoryPort {

  private final GraduateRepository graduateRepository;
  private final UserRepository userRepository;
  private final GraduateMapper graduateMapper;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final MailRepositoryPort mailRepositoryPort;

  @Override
  public void save(GraduateRequest graduateRequest) {
    userRepository
        .findByCorreoAndEstado(graduateRequest.getCorreo(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            user -> {
              throw new GraduateException(
                  String.format(ConstantsUtils.USER_CONFLICT, user.getCorreo()));
            },
            () -> {
              UserRequest userRequest = graduateMapper.toGraduateRequest(graduateRequest);
              UserEntity userEntity = userMapper.toEntity(userRequest);
              userEntity.setContrasena(passwordEncoder.encode(userEntity.getContrasena()));
              userEntity = userRepository.save(userEntity);
              GraduateEntity graduateEntity = graduateMapper.toEntity(graduateRequest, userEntity);
              graduateEntity.setValidated(
                  graduateRequest.getValidated() != null
                      ? graduateRequest.getValidated()
                      : Boolean.TRUE);
              graduateRepository.save(graduateEntity);
            });
    sendVerificationCode(graduateRequest.getCorreo());
  }

  @Override
  public List<KeyValueResponse> getList() {
    Comparator<GraduateEntity> comparator =
        Comparator.comparing(
                GraduateEntity::getCreatedDate, Comparator.nullsLast(Comparator.naturalOrder()))
            .reversed();

    return graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .sorted(comparator)
        .map(graduateMapper::toKeyValueResponse)
        .toList();
  }

  @Override
  public Page<GraduateResponse> getPagination(String search, Pageable pageable) {
    return getPagination(search, pageable, null);
  }

  @Override
  public Page<GraduateResponse> getPagination(String search, Pageable pageable, Boolean validated) {
    boolean hasSearch = StringUtils.isNotBlank(search);

    Page<GraduateEntity> graduatesEntities;
    if (validated == null) {
      graduatesEntities =
          hasSearch
              ? graduateRepository.findAllByUserEstadoSearch(
                  search, ConstantsUtils.STATUS_ACTIVE, pageable)
              : graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE, pageable);
    } else {
      graduatesEntities =
          hasSearch
              ? graduateRepository.findAllByUserEstadoAndValidatedSearch(
                  search, ConstantsUtils.STATUS_ACTIVE, validated, pageable)
              : graduateRepository.findAllByUserEstadoAndValidated(
                  ConstantsUtils.STATUS_ACTIVE, validated, pageable);
    }

    List<GraduateResponse> graduateResponseList =
        graduatesEntities.getContent().stream().map(graduateMapper::toDomain).toList();
    return new PageImpl<>(graduateResponseList, pageable, graduatesEntities.getTotalElements());
  }

  @Override
  public GraduateResponse getDomain(Long id) {
    return graduateRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(graduateMapper::toDomain)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, id)));
  }

  @Override
  public void update(GraduateRequest request, Long id) {
    graduateRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            graduateEntity -> {
              graduateMapper.updateGraduateEntity(request, graduateEntity);
              graduateEntity
                  .getUser()
                  .setContrasena(passwordEncoder.encode(request.getContrasena()));
              if (request.getValidated() != null) {
                graduateEntity.setValidated(request.getValidated());
              }
              return graduateRepository.save(graduateEntity);
            })
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, id)));
  }

  @Override
  public void delete(Long id) {
    graduateRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            graduate -> graduateRepository.deactivateUserByGraduateId(id),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.GRADUATE_NOT_FOUND, id));
            });
  }

  public void updateCvPath(Long graduateId, String cvPath) {
    graduateRepository
        .findByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            graduate -> {
              graduate.setCvPath(cvPath);
              graduateRepository.save(graduate);
            },
            () -> {
              throw new NotFoundException(
                  String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId));
            });
  }

  @Override
  public void updateValidationStatus(Long graduateId, Boolean validated) {
    graduateRepository
        .findByIdAndUserEstado(graduateId, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            graduate -> {
              graduate.setValidated(Boolean.TRUE.equals(validated));
              graduateRepository.save(graduate);
            },
            () -> {
              throw new NotFoundException(
                  String.format(ConstantsUtils.GRADUATE_NOT_FOUND, graduateId));
            });
  }

  @Override
  public Long getActiveGraduateIdByUserId(Long userId) {
    return graduateRepository
        .findByUserIdAndUserEstado(userId, ConstantsUtils.STATUS_ACTIVE)
        .map(GraduateEntity::getId)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.USER_NOT_FOUND, userId)));
  }

  @Override
  public void register(GraduateSelfRegistrationRequest request) {
    userRepository
        .findByCorreoAndEstado(request.getCorreo(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresent(
            user -> {
              throw new GraduateException(
                  String.format(ConstantsUtils.USER_CONFLICT, user.getCorreo()));
            });

    UserEntity userEntity = new UserEntity();
    userEntity.setNombres(request.getNombres());
    userEntity.setApellidos(request.getApellidos());
    userEntity.setDni(request.getDni());
    userEntity.setCelular(request.getCelular());
    userEntity.setCorreo(request.getCorreo().toLowerCase());
    userEntity.setGenero("Otro");
    userEntity.setEstado(ConstantsUtils.STATUS_ACTIVE);
    userEntity.setVerificado(Boolean.FALSE);
    userEntity.setContrasena(passwordEncoder.encode(request.getContrasena()));
    userEntity = userRepository.save(userEntity);

    GraduateEntity graduateEntity = new GraduateEntity();
    graduateEntity.setUser(userEntity);
    graduateEntity.setValidated(Boolean.FALSE);
    graduateRepository.save(graduateEntity);

    sendVerificationCode(request.getCorreo());
  }

  private void sendVerificationCode(String email) {
    MailRequest mailRequest =
        MailRequest.builder().email(email).type(ConstantsUtils.SENT_CODE_VALIDATED).build();
    try {
      mailRepositoryPort.sendCode(mailRequest);
    } catch (RuntimeException ex) {
      log.warn(
          "No se pudo enviar el código de verificación al correo {}: {}",
          email,
          ex.getMessage(),
          ex);
    }
  }
}



