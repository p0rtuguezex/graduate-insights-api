package pe.com.graduate.insights.api.infrastructure.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.EventTypesUseCase;
import pe.com.graduate.insights.api.domain.models.request.EventTypesRequest;
import pe.com.graduate.insights.api.domain.models.response.ApiResponse;
import pe.com.graduate.insights.api.domain.models.response.EventTypesResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/event_types")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DIRECTOR')")
public class EventTypesController {

  private final EventTypesUseCase eventTypesUseCase;

  private final PaginateMapper paginateMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<EventTypesResponse>> getEducationCenter(@PathVariable Long id) {
    return ResponseUtils.successResponse(eventTypesUseCase.getDomain(id));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Void>> saveEducationCenter(
      @Valid @RequestBody EventTypesRequest eventTypesRequest) {
    eventTypesUseCase.save(eventTypesRequest);
    return ResponseUtils.sucessCreateResponse();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> updateEducationCenter(
      @RequestBody EventTypesRequest eventTypesRequest, @PathVariable Long id) {
    eventTypesUseCase.update(eventTypesRequest, id);
    return ResponseUtils.successUpdateResponse();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteEducationCenter(@PathVariable Long id) {
    eventTypesUseCase.delete(id);
    return ResponseUtils.successDeleteResponse();
  }

  @GetMapping
  ResponseEntity<ApiResponse<List<EventTypesResponse>>> getListEducationCenterPaginate(
      @RequestParam(value = "search", defaultValue = "") String search,
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "10") String size) {
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<EventTypesResponse> eventTypesPage = eventTypesUseCase.getPagination(search, pageable);
    return ResponseUtils.successResponsePaginate(
        eventTypesPage.getContent(), paginateMapper.toDomain(eventTypesPage));
  }

  @GetMapping("/list")
  public ResponseEntity<ApiResponse<List<EventTypesResponse>>> getListEducationCenterAll() {
    return ResponseUtils.successResponse(eventTypesUseCase.getList());
  }
}
