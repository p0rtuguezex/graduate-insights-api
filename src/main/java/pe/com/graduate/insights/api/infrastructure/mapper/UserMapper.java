package pe.com.graduate.insights.api.infrastructure.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.User;
import pe.com.graduate.insights.api.infrastructure.adapter.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  User toDomain(UserEntity userEntity);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
  UserEntity toEntity(UserRequest userRequest);
}
