package pe.com.graduate.insights.api.features.user.infrastructure.adapter;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;
import pe.com.graduate.insights.api.features.user.application.dto.ProfileUpdateRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;
import pe.com.graduate.insights.api.features.user.application.ports.output.UserRepositoryPort;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.jpa.UserRepository;
import pe.com.graduate.insights.api.features.user.infrastructure.mapper.UserMapper;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void save(UserRequest request) {
    UserEntity userEntity = userMapper.toEntity(request);
    userRepository.save(userEntity);
  }

  @Override
  public UserResponse getDomain(Long id) {
    return userRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(userMapper::toDomain)
        .orElseThrow(() -> new NotFoundException(String.format(ConstantsUtils.USER_NOT_FOUND, id)));
  }

  @Override
  public void update(UserRequest request, Long id) {
    userRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            userEntity -> {
              userMapper.updateUserEntity(request, userEntity);
              return userRepository.save(userEntity);
            })
        .orElseThrow(() -> new NotFoundException(String.format(ConstantsUtils.USER_NOT_FOUND, id)));
  }

  @Override
  public void delete(Long id) {
    userRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            user -> userRepository.updateStatusById(id, ConstantsUtils.STATUS_INACTIVE),
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.USER_NOT_FOUND, id));
            });
  }

  @Override
  public Page<UserResponse> getPagination(String search, Pageable pageable) {
    boolean hasSearch = !StringUtils.isEmpty(search);

    Page<UserEntity> userEntities =
        hasSearch
            ? userRepository.findAllByEstadoSearch(search, ConstantsUtils.STATUS_ACTIVE, pageable)
            : userRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE, pageable);

    List<UserResponse> users =
        userEntities.getContent().stream().map(userMapper::toDomain).toList();

    return new PageImpl<>(users, pageable, userEntities.getTotalElements());
  }

  @Override
  public List<UserResponse> getList() {
    return userRepository.findAllByEstado(ConstantsUtils.STATUS_ACTIVE).stream()
        .map(userMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void updateProfile(ProfileUpdateRequest request, Long id) {
    userRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .map(
            userEntity -> {
              userMapper.updateProfile(request, userEntity);
              return userRepository.save(userEntity);
            })
        .orElseThrow(() -> new NotFoundException(String.format(ConstantsUtils.USER_NOT_FOUND, id)));
  }

  @Override
  public void updatePassword(Long id, String newPassword) {
    userRepository
        .findByIdAndEstado(id, ConstantsUtils.STATUS_ACTIVE)
        .ifPresentOrElse(
            userEntity -> {
              String encoded = passwordEncoder.encode(newPassword);
              userRepository.updatePasswordByUserId(encoded, userEntity.getId());
            },
            () -> {
              throw new NotFoundException(String.format(ConstantsUtils.USER_NOT_FOUND, id));
            });
  }
}





