package pe.com.graduate.insights.api.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.application.ports.input.HomeFeedUseCase;
import pe.com.graduate.insights.api.domain.models.response.HomeFeedResponse;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;
import pe.com.graduate.insights.api.infrastructure.repository.mapper.PaginateMapper;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@PreAuthorize("hasAnyRole('DIRECTOR', 'GRADUATE')")
@Tag(
    name = "Home Feed",
    description = "APIs para el feed principal con eventos y ofertas laborales")
public class HomeController {

  private final HomeFeedUseCase homeFeedUseCase;
  private final PaginateMapper paginateMapper;

  @Operation(
      summary = "Obtener feed principal paginado",
      description =
          "Obtiene una lista combinada de eventos y ofertas laborales ordenados por fecha descendente")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Feed principal obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = HomeFeedResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/feed")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<HomeFeedResponse>>>
      getHomeFeedPaginated(
          @Parameter(description = "Número de página", example = "1")
              @RequestParam(value = "page", defaultValue = "1")
              String page,
          @Parameter(description = "Tamaño de página", example = "10")
              @RequestParam(value = "size", defaultValue = "10")
              String size) {

    Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
    Page<HomeFeedResponse> homeFeedPage = homeFeedUseCase.getHomeFeed(pageable);

    return ResponseUtils.successResponsePaginate(
        homeFeedPage.getContent(), paginateMapper.toDomain(homeFeedPage));
  }

  @Operation(
      summary = "Obtener feed principal reciente",
      description =
          "Obtiene los elementos más recientes del feed principal (eventos y ofertas laborales)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Feed principal reciente obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = HomeFeedResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/feed/recent")
  public ResponseEntity<
          pe.com.graduate.insights.api.domain.models.response.ApiResponse<List<HomeFeedResponse>>>
      getRecentHomeFeed(
          @Parameter(description = "Número máximo de elementos", example = "10")
              @RequestParam(value = "limit", defaultValue = "10")
              int limit) {

    List<HomeFeedResponse> recentFeed = homeFeedUseCase.getRecentHomeFeed(limit);
    return ResponseUtils.successResponse(recentFeed);
  }
}
