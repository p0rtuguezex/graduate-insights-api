package pe.com.graduate.insights.api.infrastructure.repository.adapter;

import lombok.RequiredArgsConstructor;
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
}
