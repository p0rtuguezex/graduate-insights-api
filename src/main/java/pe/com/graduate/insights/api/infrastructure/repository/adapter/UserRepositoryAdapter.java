package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.UserRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.UserResponse;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.UserRepository;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public void save(UserRequest request) {
    UserEntity userEntity = userMapper.toEntity(request);
    userRepository.save(userEntity);
  }

  @Override
  public UserResponse getDomain(Long id) {
    return userRepository
        .findById(id)
        .map(userMapper::toDomain)
        .orElseThrow(() -> new NotFoundException(ConstantsUtils.USER_NOT_FOUND));
  }

  @Override
  public void update(UserRequest request, Long id) {
    userRepository
        .findById(id)
        .map(
            userEntity -> {
              userMapper.updatedUserEntity(request, userEntity);
              return userRepository.save(userEntity);
            })
        .orElseThrow(() -> new NotFoundException(ConstantsUtils.USER_NOT_FOUND));
  }

  @Override
  public List<UserResponse> getList(Object... params) {
    return userRepository.findAll().stream().map(userMapper::toDomain).toList();
  }

  @Override
  public void delete(Long id) {
    var user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException(ConstantsUtils.USER_NOT_FOUND));
    userRepository.delete(user);
  }

  @Override
  public Page<UserResponse> getPagination(String search, Pageable pageable) {

    Page<UserEntity> userEntities = userRepository.findAll(pageable);

    return new PageImpl<>(
        userEntities.getContent().stream()
            .map(userEntity -> userMapper.toDomain(userEntity))
            .toList(),
        userEntities.getPageable(),
        userEntities.getTotalElements());
  }
}
