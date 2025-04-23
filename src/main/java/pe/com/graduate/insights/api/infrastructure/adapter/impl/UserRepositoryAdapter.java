package pe.com.graduate.insights.api.infrastructure.adapter.impl;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class UserRepositoryAdapter implements UserRepositoryPort {

  @Autowired private UserRepository userRepository;

  @Autowired private UserMapper userMapper;

  @Override
  public void save(UserRequest request) {
    UserEntity userEntity = userMapper.toEntity(request);
    userRepository.save(userEntity);
  }

  @Override
  public Optional<User> getDomain(Long id) {
    log.info("el id es :" + id);
    return userRepository
        .findById(id)
        .map(userEntity -> Optional.of(userMapper.toDomain(userEntity)))
        .orElseThrow(() -> new NotFoundException(ConstantsUtils.USER_NOT_FOUND));
  }

  @Override
  public void update(UserRequest request, Long id) {
    log.info("Update User with id :{}" , id);
    userRepository
        .findById(id)
        .map(
            userEntity -> {
              userMapper.updateUserEntity(request, userEntity);
              return userRepository.save(userEntity);
            })
        .orElseThrow(() -> new NotFoundException(ConstantsUtils.USER_NOT_FOUND));
  }

  @Override
  public List<User> getList(Object... params) {
    log.info("Get list Users without pages ");
    return userRepository.findAll().stream()
        .map(userEntity -> userMapper.toDomain(userEntity))
        .toList();
  }

  @Override
  public void delete(Long id) {
    log.info("Delete User with id : {} ",id);
      var user= userRepository.findById(id)
              .orElseThrow(() -> new NotFoundException(ConstantsUtils.USER_NOT_FOUND));
      userRepository.delete(user);

  }

  @Override
  public Page<User> getPagination(String search, Pageable pageable) {
    log.info(" get list of Participants");
    // lista sin ningun tipo de filtro
    Page<UserEntity> userEntities=userRepository.findAll(pageable);
    return  new PageImpl<>(
           userEntities.getContent().stream().map(userEntity -> userMapper.toDomain(userEntity)).toList(),
            userEntities.getPageable(),
            userEntities.getTotalElements());

  }
}
