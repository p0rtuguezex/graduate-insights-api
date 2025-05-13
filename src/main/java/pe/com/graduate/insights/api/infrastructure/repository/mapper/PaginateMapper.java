package pe.com.graduate.insights.api.infrastructure.repository.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import pe.com.graduate.insights.api.domain.models.response.Paginate;

@Mapper(componentModel = "spring")
public interface PaginateMapper {

  PaginateMapper INSTANCE = Mappers.getMapper(PaginateMapper.class);

  @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
  @Mapping(source = "totalElements", target = "totalElements")
  @Mapping(source = "totalPages", target = "totalPages")
  @Mapping(source = "number", target = "currentPage", qualifiedByName = "incrementPage")
  Paginate toDomain(Page<?> page);

  @Named("incrementPage")
  default Integer incrementPage(Integer number) {
    return number + 1;
  }
}
