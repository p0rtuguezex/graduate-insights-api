package pe.com.graduate.insights.api.shared.infrastructure.bootstrap;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;
import pe.com.graduate.insights.api.features.director.infrastructure.entity.DirectorEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.features.director.infrastructure.jpa.DirectorRepository;
import pe.com.graduate.insights.api.features.user.infrastructure.jpa.UserRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

  private static final List<String> SEED_TABLES =
      List.of("usuarios", "directores", "tipos_evento", "tipos_encuesta");

    private static final List<String> ACADEMIC_SEED_TABLES =
      List.of(
        "facultades",
        "escuelas_profesionales",
        "tipos_grado",
        "modalidades_titulacion",
        "idiomas_catalogo",
        "universidades");

  private final JdbcTemplate jdbcTemplate;
  private final DataSource dataSource;
  private final UserRepository userRepository;
  private final DirectorRepository directorRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("classpath:inserts.sql")
  private Resource dataScript;

  @Value("${app.seed.admin-email}")
  private String adminEmail;

  @Value("${app.seed.admin-password}")
  private String adminPassword;

  @Override
  public void run(String... args) {
    seedCatalogDataIfNeeded();
    seedAcademicCatalogDataIfNeeded();
    ensureAdminDirector();
  }

  private void seedCatalogDataIfNeeded() {
    if (hasCatalogData()) {
      log.info("Catalog data already present. Skipping inserts.sql execution.");
      return;
    }

    log.info("No catalog data detected. Loading default data from inserts.sql");
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator(dataScript);
    populator.execute(dataSource);
  }

  private boolean hasCatalogData() {
    return SEED_TABLES.stream().anyMatch(this::tableHasRows);
  }

  private boolean tableHasRows(String tableName) {
    try {
      Long count =
          jdbcTemplate.queryForObject(String.format("SELECT COUNT(*) FROM %s", tableName), Long.class);
      return count != null && count > 0;
    } catch (DataAccessException exception) {
      log.debug("Seed table {} unavailable when checking seed state", tableName, exception);
      return false;
    }
  }

  private void seedAcademicCatalogDataIfNeeded() {
    if (ACADEMIC_SEED_TABLES.stream().allMatch(this::tableHasRows)) {
      log.info("Academic catalog data already present. Skipping academic seed.");
      return;
    }

    log.info("Seeding academic catalogs with default values");
    jdbcTemplate.execute(
        """
        INSERT INTO facultades (id, nombre, estado, fecha_creacion, fecha_modificacion)
        VALUES (1, 'Ingenieria civil e ingenieria de sistemas', '1', NOW(), NOW())
        ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), estado = VALUES(estado), fecha_modificacion = NOW()
        """);

    jdbcTemplate.execute(
        """
        INSERT INTO escuelas_profesionales (id, facultad_id, nombre, estado, fecha_creacion, fecha_modificacion)
        VALUES
          (1, 1, 'Ingenieria de sistemas', '1', NOW(), NOW()),
          (2, 1, 'Ingenieria civil', '1', NOW(), NOW())
        ON DUPLICATE KEY UPDATE facultad_id = VALUES(facultad_id), nombre = VALUES(nombre), estado = VALUES(estado), fecha_modificacion = NOW()
        """);

    jdbcTemplate.execute(
        """
        INSERT INTO tipos_grado (id, codigo, nombre, estado, fecha_creacion, fecha_modificacion)
        VALUES
          (1, 'BACHILLER', 'Bachiller', '1', NOW(), NOW()),
          (2, 'TITULADO', 'Titulado', '1', NOW(), NOW()),
          (3, 'MAESTRIA', 'Maestria', '1', NOW(), NOW()),
          (4, 'DOCTORADO', 'Doctorado', '1', NOW(), NOW()),
          (5, 'OTRO', 'Otro', '1', NOW(), NOW())
        ON DUPLICATE KEY UPDATE codigo = VALUES(codigo), nombre = VALUES(nombre), estado = VALUES(estado), fecha_modificacion = NOW()
        """);

    jdbcTemplate.execute(
        """
        INSERT INTO modalidades_titulacion (id, codigo, nombre, estado, fecha_creacion, fecha_modificacion)
        VALUES
          (1, 'EXAMEN_SUFICIENCIA', 'Examen de suficiencia', '1', NOW(), NOW()),
          (2, 'TESIS', 'Tesis', '1', NOW(), NOW()),
          (3, 'OTROS', 'Otros', '1', NOW(), NOW())
        ON DUPLICATE KEY UPDATE codigo = VALUES(codigo), nombre = VALUES(nombre), estado = VALUES(estado), fecha_modificacion = NOW()
        """);

    jdbcTemplate.execute(
        """
        INSERT INTO idiomas_catalogo (id, codigo, nombre, estado, fecha_creacion, fecha_modificacion)
        VALUES
          (1, 'ES', 'Espanol', '1', NOW(), NOW()),
          (2, 'EN', 'Ingles', '1', NOW(), NOW())
        ON DUPLICATE KEY UPDATE codigo = VALUES(codigo), nombre = VALUES(nombre), estado = VALUES(estado), fecha_modificacion = NOW()
        """);

    jdbcTemplate.execute(
        """
        INSERT INTO universidades (id, nombre, estado, fecha_creacion, fecha_modificacion)
        VALUES
          (1, 'Universidad Nacional San Luis Gonzaga', '1', NOW(), NOW())
        ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), estado = VALUES(estado), fecha_modificacion = NOW()
        """);
  }

  private void ensureAdminDirector() {
    final String normalizedEmail = adminEmail.trim().toLowerCase();

    UserEntity adminUser =
        userRepository
            .findByCorreo(normalizedEmail)
            .map(existing -> {
              if (existing.getContrasena() == null || existing.getContrasena().isBlank()) {
                existing.setContrasena(passwordEncoder.encode(adminPassword));
                existing.setVerificado(true);
                userRepository.save(existing);
                log.info("Admin director password was empty, re-encoded for {}", normalizedEmail);
              }
              return existing;
            })
            .orElseGet(
                () -> {
                  UserEntity user = new UserEntity();
                  user.setNombres("Director");
                  user.setApellidos("Académico");
                  user.setCorreo(normalizedEmail);
                  user.setEstado(ConstantsUtils.STATUS_ACTIVE);
                  user.setGenero("M");
                  user.setFechaNacimiento(LocalDate.of(1970, 1, 1));
                  user.setDni("12345670");
                  user.setCelular("987654343");
                  user.setVerificado(true);
                  user.setContrasena(passwordEncoder.encode(adminPassword));
                  UserEntity saved = userRepository.save(user);
                  log.info("Default director admin created with email {}", normalizedEmail);
                  return saved;
                });

    directorRepository
        .findByUserIdAndUserEstado(adminUser.getId(), ConstantsUtils.STATUS_ACTIVE)
        .orElseGet(
            () -> {
              DirectorEntity director = new DirectorEntity();
              director.setUser(adminUser);
              DirectorEntity saved = directorRepository.save(director);
              log.info("Director record created for admin user {}", normalizedEmail);
              return saved;
            });
  }
}




