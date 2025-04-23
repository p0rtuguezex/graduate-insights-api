package pe.com.graduate.insights.api.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.infrastructure.adapter.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface GraduateMapper {

  GraduateMapper INSTANCE = Mappers.getMapper(GraduateMapper.class);

  UserEntity userRequestoToUserEntity(UserRequest userRequest);

  UserRequest userEntityToUserRequest(UserEntity userEntity);
}
