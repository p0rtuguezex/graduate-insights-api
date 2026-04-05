package pe.com.graduate.insights.api.shared.infrastructure.storage;

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
import pe.com.graduate.insights.api.features.storage.domain.exception.FileException;
import pe.com.graduate.insights.api.shared.exception.NotFoundException;

@Service
public class FileService implements InitializingBean {

  private static final List<String> ALLOWED_EXTENSIONS =
      Arrays.asList(".pdf", ".doc", ".docx", ".jpg", ".jpeg", ".png");
  private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;

  @Value("${app.file.upload-dir:./uploads}")
  private String uploadDir;

  @Value("${spring.servlet.multipart.location:}")
  private String multipartTmpDir;

  private Path fileStorageLocation;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.fileStorageLocation.resolve("cv"));
      Files.createDirectories(this.fileStorageLocation.resolve("images"));
      if (StringUtils.hasText(multipartTmpDir)) {
        Path tmpLocation = Paths.get(multipartTmpDir).toAbsolutePath().normalize();
        Files.createDirectories(tmpLocation);
      }
    } catch (IOException ex) {
      throw new FileException(
          "No se pudo crear el directorio para almacenar archivos: " + uploadDir, ex);
    }
  }

  private Path getSubDirectory(String fileType) {
    if ("PHOTO".equalsIgnoreCase(fileType)) {
      return fileStorageLocation.resolve("images");
    }
    return fileStorageLocation.resolve("cv");
  }

  private Path resolveFilePath(String fileName) {
    if (fileName.startsWith("PHOTO_")) {
      return fileStorageLocation.resolve("images").resolve(fileName);
    }
    return fileStorageLocation.resolve("cv").resolve(fileName);
  }

  public String storeFile(MultipartFile file, String fileType) {
    if (file.getSize() > MAX_FILE_SIZE) {
      throw new FileException("El archivo excede el tamaño máximo permitido de 10MB");
    }

    String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
    if (!originalFilename.contains(".")) {
      throw new FileException("El archivo no tiene una extensión válida");
    }
    String fileExtension =
        originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
    if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
      throw new FileException(
          "Tipo de archivo no permitido. Extensiones permitidas: "
              + String.join(", ", ALLOWED_EXTENSIONS));
    }

    String fileName = fileType + "_" + UUID.randomUUID() + fileExtension;

    try {
      if (fileName.contains("..")) {
        throw new FileException("El nombre del archivo contiene caracteres inválidos");
      }

      Path targetLocation = getSubDirectory(fileType).resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return fileName;

    } catch (IOException ex) {
      throw new FileException("No se pudo guardar el archivo " + fileName, ex);
    }
  }

  public Resource loadFileAsResource(String fileName) {
    try {
      Path filePath = resolveFilePath(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists()) {
        return resource;
      } else {
        throw new NotFoundException("Archivo no encontrado: " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new FileException("Archivo no encontrado: " + fileName, ex);
    }
  }

  public boolean deleteFile(String fileName) {
    if (fileName != null && !fileName.trim().isEmpty()) {
      try {
        Path filePath = resolveFilePath(fileName).normalize();
        return Files.deleteIfExists(filePath);
      } catch (IOException ex) {
        throw new FileException("Error al eliminar el archivo: " + fileName, ex);
      }
    }
    return false;
  }

  public boolean fileExists(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      return false;
    }
    Path filePath = resolveFilePath(fileName).normalize();
    return Files.exists(filePath);
  }

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
