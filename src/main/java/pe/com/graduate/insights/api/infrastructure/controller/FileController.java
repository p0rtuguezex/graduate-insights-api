package pe.com.graduate.insights.api.infrastructure.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.com.graduate.insights.api.application.service.FileService;
import pe.com.graduate.insights.api.domain.utils.ResponseUtils;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Tag(name = "Archivos", description = "APIs para gestión de archivos CV")
public class FileController {

  private final FileService fileService;

  @Operation(
      summary = "Subir archivo CV",
      description = "Sube un archivo CV y devuelve el path del archivo guardado")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Archivo subido exitosamente",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Archivo inválido"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @PreAuthorize("hasRole('DIRECTOR')")
  @PostMapping("/upload-cv")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<String>>
      uploadCv(
          @Parameter(description = "Archivo PDF del CV", required = true)
              @RequestParam("file") MultipartFile file) {
    
    String filePath = fileService.storeCvFile(file);
    return ResponseUtils.successResponse(filePath);
  }

  @Operation(
      summary = "Descargar archivo por path",
      description = "Descarga un archivo CV utilizando su path. Compatible con cliente Nuxt")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Archivo descargado exitosamente",
            content = @Content(mediaType = "application/pdf")),
        @ApiResponse(responseCode = "404", description = "Archivo no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/download/{fileName}")
  public ResponseEntity<Resource> downloadFile(
      @Parameter(description = "Nombre del archivo", required = true) 
      @PathVariable String fileName) {

    Resource resource = fileService.loadCvFileAsResource(fileName);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=\"" + fileName + "\"")
        .body(resource);
  }
} 