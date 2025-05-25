package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.DirectorRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.DirectorException;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.DirectorResponse;
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

  private final UserMapper userMapper;

  private final DirectorMapper directorMapper;

  @Override
  public void save(DirectorRequest directorRequest) {
    userRepository
        .findByCorreoAndEstado(directorRequest.getCorreo(), ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            userEntity -> {
              throw new DirectorException(
                  String.format(ConstantsUtils.USER_CONFLICT, userEntity.getCorreo()));
            },
            () -> {
              UserRequest userRequest = directorMapper.toUserRequest(directorRequest);
              UserEntity userEntity = userMapper.toEntity(userRequest);
              userEntity = userRepository.save(userEntity); //
              DirectorEntity directorEntity = directorMapper.toEntity(directorRequest, userEntity);
              directorRepository.save(directorEntity);
            });
  }

  @Override
  public void delete(Long id) {
    directorRepository
        .findByUserIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            directorEntity -> directorRepository.desactivateUserByDirectorId(id),
            () -> {
              throw new DirectorException(String.format(ConstantsUtils.DIRECTOR_NOT_FOUND, id));
            });
  }

  @Override
  public List<DirectorResponse> getList() {
    return directorRepository.findAllByUserEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .map(directorMapper::toDomain)
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
            () -> new DirectorException(String.format(ConstantsUtils.DIRECTOR_NOT_FOUND, id)));
  }

  @Override
  public void update(DirectorRequest request, Long id) {
    directorRepository
        .findByIdAndUserEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            directorEntity -> {
              directorMapper.updateDirectorEntity(request, directorEntity);
              return directorRepository.save(directorEntity);
            })
        .orElseThrow(
            () -> new DirectorException(String.format(ConstantsUtils.DIRECTOR_NOT_FOUND, id)));
  }
}
