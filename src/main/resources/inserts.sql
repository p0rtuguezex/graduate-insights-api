SET NAMES utf8mb4;
-- ============================================
-- SCRIPT DE DATOS INICIAL PARA GRADUATE INSIGHTS API
-- ============================================
-- 
-- Este archivo contiene todos los datos de ejemplo para la aplicación.
-- Organizado en orden de dependencias para evitar errores de FK.
-- Respeta exactamente la estructura de tables.sql
-- ============================================

-- ============================================
-- CATALOGOS ACADEMICOS
-- ============================================

INSERT INTO facultades (id, nombre, estado, fecha_creacion, fecha_modificacion)
VALUES
    (1, 'Facultad de ingenieria de Sistemas e Ingeniería Civil', '1', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    estado = VALUES(estado),
    fecha_modificacion = NOW();

INSERT INTO escuelas_profesionales (id, facultad_id, nombre, estado, fecha_creacion, fecha_modificacion)
VALUES
    (1, 1, 'Escuela Académica Profesional de Ingeniería de Sistemas', '1', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    facultad_id = VALUES(facultad_id),
    nombre = VALUES(nombre),
    estado = VALUES(estado),
    fecha_modificacion = NOW();

INSERT INTO tipos_grado (id, codigo, nombre, estado, fecha_creacion, fecha_modificacion)
VALUES
    (1, 'BACHILLER', 'Bachiller', '1', NOW(), NOW()),
    (2, 'TITULADO', 'Titulado', '1', NOW(), NOW()),
    (3, 'MAESTRIA', 'Maestria', '1', NOW(), NOW()),
    (4, 'DOCTORADO', 'Doctorado', '1', NOW(), NOW()),
    (5, 'OTRO', 'Otro', '1', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    codigo = VALUES(codigo),
    nombre = VALUES(nombre),
    estado = VALUES(estado),
    fecha_modificacion = NOW();

INSERT INTO modalidades_titulacion (id, codigo, nombre, estado, fecha_creacion, fecha_modificacion)
VALUES
    (1, 'EXAMEN_SUFICIENCIA', 'Examen de suficiencia', '1', NOW(), NOW()),
    (2, 'TESIS', 'Tesis', '1', NOW(), NOW()),
    (3, 'OTROS', 'Otros', '1', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    codigo = VALUES(codigo),
    nombre = VALUES(nombre),
    estado = VALUES(estado),
    fecha_modificacion = NOW();

INSERT INTO idiomas_catalogo (id, codigo, nombre, estado, fecha_creacion, fecha_modificacion)
VALUES
    (1, 'ES', 'Espanol', '1', NOW(), NOW()),
    (2, 'EN', 'Ingles', '1', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    codigo = VALUES(codigo),
    nombre = VALUES(nombre),
    estado = VALUES(estado),
    fecha_modificacion = NOW();

INSERT INTO universidades (id, nombre, estado, fecha_creacion, fecha_modificacion)
VALUES
    (1, 'Universidad Nacional San Luis Gonzaga', '1', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    estado = VALUES(estado),
    fecha_modificacion = NOW();

-- ============================================
-- USUARIOS
-- ============================================

INSERT INTO usuarios (nombres, apellidos, fecha_nacimiento, genero, correo, estado, dni, celular, contrasena, verificado, fecha_creacion, fecha_modificacion)
VALUES-- Usuario para director
    ('Director', 'Académico', '1970-01-01', 'M', 'director@university.edu', '1', '12345670', '987654343', '$2a$10$AzS.1vRFEWlMYCZT4SCbu.QqlRGci70s.5WiyFnljZctq2vs4xfNG', true, NOW(), NOW());



-- ============================================
-- DIRECTORES
-- ============================================

INSERT INTO directores (usuario_id, fecha_creacion, fecha_modificacion)
VALUES
    (1, NOW(), NOW());



-- ============================================
-- TIPOS DE EVENTOS
-- ============================================

INSERT INTO tipos_evento (nombre, estado, fecha_creacion, fecha_modificacion)
VALUES
    ('Conferencia Tecnológica', '1', NOW(), NOW()),
    ('Feria de Empleo', '1', NOW(), NOW()),
    ('Workshop de Emprendimiento', '1', NOW(), NOW()),
    ('Seminario de Liderazgo', '1', NOW(), NOW());


-- ============================================
-- TIPOS DE ENCUESTA
-- ============================================

INSERT INTO tipos_encuesta (nombre, descripcion, activo, fecha_creacion, fecha_modificacion)
VALUES
    ('EMPLOYMENT', 'Encuestas relacionadas con el empleo y situación laboral de egresados', true, NOW(), NOW()),
    ('ACADEMIC', 'Encuestas sobre formación académica y desarrollo profesional', true, NOW(), NOW()),
    ('SATISFACTION', 'Encuestas de satisfacción y experiencia estudiantil', true, NOW(), NOW()),
    ('ENTREPRENEURSHIP', 'Encuestas sobre emprendimiento y creación de empresas', true, NOW(), NOW());

