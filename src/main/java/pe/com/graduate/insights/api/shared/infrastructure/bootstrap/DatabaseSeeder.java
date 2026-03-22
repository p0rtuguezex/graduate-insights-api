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

  private void ensureAdminDirector() {
    final String normalizedEmail = adminEmail.trim().toLowerCase();

    UserEntity adminUser =
        userRepository
            .findByCorreo(normalizedEmail)
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




