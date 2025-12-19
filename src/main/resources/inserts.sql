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

