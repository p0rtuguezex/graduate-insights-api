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

CREATE TABLE IF NOT EXISTS graduados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT UNIQUE,
    fecha_inicio DATE,
    fecha_fin DATE,
    cv TEXT,
    ruta_cv VARCHAR(500),
    validado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS directores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT UNIQUE,
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
