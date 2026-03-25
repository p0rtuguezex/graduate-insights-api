SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    fecha_nacimiento DATE,
    genero VARCHAR(20),
    correo VARCHAR(100),
    estado CHAR(1),
    dni VARCHAR(20),
    celular VARCHAR(20),
    contrasena VARCHAR(100),
    verificado BOOLEAN DEFAULT FALSE,
    codigo_confirmacion CHAR(6) DEFAULT NULL,
    codigo_recuperacion CHAR(6) DEFAULT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS facultades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT '1',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_facultades_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS escuelas_profesionales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    facultad_id BIGINT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT '1',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_escuelas_facultad_nombre (facultad_id, nombre),
    FOREIGN KEY (facultad_id) REFERENCES facultades(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tipos_grado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(40) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT '1',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_tipos_grado_codigo (codigo),
    UNIQUE KEY uk_tipos_grado_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS modalidades_titulacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(40) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT '1',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_modalidades_titulacion_codigo (codigo),
    UNIQUE KEY uk_modalidades_titulacion_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS idiomas_catalogo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT '1',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_idiomas_catalogo_codigo (codigo),
    UNIQUE KEY uk_idiomas_catalogo_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS universidades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(180) NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT '1',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_universidades_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS graduados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT UNIQUE,
    codigo_universitario VARCHAR(20),
    estado_civil VARCHAR(30),
    nacionalidad VARCHAR(60),
    correo_institucional VARCHAR(100),
    direccion_actual VARCHAR(150),
    ciudad VARCHAR(80),
    departamento VARCHAR(80),
    pais_residencia VARCHAR(80),
    linkedin VARCHAR(255),
    portafolio VARCHAR(255),
    escuela_profesional_id BIGINT,
    anio_ingreso VARCHAR(4),
    anio_egreso VARCHAR(4),
    ruta_cv VARCHAR(500),
    validado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY(escuela_profesional_id) REFERENCES escuelas_profesionales(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'codigo_universitario');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN codigo_universitario VARCHAR(20)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'estado_civil');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN estado_civil VARCHAR(30)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'nacionalidad');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN nacionalidad VARCHAR(60)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'correo_institucional');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN correo_institucional VARCHAR(100)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'direccion_actual');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN direccion_actual VARCHAR(150)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'ciudad');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN ciudad VARCHAR(80)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'departamento');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN departamento VARCHAR(80)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'pais_residencia');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN pais_residencia VARCHAR(80)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'linkedin');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN linkedin VARCHAR(255)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'portafolio');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN portafolio VARCHAR(255)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

        SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'escuela_profesional_id');
        SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN escuela_profesional_id BIGINT', 'SELECT 1');
        PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

        SET @fk_exists = (
            SELECT COUNT(*)
            FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
            WHERE TABLE_SCHEMA = DATABASE()
                AND TABLE_NAME = 'graduados'
                AND CONSTRAINT_NAME = 'fk_graduados_escuela_profesional'
                AND CONSTRAINT_TYPE = 'FOREIGN KEY'
        );
        SET @ddl = IF(
            @fk_exists = 0,
            'ALTER TABLE graduados ADD CONSTRAINT fk_graduados_escuela_profesional FOREIGN KEY (escuela_profesional_id) REFERENCES escuelas_profesionales(id) ON DELETE SET NULL ON UPDATE CASCADE',
            'SELECT 1'
        );
        PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'anio_ingreso');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN anio_ingreso VARCHAR(4)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'anio_egreso');
    SET @ddl = IF(@col_exists = 0, 'ALTER TABLE graduados ADD COLUMN anio_egreso VARCHAR(4)', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    -- Drop legacy flat columns (data now lives in normalized child tables)
    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'facultad');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN facultad', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'escuela_profesional');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN escuela_profesional', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'grado_obtenido');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN grado_obtenido', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'bachiller_fecha');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN bachiller_fecha', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'bachiller_universidad');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN bachiller_universidad', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'titulo_profesional_fecha');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN titulo_profesional_fecha', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'titulo_profesional_universidad');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN titulo_profesional_universidad', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'maestria_fecha');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN maestria_fecha', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'maestria_universidad');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN maestria_universidad', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'doctorado_fecha');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN doctorado_fecha', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'doctorado_universidad');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN doctorado_universidad', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'otro_grado_nombre');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN otro_grado_nombre', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'otro_grado_fecha');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN otro_grado_fecha', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'otro_grado_universidad');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN otro_grado_universidad', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'modalidad_titulacion');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN modalidad_titulacion', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'modalidad_titulacion_otro');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN modalidad_titulacion_otro', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'idioma_nombre');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN idioma_nombre', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'idioma_nivel');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN idioma_nivel', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'idioma_fecha_inicio');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN idioma_fecha_inicio', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'idioma_fecha_fin');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN idioma_fecha_fin', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'idioma_aprendizaje');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN idioma_aprendizaje', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'fecha_inicio');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN fecha_inicio', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'fecha_fin');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN fecha_fin', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

    SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'graduados' AND COLUMN_NAME = 'cv');
    SET @ddl = IF(@col_exists > 0, 'ALTER TABLE graduados DROP COLUMN cv', 'SELECT 1');
    PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS graduado_grados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    graduado_id BIGINT NOT NULL,
    tipo_grado_id BIGINT NOT NULL,
    universidad_id BIGINT,
    fecha_grado DATE,
    otro_grado_nombre VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (graduado_id) REFERENCES graduados(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (tipo_grado_id) REFERENCES tipos_grado(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    FOREIGN KEY (universidad_id) REFERENCES universidades(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    UNIQUE KEY uk_graduado_grados_semantic (graduado_id, tipo_grado_id, universidad_id, fecha_grado, otro_grado_nombre)
);

CREATE TABLE IF NOT EXISTS graduado_titulaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    graduado_grado_id BIGINT NOT NULL,
    modalidad_titulacion_id BIGINT NOT NULL,
    modalidad_otro VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (graduado_grado_id) REFERENCES graduado_grados(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (modalidad_titulacion_id) REFERENCES modalidades_titulacion(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    UNIQUE KEY uk_graduado_titulaciones_grado (graduado_grado_id)
);

CREATE TABLE IF NOT EXISTS graduado_idiomas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    graduado_id BIGINT NOT NULL,
    idioma_id BIGINT NOT NULL,
    nivel VARCHAR(30) NOT NULL,
    fecha_inicio DATE,
    fecha_fin DATE,
    aprendizaje VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (graduado_id) REFERENCES graduados(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (idioma_id) REFERENCES idiomas_catalogo(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    UNIQUE KEY uk_graduado_idiomas_semantic (graduado_id, idioma_id, nivel, fecha_inicio)
);

CREATE TABLE IF NOT EXISTS graduado_formaciones_complementarias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    graduado_id BIGINT NOT NULL,
    nombre_curso VARCHAR(150) NOT NULL,
    institucion VARCHAR(150),
    fecha_inicio DATE,
    fecha_fin DATE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (graduado_id) REFERENCES graduados(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE KEY uk_graduado_formaciones_semantic (graduado_id, nombre_curso, institucion, fecha_inicio)
);

CREATE TABLE IF NOT EXISTS graduado_trayectorias_laborales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    graduado_id BIGINT NOT NULL,
    empresa VARCHAR(150) NOT NULL,
    cargo VARCHAR(150) NOT NULL,
    modalidad VARCHAR(80),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (graduado_id) REFERENCES graduados(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    UNIQUE KEY uk_graduado_trayectorias_semantic (graduado_id, empresa, cargo, fecha_inicio)
);

CREATE TABLE IF NOT EXISTS directores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT UNIQUE,
    cargo VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS empleadores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT UNIQUE,
    ruc VARCHAR(12),
    razon_social VARCHAR(150),
    direccion VARCHAR(200),
    resumen_empresa TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS trabajos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    compania VARCHAR(255),
    cargo VARCHAR(255),
    modalidad VARCHAR(100),
    estado CHAR(1),
    fecha_inicio DATE,
    fecha_fin DATE,
    graduado_id BIGINT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (graduado_id) REFERENCES graduados(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS centros_educativos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estado CHAR(1),
    nombre VARCHAR(100),
    direccion VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tipos_evento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    estado CHAR(1),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    contenido TEXT,
    estado CHAR(1),
    director_id BIGINT,
    tipo_evento_id BIGINT,
    fecha_evento DATE,
    enlace_inscripcion VARCHAR(500),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (director_id) REFERENCES directores(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (tipo_evento_id) REFERENCES tipos_evento(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ofertas_laborales (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255),
    link VARCHAR(255),
    descripcion TEXT,
    estado CHAR(1),
    empleador_id BIGINT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (empleador_id) REFERENCES empleadores(id)
);

CREATE TABLE IF NOT EXISTS tipos_encuesta (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS encuestas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    tipo_encuesta_id BIGINT NOT NULL,
    estado VARCHAR(20) DEFAULT 'DRAFT' NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tipo_encuesta_id) REFERENCES tipos_encuesta(id)
);

CREATE TABLE IF NOT EXISTS preguntas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    encuesta_id BIGINT NOT NULL,
    texto_pregunta VARCHAR(500) NOT NULL,
    tipo_pregunta VARCHAR(50) NOT NULL,
    requerida BOOLEAN DEFAULT false,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (encuesta_id) REFERENCES encuestas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS opciones_pregunta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pregunta_id BIGINT NOT NULL,
    texto_opcion VARCHAR(255) NOT NULL,
    numero_orden INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pregunta_id) REFERENCES preguntas(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS respuestas_encuestas_graduados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    encuesta_id BIGINT NOT NULL,
    graduado_id BIGINT NOT NULL,
    enviada_en TIMESTAMP,
    completada BOOLEAN DEFAULT false,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (encuesta_id) REFERENCES encuestas(id),
    FOREIGN KEY (graduado_id) REFERENCES graduados(id),
    UNIQUE KEY uk_encuesta_graduado (encuesta_id, graduado_id)
);

CREATE TABLE IF NOT EXISTS respuestas_preguntas_graduados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    respuesta_encuesta_graduado_id BIGINT NOT NULL,
    pregunta_id BIGINT NOT NULL,
    respuesta_texto TEXT,
    respuesta_numerica INT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (respuesta_encuesta_graduado_id) REFERENCES respuestas_encuestas_graduados(id) ON DELETE CASCADE,
    FOREIGN KEY (pregunta_id) REFERENCES preguntas(id)
);

CREATE TABLE IF NOT EXISTS opciones_respuesta_preguntas_graduados (
    respuesta_id BIGINT NOT NULL,
    opcion_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (respuesta_id, opcion_id),
    FOREIGN KEY (respuesta_id) REFERENCES respuestas_preguntas_graduados(id) ON DELETE CASCADE,
    FOREIGN KEY (opcion_id) REFERENCES opciones_pregunta(id)
);

-- =============================================
-- FASE 5: Employer, Director, Event field additions
-- =============================================

-- Employer new fields
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'empleadores' AND COLUMN_NAME = 'direccion');
SET @ddl = IF(@col_exists = 0, 'ALTER TABLE empleadores ADD COLUMN direccion VARCHAR(200)', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'empleadores' AND COLUMN_NAME = 'resumen_empresa');
SET @ddl = IF(@col_exists = 0, 'ALTER TABLE empleadores ADD COLUMN resumen_empresa TEXT', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Director new field
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'directores' AND COLUMN_NAME = 'cargo');
SET @ddl = IF(@col_exists = 0, 'ALTER TABLE directores ADD COLUMN cargo VARCHAR(100)', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Event new fields
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'eventos' AND COLUMN_NAME = 'fecha_evento');
SET @ddl = IF(@col_exists = 0, 'ALTER TABLE eventos ADD COLUMN fecha_evento DATE', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'eventos' AND COLUMN_NAME = 'enlace_inscripcion');
SET @ddl = IF(@col_exists = 0, 'ALTER TABLE eventos ADD COLUMN enlace_inscripcion VARCHAR(500)', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Unique constraint on email
SET @idx_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'usuarios' AND INDEX_NAME = 'uk_usuarios_correo');
SET @ddl = IF(@idx_exists = 0, 'ALTER TABLE usuarios ADD CONSTRAINT uk_usuarios_correo UNIQUE (correo)', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;
