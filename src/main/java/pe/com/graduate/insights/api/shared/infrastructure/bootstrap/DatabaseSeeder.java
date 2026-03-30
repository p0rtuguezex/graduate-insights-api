package pe.com.graduate.insights.api.shared.infrastructure.bootstrap;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
        "idiomas_catalogo");

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
    seedUniversitiesAlways();
    ensureAdminDirector();
    seedMockDataIfNeeded();
    seedEducationCentersAlways();
    seedEventsIfNeeded();
    seedJobsIfNeeded();
    seedJobOffersIfNeeded();
    seedSurveyQuestionsIfNeeded();
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

    // universidades se sincronizan en seedUniversitiesAlways()
  }

  private void seedUniversitiesAlways() {
    log.info("Syncing universities catalog...");
    jdbcTemplate.execute(
        """
        INSERT INTO universidades (id, nombre, estado, fecha_creacion, fecha_modificacion)
        VALUES
          (1,  'Universidad Nacional San Luis Gonzaga', '1', NOW(), NOW()),
          (2,  'Universidad Nacional Mayor de San Marcos', '1', NOW(), NOW()),
          (3,  'Universidad Nacional de Ingeniería', '1', NOW(), NOW()),
          (4,  'Universidad Nacional Agraria La Molina', '1', NOW(), NOW()),
          (5,  'Universidad Nacional Federico Villarreal', '1', NOW(), NOW()),
          (6,  'Universidad Nacional de Educación Enrique Guzmán y Valle', '1', NOW(), NOW()),
          (7,  'Universidad Nacional del Callao', '1', NOW(), NOW()),
          (8,  'Universidad Nacional de San Agustín de Arequipa', '1', NOW(), NOW()),
          (9,  'Universidad Nacional de Trujillo', '1', NOW(), NOW()),
          (10, 'Universidad Nacional de Piura', '1', NOW(), NOW()),
          (11, 'Universidad Nacional de la Amazonía Peruana', '1', NOW(), NOW()),
          (12, 'Universidad Nacional San Cristóbal de Huamanga', '1', NOW(), NOW()),
          (13, 'Universidad Nacional del Centro del Perú', '1', NOW(), NOW()),
          (14, 'Universidad Nacional de San Antonio Abad del Cusco', '1', NOW(), NOW()),
          (15, 'Universidad Nacional del Altiplano', '1', NOW(), NOW()),
          (16, 'Universidad Nacional de Cajamarca', '1', NOW(), NOW()),
          (17, 'Universidad Nacional Hermilio Valdizán', '1', NOW(), NOW()),
          (18, 'Universidad Nacional de San Martín', '1', NOW(), NOW()),
          (19, 'Universidad Nacional José Faustino Sánchez Carrión', '1', NOW(), NOW()),
          (20, 'Universidad Nacional Santiago Antúnez de Mayolo', '1', NOW(), NOW()),
          (21, 'Universidad Nacional de Ucayali', '1', NOW(), NOW()),
          (22, 'Universidad Nacional de Huancavelica', '1', NOW(), NOW()),
          (23, 'Universidad Nacional Toribio Rodríguez de Mendoza de Amazonas', '1', NOW(), NOW()),
          (24, 'Universidad Nacional de Moquegua', '1', NOW(), NOW()),
          (25, 'Universidad Nacional de Tumbes', '1', NOW(), NOW()),
          (26, 'Universidad Nacional Intercultural de la Amazonía', '1', NOW(), NOW()),
          (27, 'Universidad Nacional de Barranca', '1', NOW(), NOW()),
          (28, 'Universidad Nacional de Cañete', '1', NOW(), NOW()),
          (29, 'Universidad Nacional de Juliaca', '1', NOW(), NOW()),
          (30, 'Universidad Nacional de Jaén', '1', NOW(), NOW()),
          (31, 'Universidad Nacional Autónoma de Chota', '1', NOW(), NOW()),
          (32, 'Universidad Nacional Intercultural Fabiola Salazar Leguía de Bagua', '1', NOW(), NOW()),
          (33, 'Universidad Nacional de Frontera', '1', NOW(), NOW()),
          (34, 'Universidad Nacional Autónoma de Alto Amazonas', '1', NOW(), NOW()),
          (35, 'Universidad Nacional de Tayacaja Daniel Hernández Morillo', '1', NOW(), NOW()),
          (36, 'Universidad Nacional Amazónica de Madre de Dios', '1', NOW(), NOW()),
          (37, 'Universidad Nacional Autónoma de Huanta', '1', NOW(), NOW()),
          (38, 'Pontificia Universidad Católica del Perú', '1', NOW(), NOW()),
          (39, 'Universidad Peruana Cayetano Heredia', '1', NOW(), NOW()),
          (40, 'Universidad de Lima', '1', NOW(), NOW()),
          (41, 'Universidad del Pacífico', '1', NOW(), NOW()),
          (42, 'Universidad ESAN', '1', NOW(), NOW()),
          (43, 'Universidad San Ignacio de Loyola', '1', NOW(), NOW()),
          (44, 'Universidad Científica del Sur', '1', NOW(), NOW()),
          (45, 'Universidad de San Martín de Porres', '1', NOW(), NOW()),
          (46, 'Universidad Privada del Norte', '1', NOW(), NOW()),
          (47, 'Universidad César Vallejo', '1', NOW(), NOW()),
          (48, 'Universidad Continental', '1', NOW(), NOW()),
          (49, 'Universidad Tecnológica del Perú', '1', NOW(), NOW()),
          (50, 'Universidad Peruana de Ciencias Aplicadas', '1', NOW(), NOW()),
          (51, 'Universidad Peruana Los Andes', '1', NOW(), NOW()),
          (52, 'Universidad Andina del Cusco', '1', NOW(), NOW()),
          (53, 'Universidad Católica de Santa María', '1', NOW(), NOW()),
          (54, 'Universidad Católica San Pablo', '1', NOW(), NOW()),
          (55, 'Universidad Privada San Pedro', '1', NOW(), NOW()),
          (56, 'Universidad Privada Antenor Orrego', '1', NOW(), NOW()),
          (57, 'Universidad Señor de Sipán', '1', NOW(), NOW()),
          (58, 'Universidad de Chiclayo', '1', NOW(), NOW()),
          (59, 'Universidad Alas Peruanas', '1', NOW(), NOW()),
          (60, 'Universidad Autónoma del Perú', '1', NOW(), NOW()),
          (61, 'Universidad Norbert Wiener', '1', NOW(), NOW()),
          (62, 'Universidad Inca Garcilaso de la Vega', '1', NOW(), NOW()),
          (63, 'Universidad Ricardo Palma', '1', NOW(), NOW()),
          (64, 'Universidad Marcelino Champagnat', '1', NOW(), NOW()),
          (65, 'Universidad Femenina del Sagrado Corazón', '1', NOW(), NOW()),
          (66, 'Universidad Antonio Ruiz de Montoya', '1', NOW(), NOW()),
          (67, 'Universidad Le Cordon Bleu', '1', NOW(), NOW()),
          (68, 'Universidad Privada de Huancayo Franklin Roosevelt', '1', NOW(), NOW()),
          (69, 'Universidad Católica Los Ángeles de Chimbote', '1', NOW(), NOW()),
          (70, 'Universidad Privada de Tacna', '1', NOW(), NOW()),
          (71, 'Universidad San Pedro', '1', NOW(), NOW()),
          (72, 'Universidad de Piura', '1', NOW(), NOW()),
          (73, 'Universidad Católica Santo Toribio de Mogrovejo', '1', NOW(), NOW()),
          (74, 'Universidad de Huánuco', '1', NOW(), NOW()),
          (75, 'Universidad Privada de Pucallpa', '1', NOW(), NOW()),
          (76, 'Universidad Peruana Unión', '1', NOW(), NOW()),
          (77, 'Universidad Adventista de la Selva', '1', NOW(), NOW()),
          (78, 'Universidad Peruana del Oriente', '1', NOW(), NOW()),
          (79, 'Universidad Tecnológica de los Andes', '1', NOW(), NOW()),
          (80, 'Universidad Andina Néstor Cáceres Velásquez', '1', NOW(), NOW()),
          (81, 'Universidad de Ciencias y Humanidades', '1', NOW(), NOW()),
          (82, 'Universidad Privada San Carlos', '1', NOW(), NOW()),
          (83, 'Universidad María Auxiliadora', '1', NOW(), NOW()),
          (84, 'Universidad de Lambayeque', '1', NOW(), NOW()),
          (85, 'Universidad Privada Sergio Bernales', '1', NOW(), NOW()),
          (86, 'Universidad Privada Leonardo Da Vinci', '1', NOW(), NOW()),
          (87, 'Universidad Peruana de Arte Orval', '1', NOW(), NOW()),
          (88, 'Universidad Privada Juan Pablo II', '1', NOW(), NOW()),
          (89, 'Universidad para el Desarrollo Andino', '1', NOW(), NOW()),
          (90, 'Universidad Privada de Ica', '1', NOW(), NOW()),
          (91, 'Universidad Privada Telesup', '1', NOW(), NOW()),
          (92, 'Universidad de Las Américas', '1', NOW(), NOW()),
          (93, 'Universidad Global del Cusco', '1', NOW(), NOW()),
          (94, 'Universidad Interamericana para el Desarrollo', '1', NOW(), NOW()),
          (95, 'Universidad Privada del Cusco', '1', NOW(), NOW())
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

  // ============================================================
  // CENTROS EDUCATIVOS
  // ============================================================

  private void seedEducationCentersAlways() {
    log.info("Syncing education centers...");
    jdbcTemplate.execute(
        """
        INSERT IGNORE INTO centros_educativos (nombre, direccion, estado, fecha_creacion, fecha_modificacion)
        VALUES
          ('Universidad Nacional San Luis Gonzaga', 'Ica', '1', NOW(), NOW()),
          ('Universidad Nacional Mayor de San Marcos', 'Lima', '1', NOW(), NOW()),
          ('Universidad Nacional de Ingeniería', 'Lima', '1', NOW(), NOW()),
          ('Universidad Nacional Agraria La Molina', 'Lima', '1', NOW(), NOW()),
          ('Universidad Nacional Federico Villarreal', 'Lima', '1', NOW(), NOW()),
          ('Universidad Nacional de Educación Enrique Guzmán y Valle', 'Lima', '1', NOW(), NOW()),
          ('Universidad Nacional del Callao', 'Callao', '1', NOW(), NOW()),
          ('Universidad Nacional de San Agustín de Arequipa', 'Arequipa', '1', NOW(), NOW()),
          ('Universidad Nacional de Trujillo', 'La Libertad', '1', NOW(), NOW()),
          ('Universidad Nacional de Piura', 'Piura', '1', NOW(), NOW()),
          ('Universidad Nacional de la Amazonía Peruana', 'Loreto', '1', NOW(), NOW()),
          ('Universidad Nacional San Cristóbal de Huamanga', 'Ayacucho', '1', NOW(), NOW()),
          ('Universidad Nacional del Centro del Perú', 'Junín', '1', NOW(), NOW()),
          ('Universidad Nacional de San Antonio Abad del Cusco', 'Cusco', '1', NOW(), NOW()),
          ('Universidad Nacional del Altiplano', 'Puno', '1', NOW(), NOW()),
          ('Universidad Nacional de Cajamarca', 'Cajamarca', '1', NOW(), NOW()),
          ('Universidad Nacional Hermilio Valdizán', 'Huánuco', '1', NOW(), NOW()),
          ('Universidad Nacional de San Martín', 'San Martín', '1', NOW(), NOW()),
          ('Universidad Nacional José Faustino Sánchez Carrión', 'Lima', '1', NOW(), NOW()),
          ('Universidad Nacional Santiago Antúnez de Mayolo', 'Ancash', '1', NOW(), NOW()),
          ('Universidad Nacional de Ucayali', 'Ucayali', '1', NOW(), NOW()),
          ('Universidad Nacional de Huancavelica', 'Huancavelica', '1', NOW(), NOW()),
          ('Universidad Nacional Toribio Rodríguez de Mendoza de Amazonas', 'Amazonas', '1', NOW(), NOW()),
          ('Universidad Nacional de Moquegua', 'Moquegua', '1', NOW(), NOW()),
          ('Universidad Nacional de Tumbes', 'Tumbes', '1', NOW(), NOW()),
          ('Universidad Nacional Intercultural de la Amazonía', 'Ucayali', '1', NOW(), NOW()),
          ('Universidad Nacional de Barranca', 'Lima', '1', NOW(), NOW()),
          ('Universidad Nacional de Cañete', 'Lima', '1', NOW(), NOW()),
          ('Universidad Nacional de Juliaca', 'Puno', '1', NOW(), NOW()),
          ('Universidad Nacional de Jaén', 'Cajamarca', '1', NOW(), NOW()),
          ('Universidad Nacional Autónoma de Chota', 'Cajamarca', '1', NOW(), NOW()),
          ('Universidad Nacional Intercultural Fabiola Salazar Leguía de Bagua', 'Amazonas', '1', NOW(), NOW()),
          ('Universidad Nacional de Frontera', 'Loreto', '1', NOW(), NOW()),
          ('Universidad Nacional Autónoma de Alto Amazonas', 'Loreto', '1', NOW(), NOW()),
          ('Universidad Nacional de Tayacaja Daniel Hernández Morillo', 'Huancavelica', '1', NOW(), NOW()),
          ('Universidad Nacional Amazónica de Madre de Dios', 'Madre de Dios', '1', NOW(), NOW()),
          ('Universidad Nacional Autónoma de Huanta', 'Ayacucho', '1', NOW(), NOW()),
          ('Pontificia Universidad Católica del Perú', 'Lima', '1', NOW(), NOW()),
          ('Universidad Peruana Cayetano Heredia', 'Lima', '1', NOW(), NOW()),
          ('Universidad de Lima', 'Lima', '1', NOW(), NOW()),
          ('Universidad del Pacífico', 'Lima', '1', NOW(), NOW()),
          ('Universidad ESAN', 'Lima', '1', NOW(), NOW()),
          ('Universidad San Ignacio de Loyola', 'Lima', '1', NOW(), NOW()),
          ('Universidad Científica del Sur', 'Lima', '1', NOW(), NOW()),
          ('Universidad de San Martín de Porres', 'Lima', '1', NOW(), NOW()),
          ('Universidad Privada del Norte', 'La Libertad', '1', NOW(), NOW()),
          ('Universidad César Vallejo', 'La Libertad', '1', NOW(), NOW()),
          ('Universidad Continental', 'Junín', '1', NOW(), NOW()),
          ('Universidad Tecnológica del Perú', 'Lima', '1', NOW(), NOW()),
          ('Universidad Peruana de Ciencias Aplicadas', 'Lima', '1', NOW(), NOW()),
          ('Universidad Peruana Los Andes', 'Junín', '1', NOW(), NOW()),
          ('Universidad Andina del Cusco', 'Cusco', '1', NOW(), NOW()),
          ('Universidad Católica de Santa María', 'Arequipa', '1', NOW(), NOW()),
          ('Universidad Católica San Pablo', 'Arequipa', '1', NOW(), NOW()),
          ('Universidad Privada San Pedro', 'Ancash', '1', NOW(), NOW()),
          ('Universidad Privada Antenor Orrego', 'La Libertad', '1', NOW(), NOW()),
          ('Universidad Señor de Sipán', 'Lambayeque', '1', NOW(), NOW()),
          ('Universidad de Chiclayo', 'Lambayeque', '1', NOW(), NOW()),
          ('Universidad Alas Peruanas', 'Lima', '1', NOW(), NOW()),
          ('Universidad Autónoma del Perú', 'Lima', '1', NOW(), NOW()),
          ('Universidad Norbert Wiener', 'Lima', '1', NOW(), NOW()),
          ('Universidad Inca Garcilaso de la Vega', 'Lima', '1', NOW(), NOW()),
          ('Universidad Ricardo Palma', 'Lima', '1', NOW(), NOW()),
          ('Universidad Marcelino Champagnat', 'Lima', '1', NOW(), NOW()),
          ('Universidad Femenina del Sagrado Corazón', 'Lima', '1', NOW(), NOW()),
          ('Universidad Antonio Ruiz de Montoya', 'Lima', '1', NOW(), NOW()),
          ('Universidad Le Cordon Bleu', 'Lima', '1', NOW(), NOW()),
          ('Universidad Privada de Huancayo Franklin Roosevelt', 'Junín', '1', NOW(), NOW()),
          ('Universidad Católica Los Ángeles de Chimbote', 'Ancash', '1', NOW(), NOW()),
          ('Universidad Privada de Tacna', 'Tacna', '1', NOW(), NOW()),
          ('Universidad San Pedro', 'Ancash', '1', NOW(), NOW()),
          ('Universidad de Piura', 'Piura', '1', NOW(), NOW()),
          ('Universidad Católica Santo Toribio de Mogrovejo', 'Lambayeque', '1', NOW(), NOW()),
          ('Universidad de Huánuco', 'Huánuco', '1', NOW(), NOW()),
          ('Universidad Privada de Pucallpa', 'Ucayali', '1', NOW(), NOW()),
          ('Universidad Peruana Unión', 'Lima', '1', NOW(), NOW()),
          ('Universidad Adventista de la Selva', 'San Martín', '1', NOW(), NOW()),
          ('Universidad Peruana del Oriente', 'Loreto', '1', NOW(), NOW()),
          ('Universidad Tecnológica de los Andes', 'Apurímac', '1', NOW(), NOW()),
          ('Universidad Andina Néstor Cáceres Velásquez', 'Puno', '1', NOW(), NOW()),
          ('Universidad de Ciencias y Humanidades', 'Lima', '1', NOW(), NOW()),
          ('Universidad Privada San Carlos', 'Puno', '1', NOW(), NOW()),
          ('Universidad María Auxiliadora', 'Lima', '1', NOW(), NOW()),
          ('Universidad de Lambayeque', 'Lambayeque', '1', NOW(), NOW()),
          ('Universidad Privada Sergio Bernales', 'Lima', '1', NOW(), NOW()),
          ('Universidad Privada Leonardo Da Vinci', 'La Libertad', '1', NOW(), NOW()),
          ('Universidad Peruana de Arte Orval', 'Lima', '1', NOW(), NOW()),
          ('Universidad Privada Juan Pablo II', 'Lima', '1', NOW(), NOW()),
          ('Universidad para el Desarrollo Andino', 'Huancavelica', '1', NOW(), NOW()),
          ('Universidad Privada de Ica', 'Ica', '1', NOW(), NOW()),
          ('Universidad Privada Telesup', 'Lima', '1', NOW(), NOW()),
          ('Universidad de Las Américas', 'Lima', '1', NOW(), NOW()),
          ('Universidad Global del Cusco', 'Cusco', '1', NOW(), NOW()),
          ('Universidad Interamericana para el Desarrollo', 'Lima', '1', NOW(), NOW()),
          ('Universidad Privada del Cusco', 'Cusco', '1', NOW(), NOW()),
          ('Instituto Superior Tecnológico Público de Ica', 'Ica', '1', NOW(), NOW()),
          ('CEBA José Carlos Mariátegui', 'Ica', '1', NOW(), NOW()),
          ('Instituto Superior de Educación Público de Palpa', 'Ica', '1', NOW(), NOW()),
          ('Universidad Alas Peruanas - Filial Ica', 'Ica', '1', NOW(), NOW())
        """);
    log.info("Education centers synced successfully.");
  }

  // ============================================================
  // EVENTOS
  // ============================================================

  private void seedEventsIfNeeded() {
    Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM eventos", Long.class);
    if (count != null && count > 0) {
      log.info("Events already present. Skipping.");
      return;
    }

    Long directorId = null;
    try {
      directorId = jdbcTemplate.queryForObject(
          "SELECT d.id FROM directores d JOIN usuarios u ON d.usuario_id = u.id WHERE u.estado = '1' LIMIT 1",
          Long.class);
    } catch (DataAccessException e) {
      log.warn("No director found to seed events. Skipping events.");
      return;
    }
    if (directorId == null) return;

    List<Map<String, Object>> eventTypes = jdbcTemplate.queryForList(
        "SELECT id, nombre FROM tipos_evento WHERE estado = '1'");
    if (eventTypes.isEmpty()) return;

    long feriaTipoId = (Long) eventTypes.stream()
        .filter(t -> "Feria de Empleo".equals(t.get("nombre"))).findFirst()
        .map(t -> t.get("id")).orElse(eventTypes.get(0).get("id"));
    long conferenciaTipoId = (Long) eventTypes.stream()
        .filter(t -> "Conferencia Tecnológica".equals(t.get("nombre"))).findFirst()
        .map(t -> t.get("id")).orElse(eventTypes.get(0).get("id"));
    long workshopTipoId = (Long) eventTypes.stream()
        .filter(t -> "Workshop de Emprendimiento".equals(t.get("nombre"))).findFirst()
        .map(t -> t.get("id")).orElse(eventTypes.get(0).get("id"));
    long seminarioTipoId = (Long) eventTypes.stream()
        .filter(t -> "Seminario de Liderazgo".equals(t.get("nombre"))).findFirst()
        .map(t -> t.get("id")).orElse(eventTypes.get(0).get("id"));

    log.info("Seeding mock events...");

    jdbcTemplate.update(
        "INSERT INTO eventos (nombre, contenido, estado, fecha_evento, enlace_inscripcion, director_id, tipo_evento_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, '1', ?, ?, ?, ?, NOW(), NOW())",
        "Feria de Empleo Ica 2024",
        "Gran feria de empleo dirigida a egresados universitarios de la región Ica. Más de 30 empresas participantes de los sectores de tecnología, construcción, agroindustria y servicios.",
        java.sql.Date.valueOf(LocalDate.of(2024, 8, 15)),
        "https://egresys.ica.edu.pe/eventos/feria-empleo-2024",
        directorId, feriaTipoId);

    jdbcTemplate.update(
        "INSERT INTO eventos (nombre, contenido, estado, fecha_evento, enlace_inscripcion, director_id, tipo_evento_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, '1', ?, ?, ?, ?, NOW(), NOW())",
        "Conferencia de Innovación Tecnológica 2024",
        "Conferencia magistral sobre tendencias tecnológicas: Inteligencia Artificial, Cloud Computing y Ciberseguridad. Ponentes nacionales e internacionales del sector tecnológico.",
        java.sql.Date.valueOf(LocalDate.of(2024, 9, 20)),
        "https://egresys.ica.edu.pe/eventos/conferencia-tech-2024",
        directorId, conferenciaTipoId);

    jdbcTemplate.update(
        "INSERT INTO eventos (nombre, contenido, estado, fecha_evento, enlace_inscripcion, director_id, tipo_evento_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, '1', ?, ?, ?, ?, NOW(), NOW())",
        "Workshop: Emprendimiento y Startups",
        "Taller práctico de emprendimiento para egresados que desean iniciar su propio negocio. Incluye mentoría, plan de negocios y acceso a red de inversionistas ángeles de la región.",
        java.sql.Date.valueOf(LocalDate.of(2024, 10, 5)),
        "https://egresys.ica.edu.pe/eventos/workshop-emprendimiento",
        directorId, workshopTipoId);

    jdbcTemplate.update(
        "INSERT INTO eventos (nombre, contenido, estado, fecha_evento, enlace_inscripcion, director_id, tipo_evento_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, '1', ?, ?, ?, ?, NOW(), NOW())",
        "Seminario de Liderazgo Profesional",
        "Seminario orientado al desarrollo de habilidades de liderazgo, comunicación efectiva y gestión de equipos para profesionales jóvenes.",
        java.sql.Date.valueOf(LocalDate.of(2024, 11, 10)),
        "https://egresys.ica.edu.pe/eventos/seminario-liderazgo",
        directorId, seminarioTipoId);

    jdbcTemplate.update(
        "INSERT INTO eventos (nombre, contenido, estado, fecha_evento, enlace_inscripcion, director_id, tipo_evento_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, '1', ?, ?, ?, ?, NOW(), NOW())",
        "Feria de Empleo Ica 2025",
        "Segunda edición de la feria de empleo más importante de la región. Oportunidades laborales en más de 40 empresas del sector público y privado.",
        java.sql.Date.valueOf(LocalDate.of(2025, 4, 22)),
        "https://egresys.ica.edu.pe/eventos/feria-empleo-2025",
        directorId, feriaTipoId);

    log.info("Events seeded successfully.");
  }

  // ============================================================
  // TRABAJOS DE EGRESADOS
  // ============================================================

  private void seedJobsIfNeeded() {
    Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM trabajos", Long.class);
    if (count != null && count > 0) {
      log.info("Jobs already present. Skipping.");
      return;
    }

    List<Map<String, Object>> graduates = jdbcTemplate.queryForList(
        "SELECT g.id, u.nombres FROM graduados g JOIN usuarios u ON g.usuario_id = u.id WHERE u.estado = '1' ORDER BY g.id ASC LIMIT 6");
    if (graduates.isEmpty()) return;

    log.info("Seeding mock graduate jobs...");

    if (graduates.size() >= 1) {
      Long gId = toLong(graduates.get(0).get("id"));
      // Carlos Mendoza — trabaja en tecnología, trabajo actual
      jdbcTemplate.update(
          "INSERT INTO trabajos (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduado_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, null, ?, NOW(), NOW())",
          "TechIca Solutions SAC", "Desarrollador de Software Junior", "Híbrido",
          java.sql.Date.valueOf(LocalDate.of(2024, 3, 1)), gId);
    }

    if (graduates.size() >= 2) {
      Long gId = toLong(graduates.get(1).get("id"));
      // Luis García — trabajo anterior y trabajo actual
      jdbcTemplate.update(
          "INSERT INTO trabajos (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduado_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, ?, ?, NOW(), NOW())",
          "Municipalidad Provincial de Ica", "Practicante de TI", "Presencial",
          java.sql.Date.valueOf(LocalDate.of(2023, 1, 1)),
          java.sql.Date.valueOf(LocalDate.of(2023, 12, 31)), gId);
      jdbcTemplate.update(
          "INSERT INTO trabajos (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduado_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, null, ?, NOW(), NOW())",
          "Municipalidad Provincial de Ica", "Analista de Sistemas", "Presencial",
          java.sql.Date.valueOf(LocalDate.of(2024, 1, 15)), gId);
    }

    if (graduates.size() >= 3) {
      Long gId = toLong(graduates.get(2).get("id"));
      // Andrés Quispe — freelance
      jdbcTemplate.update(
          "INSERT INTO trabajos (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduado_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, null, ?, NOW(), NOW())",
          "Independiente", "Desarrollador Web Freelance", "Remoto",
          java.sql.Date.valueOf(LocalDate.of(2024, 6, 1)), gId);
    }

    if (graduates.size() >= 4) {
      Long gId = toLong(graduates.get(3).get("id"));
      // María López — trabajo en empresa privada
      jdbcTemplate.update(
          "INSERT INTO trabajos (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduado_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, null, ?, NOW(), NOW())",
          "Inversiones IcaCorp SAC", "Ingeniera de Proyectos TI", "Presencial",
          java.sql.Date.valueOf(LocalDate.of(2024, 2, 1)), gId);
    }

    if (graduates.size() >= 5) {
      Long gId = toLong(graduates.get(4).get("id"));
      // Ana Castillo — trabajo anterior y actual
      jdbcTemplate.update(
          "INSERT INTO trabajos (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduado_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, ?, ?, NOW(), NOW())",
          "SUNAT Oficina Ica", "Practicante Profesional", "Presencial",
          java.sql.Date.valueOf(LocalDate.of(2023, 4, 1)),
          java.sql.Date.valueOf(LocalDate.of(2023, 12, 31)), gId);
      jdbcTemplate.update(
          "INSERT INTO trabajos (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduado_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, null, ?, NOW(), NOW())",
          "SUNAT Oficina Ica", "Especialista en Sistemas", "Presencial",
          java.sql.Date.valueOf(LocalDate.of(2024, 1, 10)), gId);
    }

    log.info("Graduate jobs seeded successfully.");
  }

  // ============================================================
  // OFERTAS LABORALES
  // ============================================================

  private void seedJobOffersIfNeeded() {
    Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ofertas_laborales", Long.class);
    if (count != null && count > 0) {
      log.info("Job offers already present. Skipping.");
      return;
    }

    List<Map<String, Object>> employers = jdbcTemplate.queryForList(
        "SELECT e.id, e.razon_social FROM empleadores e ORDER BY e.id ASC LIMIT 2");
    if (employers.isEmpty()) return;

    log.info("Seeding mock job offers...");

    Long emp1Id = toLong(employers.get(0).get("id")); // TechIca Solutions SAC

    jdbcTemplate.update(
        "INSERT INTO ofertas_laborales (titulo, descripcion, link, estado, empleador_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, ?, '1', ?, NOW(), NOW())",
        "Desarrollador Full Stack (Java + Vue.js)",
        "Buscamos desarrollador Full Stack con experiencia en Java Spring Boot y Vue.js. "
        + "Requisitos: egresado de Ing. de Sistemas, conocimiento de bases de datos relacionales, "
        + "trabajo en equipo ágil. Beneficios: seguro médico, horario flexible, trabajo híbrido. Salario: S/ 3,500 - S/ 5,000.",
        "https://techica.pe/empleos/dev-fullstack",
        emp1Id);

    jdbcTemplate.update(
        "INSERT INTO ofertas_laborales (titulo, descripcion, link, estado, empleador_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, ?, '1', ?, NOW(), NOW())",
        "Analista de Datos e Inteligencia de Negocios",
        "Se requiere analista de datos con conocimientos en SQL, Power BI o Tableau. "
        + "Funciones: diseño de dashboards, análisis de KPIs y generación de reportes para la dirección. "
        + "Modalidad remota. Salario: S/ 2,800 - S/ 3,800.",
        "https://techica.pe/empleos/analista-datos",
        emp1Id);

    jdbcTemplate.update(
        "INSERT INTO ofertas_laborales (titulo, descripcion, link, estado, empleador_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, ?, '1', ?, NOW(), NOW())",
        "DevOps / Administrador de Servidores Linux",
        "Perfil DevOps para gestión de infraestructura en la nube (AWS/Azure), CI/CD pipelines y monitoreo de sistemas. "
        + "Experiencia mínima 1 año. Salario: S/ 4,000 - S/ 6,000.",
        "https://techica.pe/empleos/devops",
        emp1Id);

    if (employers.size() >= 2) {
      Long emp2Id = toLong(employers.get(1).get("id")); // Constructora IcaSur SRL

      jdbcTemplate.update(
          "INSERT INTO ofertas_laborales (titulo, descripcion, link, estado, empleador_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, NOW(), NOW())",
          "Ingeniero Residente de Obra",
          "Se solicita ingeniero civil para supervisión de obras de infraestructura en la región sur. "
          + "Requisitos: título profesional en Ing. Civil, experiencia en proyectos de construcción, "
          + "disponibilidad para trabajar en campo. Salario: S/ 4,500 + viáticos.",
          "https://constructoraicasur.pe/empleos/ing-residente",
          emp2Id);

      jdbcTemplate.update(
          "INSERT INTO ofertas_laborales (titulo, descripcion, link, estado, empleador_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, '1', ?, NOW(), NOW())",
          "Asistente de Ingeniería y Presupuestos",
          "Asistente para elaboración de presupuestos, metrados y expedientes técnicos. "
          + "Conocimientos en AutoCAD y S10 indispensables. Recién egresados con ganas de aprender son bienvenidos. "
          + "Salario: S/ 1,800 - S/ 2,500.",
          "https://constructoraicasur.pe/empleos/asistente-ing",
          emp2Id);
    }

    log.info("Job offers seeded successfully.");
  }

  // ============================================================
  // PREGUNTAS DE ENCUESTAS
  // ============================================================

  private void seedSurveyQuestionsIfNeeded() {
    Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM preguntas", Long.class);
    if (count != null && count > 0) {
      log.info("Survey questions already present. Skipping.");
      return;
    }

    List<Map<String, Object>> surveys = jdbcTemplate.queryForList(
        "SELECT id, titulo FROM encuestas ORDER BY id ASC");
    if (surveys.isEmpty()) return;

    log.info("Seeding mock survey questions...");

    for (Map<String, Object> survey : surveys) {
      Long surveyId = toLong(survey.get("id"));
      String titulo = (String) survey.get("titulo");

      if (titulo != null && titulo.contains("Inserción Laboral")) {
        seedEmploymentSurveyQuestions(surveyId);
      } else if (titulo != null && titulo.contains("Satisfacción Académica")) {
        seedAcademicSurveyQuestions(surveyId);
      } else if (titulo != null && titulo.contains("Seguimiento")) {
        seedFollowUpSurveyQuestions(surveyId);
      }
    }

    log.info("Survey questions seeded successfully.");
  }

  private void seedEmploymentSurveyQuestions(Long surveyId) {
    long q1 = insertQuestion(surveyId, "¿Actualmente se encuentra trabajando?", "YES_NO", true);

    long q2 = insertQuestion(surveyId, "¿En qué sector labora actualmente?", "SINGLE_CHOICE", true);
    insertOption(q2, "Sector público", 1);
    insertOption(q2, "Sector privado", 2);
    insertOption(q2, "Trabajo independiente / freelance", 3);
    insertOption(q2, "No me encuentro trabajando", 4);

    long q3 = insertQuestion(surveyId, "¿Su trabajo está relacionado con su carrera profesional?", "SINGLE_CHOICE", true);
    insertOption(q3, "Sí, totalmente relacionado", 1);
    insertOption(q3, "Sí, parcialmente relacionado", 2);
    insertOption(q3, "No, es de un área diferente", 3);

    long q4 = insertQuestion(surveyId, "¿Cuánto tiempo le tomó conseguir su primer empleo tras graduarse?", "SINGLE_CHOICE", true);
    insertOption(q4, "Menos de 3 meses", 1);
    insertOption(q4, "Entre 3 y 6 meses", 2);
    insertOption(q4, "Entre 6 y 12 meses", 3);
    insertOption(q4, "Más de 1 año", 4);
    insertOption(q4, "Aún no he trabajado", 5);

    long q5 = insertQuestion(surveyId, "¿Cuál es su modalidad de trabajo actual?", "SINGLE_CHOICE", false);
    insertOption(q5, "Presencial", 1);
    insertOption(q5, "Remoto", 2);
    insertOption(q5, "Híbrido", 3);
    insertOption(q5, "No aplica", 4);

    long q6 = insertQuestion(surveyId, "¿Cuál es su rango salarial mensual aproximado?", "SINGLE_CHOICE", false);
    insertOption(q6, "Menos de S/ 1,000", 1);
    insertOption(q6, "S/ 1,000 – S/ 2,000", 2);
    insertOption(q6, "S/ 2,001 – S/ 3,500", 3);
    insertOption(q6, "S/ 3,501 – S/ 5,000", 4);
    insertOption(q6, "Más de S/ 5,000", 5);
    insertOption(q6, "Prefiero no indicarlo", 6);

    insertQuestion(surveyId, "Describa brevemente sus funciones o actividades laborales actuales", "TEXT", false);
  }

  private void seedAcademicSurveyQuestions(Long surveyId) {
    insertQuestion(surveyId,
        "En general, ¿cuál es su nivel de satisfacción con la formación académica recibida? (1 = Muy insatisfecho, 5 = Muy satisfecho)",
        "SCALE", true);

    long q2 = insertQuestion(surveyId, "¿La malla curricular fue relevante para el mercado laboral?", "SINGLE_CHOICE", true);
    insertOption(q2, "Muy relevante", 1);
    insertOption(q2, "Relevante", 2);
    insertOption(q2, "Poco relevante", 3);
    insertOption(q2, "Nada relevante", 4);

    insertQuestion(surveyId,
        "¿Los docentes demostraron dominio y actualización en sus especialidades? (1 = Muy en desacuerdo, 5 = Muy de acuerdo)",
        "SCALE", true);

    long q4 = insertQuestion(surveyId, "¿La infraestructura y equipamiento de la universidad fue adecuada?", "SINGLE_CHOICE", true);
    insertOption(q4, "Excelente", 1);
    insertOption(q4, "Buena", 2);
    insertOption(q4, "Regular", 3);
    insertOption(q4, "Deficiente", 4);

    long q5 = insertQuestion(surveyId, "¿Recomendaría esta carrera a otras personas?", "YES_NO", true);

    insertQuestion(surveyId, "¿Qué aspecto mejoraría de la carrera o de la universidad?", "TEXT", false);
  }

  private void seedFollowUpSurveyQuestions(Long surveyId) {
    long q1 = insertQuestion(surveyId, "¿Continúa residiendo en la región Ica?", "YES_NO", true);

    long q2 = insertQuestion(surveyId, "¿Ha iniciado o completado estudios de posgrado?", "SINGLE_CHOICE", true);
    insertOption(q2, "Sí, actualmente estoy cursando una maestría", 1);
    insertOption(q2, "Sí, ya completé estudios de posgrado", 2);
    insertOption(q2, "No, pero planeo hacerlo pronto", 3);
    insertOption(q2, "No, y no tengo planes por ahora", 4);

    long q3 = insertQuestion(surveyId, "¿Participa en actividades de extensión o redes de egresados de la universidad?", "YES_NO", false);

    insertQuestion(surveyId,
        "En general, ¿cómo evaluaría su desarrollo profesional desde que se graduó? (1 = Muy insatisfecho, 5 = Muy satisfecho)",
        "SCALE", true);

    long q5 = insertQuestion(surveyId, "¿Qué tipo de apoyo considera que la universidad debería brindar a sus egresados?", "MULTIPLE_CHOICE", false);
    insertOption(q5, "Bolsa de trabajo", 1);
    insertOption(q5, "Cursos de actualización", 2);
    insertOption(q5, "Certificaciones profesionales", 3);
    insertOption(q5, "Red de contactos (networking)", 4);
    insertOption(q5, "Asesoría para emprendimiento", 5);
  }

  // ============================================================
  // HELPERS
  // ============================================================

  private long insertQuestion(Long surveyId, String text, String type, boolean required) {
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO preguntas (texto_pregunta, tipo_pregunta, requerida, encuesta_id, fecha_creacion, fecha_modificacion) "
          + "VALUES (?, ?, ?, ?, NOW(), NOW())",
          Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, text);
      ps.setString(2, type);
      ps.setBoolean(3, required);
      ps.setLong(4, surveyId);
      return ps;
    }, kh);
    return kh.getKey().longValue();
  }

  private void insertOption(Long questionId, String text, int order) {
    jdbcTemplate.update(
        "INSERT INTO opciones_pregunta (texto_opcion, numero_orden, pregunta_id, fecha_creacion, fecha_modificacion) "
        + "VALUES (?, ?, ?, NOW(), NOW())",
        text, order, questionId);
  }

  private Long toLong(Object value) {
    if (value instanceof Long l) return l;
    if (value instanceof Integer i) return i.longValue();
    if (value instanceof Number n) return n.longValue();
    return null;
  }
}
