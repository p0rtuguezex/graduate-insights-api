package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.DirectorRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.DirectorException;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.request.MailRequest;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.DirectorResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.DirectorEntity;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.DirectorRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.UserRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.DirectorMapper;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class DirectorRepositoryAdapter implements DirectorRepositoryPort {

  private final DirectorRepository directorRepository;
  private final UserRepository userRepository;
  private final DirectorMapper directorMapper;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final MailRepositoryAdapter mailRepositoryAdapter;

  @Override
  public void save(DirectorRequest directorRequest) {
    userRepository
        .findByCorreoAndEstado(directorRequest.getCorreo(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            user -> {
              throw new DirectorException(
                  String.format(ConstantsUtils.USER_CONFLICT, user.getCorreo()));
            },
            () -> {
              UserRequest userRequest = directorMapper.toDirectorRequest(directorRequest);
              UserEntity userEntity = userMapper.toEntity(userRequest);
              userEntity.setContrasena(passwordEncoder.encode(userEntity.getContrasena()));
              userEntity = userRepository.save(userEntity);
              DirectorEntity directorEntity = directorMapper.toEntity(directorRequest, userEntity);
              directorRepository.save(directorEntity);
            });
    MailRequest mailRequest =
        MailRequest.builder()
            .email(directorRequest.getCorreo())
            .type(ConstantsUtils.SENT_CODE_VALIDATED)
            .build();
    mailRepositoryAdapter.sendCode(mailRequest);
  }

  @Override
  public List<KeyValueResponse> getList() {
    return directorRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .map(directorMapper::toKeyValueResponse)
        .toList();
  }

  @Override
  public Page<DirectorResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<DirectorEntity> directorEntities =
        hasSearch
            ? directorRepository.findAllByUserEstadoSearch(
                search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : directorRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE, pageable);
    List<DirectorResponse> directorResponseList =
        directorEntities.getContent().stream().map(directorMapper::toDomain).toList();
    return new PageImpl<>(directorResponseList, pageable, directorEntities.getTotalElements());
  }

  @Override
  public DirectorResponse getDomain(Long id) {
    return directorRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(directorMapper::toDomain)
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.DIRECTOR_NOT_FOUND, id)));
  }

  @Override
  public void update(DirectorRequest request, Long id) {
    directorRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            directorEntity -> {
              directorMapper.updateDirectorEntity(request, directorEntity);
              directorEntity
                  .getUser()
                  .setContrasena(passwordEncoder.encode(request.getContrasena()));
              return directorRepository.save(directorEntity);
            })
        .orElseThrow(
            () -> new NotFoundException(String.format(ConstantsUtils.DIRECTOR_NOT_FOUND, id)));
  }

  @Override
  public void delete(Long id) {
    directorRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            graduate -> directorRepository.deactivateUserByDirectorId(id),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.DIRECTOR_NOT_FOUND, id));
            });
  }
}
