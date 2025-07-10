package pe.com.graduate.insights.api.application.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService implements InitializingBean {

  private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".pdf", ".doc", ".docx", ".jpg", ".jpeg", ".png");
  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB en bytes

  @Value("${app.file.upload-dir:./uploads}")
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
   * Guarda un archivo en el sistema de archivos
   *
   * @param file Archivo a guardar
   * @param fileType Tipo de archivo (ej: "cv", "image", "document")
   * @return Ruta relativa del archivo guardado
   */
  public String storeFile(MultipartFile file, String fileType) {
    // Validar tamaño del archivo
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new RuntimeException("El archivo excede el tamaño máximo permitido de 10MB");
    }

    // Validar extensión del archivo
    String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
    if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
      throw new RuntimeException("Tipo de archivo no permitido. Extensiones permitidas: " + String.join(", ", ALLOWED_EXTENSIONS));
    }

    // Generar nombre único para el archivo
    String fileName = fileType + "_" + UUID.randomUUID() + fileExtension;

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
   * Carga un archivo desde el sistema de archivos
   *
   * @param fileName Nombre del archivo
   * @return Recurso del archivo
   */
  public Resource loadFileAsResource(String fileName) {
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
   * Elimina un archivo del sistema de archivos
   *
   * @param fileName Nombre del archivo a eliminar
   * @return true si el archivo fue eliminado, false si no existía
   */
  public boolean deleteFile(String fileName) {
    if (fileName != null && !fileName.trim().isEmpty()) {
      try {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        return Files.deleteIfExists(filePath);
      } catch (IOException ex) {
        throw new RuntimeException("Error al eliminar el archivo: " + fileName, ex);
      }
    }
    return false;
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

  /**
   * Obtiene el tipo de contenido del archivo
   *
   * @param fileName Nombre del archivo
   * @return MediaType correspondiente al archivo
   */
  public MediaType getFileMediaType(String fileName) {
    String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    return switch (extension) {
      case ".pdf" -> MediaType.APPLICATION_PDF;
      case ".doc", ".docx" -> MediaType.parseMediaType("application/msword");
      case ".jpg", ".jpeg" -> MediaType.IMAGE_JPEG;
      case ".png" -> MediaType.IMAGE_PNG;
      default -> MediaType.APPLICATION_OCTET_STREAM;
    };
  }
}
