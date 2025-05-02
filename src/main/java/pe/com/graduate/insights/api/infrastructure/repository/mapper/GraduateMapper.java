package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GraduateMapper {

  GraduateMapper INSTANCE = Mappers.getMapper(GraduateMapper.class);
}
