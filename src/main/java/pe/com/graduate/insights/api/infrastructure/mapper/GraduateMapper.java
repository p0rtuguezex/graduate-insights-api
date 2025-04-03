package pe.com.graduate.insights.api.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GraduateMapper {

  GraduateMapper INSTANCE = Mappers.getMapper(GraduateMapper.class);
}
