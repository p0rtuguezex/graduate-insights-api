package pe.com.graduate.insights.api.application.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService implements InitializingBean {

  @Value("${app.file.upload-dir:./uploads/cv}")
  private String uploadDir;

  private Path fileStorageLocation;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (IOException ex) {
      throw new RuntimeException(
          "No se pudo crear el directorio para almacenar archivos: " + uploadDir, ex);
    }
  }

  /**
   * Guarda un archivo CV en el sistema de archivos
   *
   * @param file Archivo a guardar
   * @param graduateId ID del graduado
   * @return Ruta relativa del archivo guardado
   */
  public String storeCvFile(MultipartFile file, Long graduateId) {
    // Validar que el archivo sea PDF
    String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
    if (!originalFilename.toLowerCase().endsWith(".pdf")) {
      throw new RuntimeException("Solo se permiten archivos PDF");
    }

    // Generar nombre único para el archivo
    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
    String fileName = "cv_" + graduateId + "_" + UUID.randomUUID().toString() + fileExtension;

    try {
      // Verificar que el archivo no contenga caracteres maliciosos
      if (fileName.contains("..")) {
        throw new RuntimeException("El nombre del archivo contiene caracteres inválidos");
      }

      // Guardar el archivo
      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return fileName;

    } catch (IOException ex) {
      throw new RuntimeException("No se pudo guardar el archivo " + fileName, ex);
    }
  }

  /**
   * Carga un archivo CV desde el sistema de archivos
   *
   * @param fileName Nombre del archivo
   * @return Recurso del archivo
   */
  public Resource loadCvFileAsResource(String fileName) {
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists()) {
        return resource;
      } else {
        throw new RuntimeException("Archivo no encontrado: " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new RuntimeException("Archivo no encontrado: " + fileName, ex);
    }
  }

  /**
   * Elimina un archivo CV del sistema de archivos
   *
   * @param fileName Nombre del archivo a eliminar
   */
  public void deleteCvFile(String fileName) {
    if (fileName != null && !fileName.trim().isEmpty()) {
      try {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        Files.deleteIfExists(filePath);
      } catch (IOException ex) {
        // El AOP se encargará del logging de excepciones
        throw new RuntimeException("Error al eliminar el archivo: " + fileName, ex);
      }
    }
  }

  /**
   * Verifica si un archivo existe
   *
   * @param fileName Nombre del archivo
   * @return true si existe, false en caso contrario
   */
  public boolean fileExists(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      return false;
    }
    Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
    return Files.exists(filePath);
  }
}
