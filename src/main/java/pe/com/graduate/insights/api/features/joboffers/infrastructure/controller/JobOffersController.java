package pe.com.graduate.insights.api.features.joboffers.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.shared.security.UserRole;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersRequest;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersResponse;
import pe.com.graduate.insights.api.shared.utils.ResponseUtils;
import pe.com.graduate.insights.api.features.auth.application.ports.input.CurrentUserUseCase;
import pe.com.graduate.insights.api.features.joboffers.application.ports.input.JobOffersUseCase;
import pe.com.graduate.insights.api.features.userrole.application.ports.input.UserRoleUseCase;
import pe.com.graduate.insights.api.shared.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/job-offers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('DIRECTOR', 'EMPLOYER')")
@Tag(name = "Ofertas de Trabajo", description = "APIs para gestión de ofertas de trabajo")
public class JobOffersController {

  private final JobOffersUseCase jobOffersUseCase;
  private final PaginateMapper paginateMapper;
  private final CurrentUserUseCase currentUserUseCase;
    private final UserRoleUseCase userRoleUseCase;

  private boolean isUserDirector() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = currentUserUseCase.getCurrentUser(authentication);
        UserRole userRole = userRoleUseCase.getUserRole(user.id());
    return userRole == UserRole.DIRECTOR;
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = currentUserUseCase.getCurrentUser(authentication);
    return user.id();
  }

  @Operation(
      summary = "Obtener oferta de trabajo por ID",
      description =
          "Obtiene una oferta de trabajo específica por su ID. Los directores ven todas las ofertas, los empleadores solo ven las suyas.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Oferta de trabajo encontrada exitosamente",
            content = @Content(schema = @Schema(implementation = JobOffersResponse.class))),
        @ApiResponse(responseCode = "404", description = "Oferta de trabajo no encontrada"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.shared.models.response.ApiResponse<JobOffersResponse>>
      getJobOffer(
          @Parameter(description = "ID de la oferta de trabajo", required = true) @PathVariable
              Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    JobOffersResponse jobOffersResponse =
        jobOffersUseCase.getDomainByRole(id, isDirector, currentUserId);
    return ResponseUtils.successResponse(jobOffersResponse);
  }

  @Operation(
      summary = "Crear nueva oferta de trabajo",
      description =
          "Crea una nueva oferta de trabajo. Los empleadores solo pueden crear ofertas para su empresa.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Oferta de trabajo creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Oferta de trabajo ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.shared.models.response.ApiResponse<Void>>
      createJobOffer(
          @Parameter(description = "Datos de la oferta de trabajo", required = true)
              @Valid
              @RequestBody
              JobOffersRequest jobOffersRequest) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobOffersUseCase.saveByRole(jobOffersRequest, isDirector, currentUserId);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar oferta de trabajo",
      description =
          "Actualiza una oferta de trabajo existente. Los empleadores solo pueden actualizar sus propias ofertas.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Oferta de trabajo actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Oferta de trabajo no encontrada")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.shared.models.response.ApiResponse<Void>>
      updateJobOffer(
          @Parameter(description = "Datos de la oferta de trabajo", required = true) @RequestBody
              JobOffersRequest jobOffersRequest,
          @Parameter(description = "ID de la oferta de trabajo", required = true) @PathVariable
              Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobOffersUseCase.updateByRole(jobOffersRequest, id, isDirector, currentUserId);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar oferta de trabajo",
      description =
          "Elimina una oferta de trabajo específica por su ID. Los empleadores solo pueden eliminar sus propias ofertas.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Oferta de trabajo eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Oferta de trabajo no encontrada")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.shared.models.response.ApiResponse<Void>>
      deleteJobOffer(
          @Parameter(description = "ID de la oferta de trabajo", required = true) @PathVariable
              Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobOffersUseCase.deleteByRole(id, isDirector, currentUserId);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar ofertas de trabajo paginadas",
      description =
          "Obtiene una lista paginada de ofertas de trabajo con búsqueda opcional. Los directores ven todas las ofertas, los empleadores solo ven las suyas.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de ofertas de trabajo obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = JobOffersResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<
          pe.com.graduate.insights.api.shared.models.response.ApiResponse<List<JobOffersResponse>>>
      getListJobOffersPaginate(
          @Parameter(description = "Término de búsqueda", example = "desarrollador")
              @RequestParam(value = "search", defaultValue = "")
              String search,
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size), sort);
    Page<JobOffersResponse> jobOffersPage =
        jobOffersUseCase.getPaginationByRole(search, pageable, isDirector, currentUserId);
    return ResponseUtils.successResponsePaginate(
        jobOffersPage.getContent(), paginateMapper.toDomain(jobOffersPage));
  }
}




