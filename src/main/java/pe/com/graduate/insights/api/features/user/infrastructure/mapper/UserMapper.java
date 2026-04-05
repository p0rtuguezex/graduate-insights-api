package pe.com.graduate.insights.api.features.user.infrastructure.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.features.user.application.dto.ProfileUpdateRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "id", target = "userId")
  UserResponse toDomain(UserEntity userEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "estado", constant = "1")
  UserEntity toEntity(UserRequest userRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "estado", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "verificado", ignore = true)
  @Mapping(target = "codeConfirm", ignore = true)
  @Mapping(target = "passwordRecoveryCode", ignore = true)
  @Mapping(target = "userRole", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  void updateUserEntity(UserRequest userRequest, @MappingTarget UserEntity userEntity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "estado", ignore = true)
  @Mapping(target = "createdDate", ignore = true)
  @Mapping(target = "modifiedDate", ignore = true)
  @Mapping(target = "verificado", ignore = true)
  @Mapping(target = "codeConfirm", ignore = true)
  @Mapping(target = "passwordRecoveryCode", ignore = true)
  @Mapping(target = "userRole", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "contrasena", ignore = true)
  void updateProfile(ProfileUpdateRequest request, @MappingTarget UserEntity userEntity);
}
