package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.GraduateRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.GraduateException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.request.MailRequest;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.GraduateEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.UserRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.GraduateMapper;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class GraduateRepositoryAdapter implements GraduateRepositoryPort {

  private final GraduateRepository graduateRepository;
  private final UserRepository userRepository;
  private final GraduateMapper graduateMapper;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final MailRepositoryAdapter mailRepositoryAdapter;

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
              graduateRepository.save(graduateEntity);
            });
    MailRequest mailRequest =
        MailRequest.builder()
            .email(graduateRequest.getCorreo())
            .type(ConstantsUtils.SENT_CODE_VALIDATED)
            .build();
    mailRepositoryAdapter.sendCode(mailRequest);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .map(graduateMapper::toKeyValueResponse)
        .toList();
  }

  @Override
  public Page<GraduateResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<GraduateEntity> graduatesEntities =
        hasSearch
            ? graduateRepository.findAllByUserEstadoSearch(
                search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : graduateRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE, pageable);
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

  @Override
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
}
