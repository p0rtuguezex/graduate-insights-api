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
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "Archivos", description = "APIs para gestión de archivos")
public class FileController {

  private final FileService fileService;

  @Operation(
      summary = "Subir archivo",
      description = "Sube un archivo y devuelve el path del archivo guardado. Soporta PDF, DOC, DOCX, JPG, JPEG y PNG.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Archivo subido exitosamente",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Archivo inválido o excede el tamaño máximo (10MB)"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @PreAuthorize("hasRole('DIRECTOR')")
  @PostMapping("/upload")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<String>>
      uploadFile(
          @Parameter(description = "Archivo a subir", required = true)
              @RequestParam("file") MultipartFile file,
          @Parameter(description = "Tipo de archivo (ej: cv, image, document)", required = true)
              @RequestParam("fileType") String fileType) {
    
    String filePath = fileService.storeFile(file, fileType);
    return ResponseUtils.successResponse(filePath);
  }

  @Operation(
      summary = "Descargar archivo por path",
      description = "Descarga un archivo utilizando su path. Compatible con cliente Nuxt")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Archivo descargado exitosamente",
            content = @Content(mediaType = "application/octet-stream")),
        @ApiResponse(responseCode = "404", description = "Archivo no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @GetMapping("/download/{fileName}")
  public ResponseEntity<Resource> downloadFile(
      @Parameter(description = "Nombre del archivo", required = true) 
      @PathVariable String fileName) {

    Resource resource = fileService.loadFileAsResource(fileName);
    MediaType mediaType = fileService.getFileMediaType(fileName);

    return ResponseEntity.ok()
        .contentType(mediaType)
        .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=\"" + fileName + "\"")
        .body(resource);
  }

  @Operation(
      summary = "Eliminar archivo",
      description = "Elimina un archivo del sistema utilizando su path")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Archivo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Archivo no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
      })
  @PreAuthorize("hasRole('DIRECTOR')")
  @DeleteMapping("/{fileName}")
  public ResponseEntity<pe.com.graduate.insights.api.domain.models.response.ApiResponse<Void>>
      deleteFile(
          @Parameter(description = "Nombre del archivo", required = true)
          @PathVariable String fileName) {

    if (!fileService.fileExists(fileName)) {
      throw new RuntimeException("Archivo no encontrado: " + fileName);
    }

    fileService.deleteFile(fileName);
    return ResponseUtils.successDeleteResponse();
  }
} 