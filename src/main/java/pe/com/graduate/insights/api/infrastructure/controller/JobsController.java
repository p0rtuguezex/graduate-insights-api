package pe.com.graduate.insights.api.infrastructure.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.application.ports.input.JobUseCase;
import pe.com.graduate.insights.api.application.service.UserRoleService;
import pe.com.graduate.insights.api.domain.models.enums.UserRole;
import pe.com.graduate.insights.api.domain.models.request.JobRequest;
import pe.com.graduate.insights.api.domain.models.response.JobResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasAnyRole('DIRECTOR', 'GRADUATE')")
@Tag(name = "Trabajos", description = "APIs para gestión de trabajos de graduados")
public class JobsController {

  private final JobUseCase jobUseCase;
  private final PaginateMapper paginateMapper;
  private final AuthUseCase authUseCase;
  private final UserRoleService userRoleService;

  /**
   * Función para identificar el rol del usuario autenticado. - DIRECTOR: Ve todos los trabajos con
   * todos los atributos (incluye graduate_id) - GRADUATE: Ve solo sus trabajos sin el atributo
   * graduate_id
   */
  private boolean isUserDirector() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = authUseCase.getCurrentUser(authentication);
    UserRole userRole = userRoleService.getUserRole(user.getId());
    return userRole == UserRole.DIRECTOR;
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var user = authUseCase.getCurrentUser(authentication);
    return user.getId();
  }

  @Operation(
      summary = "Obtener trabajo por ID",
      description =
          "Obtiene un trabajo específico por su ID. Los directores ven todos los trabajos, los graduados solo ven los suyos.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Trabajo encontrado exitosamente",
            content = @Content(schema = @Schema(implementation = JobResponse.class))),
        @ApiResponse(responseCode = "404", description = "Trabajo no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/{id}")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<JobResponse>>
      getJob(@Parameter(description = "ID del trabajo", required = true) @PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    JobResponse jobResponse = jobUseCase.getDomainByRole(id, isDirector, currentUserId);
    return ResponseUtils.successResponse(jobResponse);
  }

  @Operation(
      summary = "Crear nuevo trabajo",
      description =
          "Crea un nuevo trabajo. Los graduados solo pueden crear trabajos para sí mismos.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Trabajo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "409", description = "Trabajo ya existe")
      })
  @PostMapping
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      saveJob(
          @Parameter(description = "Datos del trabajo", required = true) @Valid @RequestBody
              JobRequest jobRequest) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobUseCase.saveByRole(jobRequest, isDirector, currentUserId);
    return ResponseUtils.sucessCreateResponse();
  }

  @Operation(
      summary = "Actualizar trabajo",
      description =
          "Actualiza un trabajo existente. Los graduados solo pueden actualizar sus propios trabajos.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Trabajo actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Trabajo no encontrado")
      })
  @PutMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      updateJob(
          @Parameter(description = "Datos del trabajo", required = true) @RequestBody
              JobRequest jobRequest,
          @Parameter(description = "ID del trabajo", required = true) @PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobUseCase.updateByRole(jobRequest, id, isDirector, currentUserId);
    return ResponseUtils.successUpdateResponse();
  }

  @Operation(
      summary = "Eliminar trabajo",
      description =
          "Elimina un trabajo específico por su ID. Los graduados solo pueden eliminar sus propios trabajos.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Trabajo eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Trabajo no encontrado")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteJob(@Parameter(description = "ID del trabajo", required = true) @PathVariable Long id) {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    jobUseCase.deleteByRole(id, isDirector, currentUserId);
    return ResponseUtils.successDeleteResponse();
  }

  @Operation(
      summary = "Listar trabajos paginados",
      description =
          "Obtiene una lista paginada de trabajos con búsqueda opcional. Los directores ven todos los trabajos, los graduados solo ven los suyos.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de trabajos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = JobResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping
  ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<JobResponse>>>
      getListJobsPaginate(
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

    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<JobResponse> jobPage =
        jobUseCase.getPaginationByRole(search, pageable, isDirector, currentUserId);
    return ResponseUtils.successResponsePaginate(
        jobPage.getContent(), paginateMapper.toDomain(jobPage));
  }

  @Operation(
      summary = "Listar todos los trabajos",
      description =
          "Obtiene una lista completa de trabajos sin paginación. Los directores ven todos los trabajos, los graduados solo ven los suyos.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de trabajos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = KeyValueResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/list")
  public ResponseEntity<List<KeyValueResponse>> getListJobsAll() {
    boolean isDirector = isUserDirector();
    Long currentUserId = getCurrentUserId();

    List<KeyValueResponse> jobList = jobUseCase.getListByRole(isDirector, currentUserId);
    return new ResponseEntity<>(jobList, HttpStatus.OK);
  }
}
