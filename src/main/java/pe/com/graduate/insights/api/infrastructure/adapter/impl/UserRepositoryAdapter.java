package pe.com.graduate.insights.api.infrastructure.adapter.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.application.ports.output.UserRepositoryPort;
import pe.com.graduate.insights.api.domain.exception.NotFoundException;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.User;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;
import pe.com.graduate.insights.api.infrastructure.adapter.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.adapter.repository.UserRepository;
import pe.com.graduate.insights.api.infrastructure.mapper.UserMapper;

@Slf4j
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
  public Optional<User> getDomain(Long id) {
    log.info("Get speaker with id: {}", id);
    return userRepository
        .findById(id)
        .map(entity -> Optional.of(userMapper.toDomain(entity)))
        .orElseThrow(() -> new NotFoundException(ConstantsUtils.USER_NOT_FOUND));
  }
}
