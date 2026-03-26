package pe.com.graduate.insights.api.shared.infrastructure.bootstrap;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.graduate.insights.api.shared.utils.ConstantsUtils;
import pe.com.graduate.insights.api.features.director.infrastructure.entity.DirectorEntity;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;
import pe.com.graduate.insights.api.features.director.infrastructure.jpa.DirectorRepository;
import pe.com.graduate.insights.api.features.user.infrastructure.jpa.UserRepository;
import pe.com.graduate.insights.api.features.graduate.infrastructure.jpa.GraduateRepository;
import pe.com.graduate.insights.api.features.employer.infrastructure.jpa.EmployerRepository;
import pe.com.graduate.insights.api.features.surveytype.infrastructure.entity.SurveyTypeEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus;
import pe.com.graduate.insights.api.features.survey.infrastructure.jpa.SurveyRepository;
import pe.com.graduate.insights.api.features.surveytype.infrastructure.jpa.SurveyTypeRepository;

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
  private final GraduateRepository graduateRepository;
  private final EmployerRepository employerRepository;
  private final SurveyRepository surveyRepository;
  private final SurveyTypeRepository surveyTypeRepository;
  private final PasswordEncoder passwordEncoder;

  private static final String INSERT_USUARIO =
      "INSERT INTO usuarios (nombres, apellidos, fecha_nacimiento, genero, correo, estado, dni, celular, contrasena, verificado, fecha_creacion, fecha_modificacion) "
      + "VALUES (?, ?, ?, ?, ?, '1', ?, ?, ?, true, NOW(), NOW())";

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
    seedMockDataIfNeeded();
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

  private void seedMockDataIfNeeded() {
    long graduateCount = graduateRepository.countByUserEstado(ConstantsUtils.STATUS_ACTIVE);
    if (graduateCount > 0) {
      log.info("Mock data already present. Skipping mock seed.");
      return;
    }

    log.info("Seeding mock graduates, employers and surveys...");
    final String mockPassword = passwordEncoder.encode("Egresys2024*");

    // Graduados masculinos
    createGraduate("Carlos", "Mendoza Torres", "carlos.mendoza@gmail.com",
        "M", "2000-05-15", "40123456", "987111001", mockPassword,
        "20201001", "Soltero", "Peruana", "ica@gmail.com",
        "Av. Los Jardines 123", "Ica", "Ica", "Peru", 1L, "2020", "2024");

    createGraduate("Luis", "García Ramos", "luis.garcia@gmail.com",
        "M", "1999-08-22", "40234567", "987111002", mockPassword,
        "20191002", "Casado", "Peruana", "luis.ica@gmail.com",
        "Jr. Las Flores 456", "Ica", "Ica", "Peru", 1L, "2019", "2023");

    createGraduate("Andrés", "Quispe Vargas", "andres.quispe@gmail.com",
        "M", "2001-03-10", "40345678", "987111003", mockPassword,
        "20211003", "Soltero", "Peruana", "andres.ica@gmail.com",
        "Urb. Los Pinos 789", "Ica", "Ica", "Peru", 2L, "2021", "2024");

    // Graduadas femeninas
    createGraduate("María", "López Flores", "maria.lopez@gmail.com",
        "F", "2000-11-30", "40456789", "987111004", mockPassword,
        "20201004", "Soltera", "Peruana", "maria.ica@gmail.com",
        "Av. Los Álamos 321", "Ica", "Ica", "Peru", 1L, "2020", "2024");

    createGraduate("Ana", "Castillo Díaz", "ana.castillo@gmail.com",
        "F", "1999-07-14", "40567890", "987111005", mockPassword,
        "20191005", "Soltera", "Peruana", "ana.ica@gmail.com",
        "Jr. Los Rosales 654", "Ica", "Ica", "Peru", 2L, "2019", "2023");

    createGraduate("Sofía", "Vega Paredes", "sofia.vega@gmail.com",
        "F", "2001-02-28", "40678901", "987111006", mockPassword,
        "20211006", "Soltera", "Peruana", "sofia.ica@gmail.com",
        "Calle Los Cedros 987", "Ica", "Ica", "Peru", 1L, "2021", "2024");

    // Empleadores
    createEmployer("Roberto", "Sánchez Lima", "roberto.sanchez@techica.pe",
        "M", "1985-04-20", "41111111", "987222001", mockPassword,
        "20601234567", "TechIca Solutions SAC",
        "Av. Industrial 100, Ica", "Empresa de desarrollo de software en la región Ica");

    createEmployer("Patricia", "Rojas Huanca", "patricia.rojas@constructora.pe",
        "F", "1980-09-12", "41222222", "987222002", mockPassword,
        "20701234568", "Constructora IcaSur SRL",
        "Av. Los Maestros 200, Ica", "Empresa de construcción civil en el sur del país");

    // Encuestas
    seedSurveys();

    log.info("Mock data seeded successfully. Password for all mock users: Egresys2024*");
  }

  private void createGraduate(String nombres, String apellidos, String correo,
      String genero, String fechaNac, String dni, String celular, String password,
      String codigoUniv, String estadoCivil, String nacionalidad, String correoInst,
      String direccion, String ciudad, String departamento, String pais,
      Long escuelaId, String anioIngreso, String anioEgreso) {

    if (userRepository.findByCorreo(correo).isPresent()) return;

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(INSERT_USUARIO, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, nombres);
      ps.setString(2, apellidos);
      ps.setDate(3, java.sql.Date.valueOf(LocalDate.parse(fechaNac)));
      ps.setString(4, genero);
      ps.setString(5, correo);
      ps.setString(6, dni);
      ps.setString(7, celular);
      ps.setString(8, password);
      return ps;
    }, keyHolder);

    long userId = keyHolder.getKey().longValue();

    jdbcTemplate.update(
        "INSERT INTO graduados (usuario_id, codigo_universitario, estado_civil, nacionalidad, correo_institucional, "
        + "direccion_actual, ciudad, departamento, pais_residencia, escuela_profesional_id, anio_ingreso, anio_egreso, validado, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true, NOW(), NOW())",
        userId, codigoUniv, estadoCivil, nacionalidad, correoInst,
        direccion, ciudad, departamento, pais, escuelaId, anioIngreso, anioEgreso);

    log.info("Mock graduate created: {} {}", nombres, apellidos);
  }

  private void createEmployer(String nombres, String apellidos, String correo,
      String genero, String fechaNac, String dni, String celular, String password,
      String ruc, String razonSocial, String direccion, String resumen) {

    if (userRepository.findByCorreo(correo).isPresent()) return;

    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(INSERT_USUARIO, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, nombres);
      ps.setString(2, apellidos);
      ps.setDate(3, java.sql.Date.valueOf(LocalDate.parse(fechaNac)));
      ps.setString(4, genero);
      ps.setString(5, correo);
      ps.setString(6, dni);
      ps.setString(7, celular);
      ps.setString(8, password);
      return ps;
    }, keyHolder);

    long userId = keyHolder.getKey().longValue();

    jdbcTemplate.update(
        "INSERT INTO empleadores (usuario_id, ruc, razon_social, direccion, resumen_empresa, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, ?, ?, ?, NOW(), NOW())",
        userId, ruc, razonSocial, direccion, resumen);

    log.info("Mock employer created: {} {}", nombres, apellidos);
  }

  private void seedSurveys() {
    if (surveyRepository.count() > 0) return;

    surveyTypeRepository.findAll().forEach(type -> {
      if ("EMPLOYMENT".equals(type.getName())) {
        SurveyEntity survey = new SurveyEntity();
        survey.setTitle("Encuesta de Inserción Laboral 2024");
        survey.setDescription("Evalúa la situación laboral de los egresados al primer año de graduarse");
        survey.setSurveyType(type);
        survey.setStatus(SurveyStatus.ACTIVE);
        survey.setStartDate(LocalDate.of(2024, 1, 1));
        survey.setEndDate(LocalDate.of(2024, 12, 31));
        surveyRepository.save(survey);
        log.info("Mock survey created: Encuesta de Inserción Laboral 2024");
      } else if ("ACADEMIC".equals(type.getName())) {
        SurveyEntity survey = new SurveyEntity();
        survey.setTitle("Encuesta de Satisfacción Académica 2024");
        survey.setDescription("Mide la satisfacción de los egresados con su formación universitaria");
        survey.setSurveyType(type);
        survey.setStatus(SurveyStatus.ACTIVE);
        survey.setStartDate(LocalDate.of(2024, 3, 1));
        survey.setEndDate(LocalDate.of(2024, 11, 30));
        surveyRepository.save(survey);
        log.info("Mock survey created: Encuesta de Satisfacción Académica 2024");
      } else if ("SATISFACTION".equals(type.getName())) {
        SurveyEntity survey = new SurveyEntity();
        survey.setTitle("Encuesta de Seguimiento de Egresados 2023");
        survey.setDescription("Seguimiento del desarrollo profesional de los egresados de la promoción 2023");
        survey.setSurveyType(type);
        survey.setStatus(SurveyStatus.CLOSED);
        survey.setStartDate(LocalDate.of(2023, 6, 1));
        survey.setEndDate(LocalDate.of(2023, 12, 31));
        surveyRepository.save(survey);
        log.info("Mock survey created: Encuesta de Seguimiento de Egresados 2023");
      }
    });
  }
}
