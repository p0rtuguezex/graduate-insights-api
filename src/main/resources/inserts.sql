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

INSERT INTO users (nombres, apellidos, fecha_nacimiento, genero, correo, estado, dni, celular, contrasena, created_date, modified_date)
VALUES
    ('Juan Carlos', 'Pérez López', '1995-03-15', 'M', 'juan.perez@email.com', '1', '12345678', '987654321', '$2a$10$example', NOW(), NOW()),
    ('María Elena', 'García Rodríguez', '1996-07-22', 'F', 'maria.garcia@email.com', '1', '23456789', '987654322', '$2a$10$example', NOW(), NOW()),
    ('Carlos Alberto', 'Martínez Silva', '1994-11-08', 'M', 'carlos.martinez@email.com', '1', '34567890', '987654323', '$2a$10$example', NOW(), NOW()),
    ('Ana Sofía', 'Torres Mendoza', '1997-02-14', 'F', 'ana.torres@email.com', '1', '45678901', '987654324', '$2a$10$example', NOW(), NOW()),
    ('Diego Fernando', 'Morales Castro', '1995-09-30', 'M', 'diego.morales@email.com', '1', '56789012', '987654325', '$2a$10$example', NOW(), NOW()),
    ('Lucía Isabel', 'Herrera Vásquez', '1996-12-03', 'F', 'lucia.herrera@email.com', '1', '67890123', '987654326', '$2a$10$example', NOW(), NOW()),
    ('Andrés Felipe', 'Jiménez Paredes', '1994-06-18', 'M', 'andres.jimenez@email.com', '1', '78901234', '987654327', '$2a$10$example', NOW(), NOW()),
    ('Valentina', 'Cruz Delgado', '1997-04-25', 'F', 'valentina.cruz@email.com', '1', '89012345', '987654328', '$2a$10$example', NOW(), NOW()),
    ('Roberto Manuel', 'Sánchez Flores', '1995-01-12', 'M', 'roberto.sanchez@email.com', '1', '90123456', '987654329', '$2a$10$example', NOW(), NOW()),
    ('Camila Andrea', 'Ramírez Guerrero', '1996-08-07', 'F', 'camila.ramirez@email.com', '1', '01234567', '987654330', '$2a$10$example', NOW(), NOW()),
    ('Gabriel Eduardo', 'Vargas Montoya', '1994-10-16', 'M', 'gabriel.vargas@email.com', '1', '12345679', '987654331', '$2a$10$example', NOW(), NOW()),
    ('Isabella', 'Ortega Ruiz', '1997-05-29', 'F', 'isabella.ortega@email.com', '1', '23456780', '987654332', '$2a$10$example', NOW(), NOW()),
    ('Sebastián', 'Romero Aguilar', '1995-12-04', 'M', 'sebastian.romero@email.com', '1', '34567891', '987654333', '$2a$10$example', NOW(), NOW()),
    ('Natalia', 'Pineda Cortés', '1996-03-21', 'F', 'natalia.pineda@email.com', '1', '45678902', '987654334', '$2a$10$example', NOW(), NOW()),
    ('Alejandro', 'Castillo Ramos', '1994-09-13', 'M', 'xmcgiver12@gmail.com', '1', '56789013', '987654335', '$2a$10$AzS.1vRFEWlMYCZT4SCbu.QqlRGci70s.5WiyFnljZctq2vs4xfNG', NOW(), NOW()),
    ('Sofía Alejandra', 'Luna Espinoza', '1997-01-08', 'F', 'sofia.luna@email.com', '1', '67890124', '987654336', '$2a$10$example', NOW(), NOW()),
    -- Usuarios para empleadores
    ('Tech Solutions', 'SAC', '1990-01-01', 'M', 'contact@techsolutions.com', '1', '20123456789', '987654340', '$2a$10$AzS.1vRFEWlMYCZT4SCbu.QqlRGci70s.5WiyFnljZctq2vs4xfNG', NOW(), NOW()),
    ('Banco Nacional', 'SA', '1985-01-01', 'M', 'info@banconacional.com', '1', '20234567890', '987654341', '$2a$10$example', NOW(), NOW()),
    ('Clínica San Miguel', 'SAC', '1995-01-01', 'F', 'contacto@clinicasanmiguel.com', '1', '20345678901', '987654342', '$2a$10$example', NOW(), NOW()),
    -- Usuario para director
    ('Director', 'Académico', '1970-01-01', 'M', 'director@university.edu', '1', '12345670', '987654343', '$2a$10$AzS.1vRFEWlMYCZT4SCbu.QqlRGci70s.5WiyFnljZctq2vs4xfNG', NOW(), NOW());

-- ============================================
-- GRADUADOS
-- ============================================

INSERT INTO graduates (user_id, fecha_inicio, fecha_fin, cv, created_date, modified_date)
VALUES
    (1, '2016-03-01', '2020-12-15', 'Graduado en Ingeniería de Sistemas con experiencia en desarrollo web', NOW(), NOW()),
    (2, '2017-08-01', '2021-06-20', 'Graduada en Administración con especialización en gestión de proyectos', NOW(), NOW()),
    (3, '2015-03-01', '2019-11-10', 'Graduado en Contabilidad con certificación en auditoría', NOW(), NOW()),
    (4, '2017-03-01', '2021-03-15', 'Graduada en Marketing con experiencia en marketing digital', NOW(), NOW()),
    (5, '2016-08-01', '2020-08-25', 'Graduado en Ingeniería Industrial con especialización en procesos', NOW(), NOW()),
    (6, '2017-02-01', '2021-01-30', 'Graduada en Psicología con experiencia organizacional', NOW(), NOW()),
    (7, '2016-03-01', '2020-07-12', 'Graduado en Diseño Gráfico con portfolio digital', NOW(), NOW()),
    (8, '2017-08-01', '2021-05-18', 'Graduada en Comunicación Social con experiencia en medios', NOW(), NOW()),
    (9, '2015-03-01', '2019-12-08', 'Graduado en Ingeniería Civil con experiencia en construcción', NOW(), NOW()),
    (10, '2016-08-01', '2020-10-22', 'Graduado en Arquitectura con proyectos residenciales', NOW(), NOW()),
    (11, '2017-02-01', '2021-02-14', 'Graduada en Enfermería con experiencia hospitalaria', NOW(), NOW()),
    (12, '2016-03-01', '2020-09-05', 'Graduado en Derecho con especialización en derecho corporativo', NOW(), NOW()),
    (13, '2015-08-01', '2019-07-30', 'Graduado en Medicina con residencia en medicina interna', NOW(), NOW()),
    (14, '2017-03-01', '2021-04-12', 'Graduada en Odontología con consulta privada', NOW(), NOW()),
    (15, '2016-08-01', '2020-11-28', 'Graduado en Veterinaria con experiencia en clínica', NOW(), NOW()),
    (16, '2017-02-01', '2021-01-15', 'Graduada en Educación con experiencia en preescolar', NOW(), NOW());

-- ============================================
-- DIRECTORES
-- ============================================

INSERT INTO director (user_id, created_date, modified_date)
VALUES
    (20, NOW(), NOW());

-- ============================================
-- EMPLEADORES
-- ============================================

INSERT INTO employers (user_id, ruc, razon_social, created_date, modified_date)
VALUES
    (17, '20123456789', 'Tech Solutions SAC', NOW(), NOW()),
    (18, '20234567890', 'Banco Nacional SA', NOW(), NOW()),
    (19, '20345678901', 'Clínica San Miguel SAC', NOW(), NOW());

-- ============================================
-- CENTROS EDUCATIVOS
-- ============================================

INSERT INTO education_centers (estado, nombre, direccion, created_date, modified_date)
VALUES
    ('1', 'Universidad Tecnológica del Perú', 'Av. Arequipa 265, Lima', NOW(), NOW()),
    ('1', 'Instituto Superior Tecnológico', 'Av. Parra 123, Arequipa', NOW(), NOW()),
    ('1', 'Universidad Nacional Mayor de San Marcos', 'Ciudad Universitaria, Lima', NOW(), NOW());

-- ============================================
-- TIPOS DE EVENTOS
-- ============================================

INSERT INTO event_types (nombre, estado, created_date, modified_date)
VALUES
    ('Conferencia Tecnológica', '1', NOW(), NOW()),
    ('Feria de Empleo', '1', NOW(), NOW()),
    ('Workshop de Emprendimiento', '1', NOW(), NOW()),
    ('Seminario de Liderazgo', '1', NOW(), NOW());

-- ============================================
-- TRABAJOS
-- ============================================

INSERT INTO jobs (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduate_id, created_date, modified_date)
VALUES
    ('Tech Solutions SAC', 'Desarrollador Full Stack', 'Presencial', '1', '2021-01-15', NULL, 1, NOW(), NOW()),
    ('Banco Nacional SA', 'Analista de Créditos', 'Híbrido', '1', '2021-07-01', NULL, 2, NOW(), NOW()),
    ('Banco Nacional SA', 'Contador Senior', 'Presencial', '1', '2020-01-10', NULL, 3, NOW(), NOW()),
    ('Tech Solutions SAC', 'Especialista en Marketing Digital', 'Remoto', '1', '2021-04-01', NULL, 4, NOW(), NOW()),
    ('Tech Solutions SAC', 'Ingeniero de Procesos', 'Presencial', '1', '2020-09-15', NULL, 5, NOW(), NOW()),
    ('Clínica San Miguel SAC', 'Psicóloga Organizacional', 'Presencial', '1', '2021-02-20', NULL, 6, NOW(), NOW()),
    ('Tech Solutions SAC', 'Diseñador UX/UI', 'Remoto', '1', '2020-08-01', NULL, 7, NOW(), NOW()),
    ('Universidad Tecnológica', 'Coordinadora de Comunicaciones', 'Presencial', '1', '2021-06-01', NULL, 8, NOW(), NOW());

-- ============================================
-- OFERTAS DE TRABAJO
-- ============================================

INSERT INTO jobs_offers (titulo, link, descripcion, estado, employer_id, created_date, modified_date)
VALUES
    ('Desarrollador Backend Senior', 'https://techsolutions.com/jobs/backend-senior', 'Desarrollador con experiencia en Java y Spring Boot', '1', 1, NOW(), NOW()),
    ('Analista Financiero', 'https://banconacional.com/careers/analista-financiero', 'Analista para área de riesgos financieros', '1', 2, NOW(), NOW()),
    ('Enfermera Especializada', 'https://clinicasanmiguel.com/jobs/enfermera', 'Enfermera para área de cuidados intensivos', '1', 3, NOW(), NOW()),
    ('Data Scientist', 'https://techsolutions.com/jobs/data-scientist', 'Científico de datos con experiencia en Python y ML', '1', 1, NOW(), NOW());

-- ============================================
-- TIPOS DE ENCUESTA
-- ============================================

INSERT INTO survey_types (name, description, active, created_date, modified_date)
VALUES
    ('EMPLOYMENT', 'Encuestas relacionadas con el empleo y situación laboral de egresados', true, NOW(), NOW()),
    ('ACADEMIC', 'Encuestas sobre formación académica y desarrollo profesional', true, NOW(), NOW()),
    ('SATISFACTION', 'Encuestas de satisfacción y experiencia estudiantil', true, NOW(), NOW()),
    ('ENTREPRENEURSHIP', 'Encuestas sobre emprendimiento y creación de empresas', true, NOW(), NOW());

-- ============================================
-- ENCUESTAS
-- ============================================

INSERT INTO surveys (title, description, survey_type_id, status, start_date, end_date, created_date, modified_date)
VALUES
    ('Encuesta de Satisfacción Laboral', 'Encuesta para evaluar la satisfacción de los egresados en sus trabajos actuales', 1, 'ACTIVE', '2024-01-01', '2024-12-31', NOW(), NOW()),
    ('Encuesta de Seguimiento Académico', 'Encuesta para hacer seguimiento de la formación académica de los egresados', 2, 'ACTIVE', '2024-01-15', '2024-06-30', NOW(), NOW()),
    ('Encuesta de Clima Laboral', 'Evaluación del ambiente de trabajo en las empresas donde laboran los egresados', 1, 'ACTIVE', '2024-02-01', '2024-08-31', NOW(), NOW()),
    ('Encuesta de Competencias Digitales', 'Evaluación de habilidades tecnológicas de los egresados', 3, 'COMPLETED', '2023-06-01', '2023-12-31', NOW(), NOW()),
    ('Encuesta de Emprendimiento', 'Evaluación de intenciones y actividades emprendedoras', 4, 'ACTIVE', '2024-03-01', '2024-09-30', NOW(), NOW());

-- ============================================
-- PREGUNTAS
-- ============================================

-- Preguntas para Encuesta 1 (Satisfacción Laboral)
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (1, '¿Cuál es tu nivel de satisfacción con tu trabajo actual?', 'SCALE', true, NOW(), NOW()),
    (1, '¿En qué sector trabajas actualmente?', 'SINGLE_CHOICE', true, NOW(), NOW()),
    (1, '¿Qué aspectos valoras más de tu trabajo actual?', 'MULTIPLE_CHOICE', false, NOW(), NOW()),
    (1, '¿Recomendarías tu empresa actual a otros egresados?', 'YES_NO', true, NOW(), NOW()),
    (1, 'Comparte algún comentario adicional sobre tu experiencia laboral', 'TEXT', false, NOW(), NOW());

-- Preguntas para Encuesta 2 (Seguimiento Académico)
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (2, '¿Has realizado estudios de posgrado?', 'YES_NO', true, NOW(), NOW()),
    (2, '¿Qué tipo de estudios adicionales has realizado?', 'MULTIPLE_CHOICE', false, NOW(), NOW()),
    (2, '¿Cuánto inviertes mensualmente en capacitación?', 'NUMBER', false, NOW(), NOW());

-- Preguntas para Encuesta 3 (Clima Laboral)
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (3, '¿Cómo calificarías el ambiente de trabajo en tu empresa?', 'SCALE', true, NOW(), NOW()),
    (3, '¿Tu empresa ofrece trabajo remoto?', 'YES_NO', true, NOW(), NOW()),
    (3, 'Describe brevemente el aspecto que más te gusta de tu trabajo', 'TEXT', false, NOW(), NOW());

-- Preguntas para Encuesta 4 (Competencias Digitales)
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (4, '¿Cómo evalúas tu nivel general de competencias digitales?', 'SCALE', true, NOW(), NOW()),
    (4, '¿Qué herramientas digitales utilizas frecuentemente en tu trabajo?', 'MULTIPLE_CHOICE', true, NOW(), NOW()),
    (4, 'Menciona la tecnología que más te gustaría aprender', 'TEXT', false, NOW(), NOW());

-- Preguntas para Encuesta 5 (Emprendimiento)
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (5, '¿Tienes intención de emprender en los próximos 2 años?', 'YES_NO', true, NOW(), NOW()),
    (5, '¿En qué sector te gustaría emprender?', 'SINGLE_CHOICE', false, NOW(), NOW()),
    (5, 'Describe brevemente tu idea de negocio (si la tienes)', 'TEXT', false, NOW(), NOW());

-- ============================================
-- OPCIONES DE PREGUNTAS
-- ============================================

-- Opciones para pregunta 1 (satisfacción - escala)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (1, '1 - Muy insatisfecho', 1, NOW(), NOW()),
    (1, '2 - Insatisfecho', 2, NOW(), NOW()),
    (1, '3 - Neutral', 3, NOW(), NOW()),
    (1, '4 - Satisfecho', 4, NOW(), NOW()),
    (1, '5 - Muy satisfecho', 5, NOW(), NOW());

-- Opciones para pregunta 2 (sector)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (2, 'Tecnología', 1, NOW(), NOW()),
    (2, 'Finanzas', 2, NOW(), NOW()),
    (2, 'Salud', 3, NOW(), NOW()),
    (2, 'Educación', 4, NOW(), NOW()),
    (2, 'Manufactura', 5, NOW(), NOW()),
    (2, 'Servicios', 6, NOW(), NOW());

-- Opciones para pregunta 3 (aspectos valorados)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (3, 'Buen ambiente laboral', 1, NOW(), NOW()),
    (3, 'Salario competitivo', 2, NOW(), NOW()),
    (3, 'Oportunidades de crecimiento', 3, NOW(), NOW()),
    (3, 'Flexibilidad horaria', 4, NOW(), NOW()),
    (3, 'Capacitación constante', 5, NOW(), NOW()),
    (3, 'Beneficios adicionales', 6, NOW(), NOW());

-- Opciones para pregunta 4 (recomendación)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (4, 'Sí', 1, NOW(), NOW()),
    (4, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 6 (estudios posgrado)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (6, 'Sí', 1, NOW(), NOW()),
    (6, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 7 (tipo de estudios)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (7, 'Maestría', 1, NOW(), NOW()),
    (7, 'Doctorado', 2, NOW(), NOW()),
    (7, 'Diplomado', 3, NOW(), NOW()),
    (7, 'Certificaciones técnicas', 4, NOW(), NOW()),
    (7, 'Cursos online', 5, NOW(), NOW());

-- Opciones para pregunta 9 (ambiente trabajo - escala)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (9, '1 - Muy malo', 1, NOW(), NOW()),
    (9, '2 - Malo', 2, NOW(), NOW()),
    (9, '3 - Regular', 3, NOW(), NOW()),
    (9, '4 - Bueno', 4, NOW(), NOW()),
    (9, '5 - Excelente', 5, NOW(), NOW());

-- Opciones para pregunta 10 (trabajo remoto)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (10, 'Sí', 1, NOW(), NOW()),
    (10, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 12 (competencias digitales - escala)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (12, '1 - Básico', 1, NOW(), NOW()),
    (12, '2 - Intermedio bajo', 2, NOW(), NOW()),
    (12, '3 - Intermedio', 3, NOW(), NOW()),
    (12, '4 - Avanzado', 4, NOW(), NOW()),
    (12, '5 - Experto', 5, NOW(), NOW());

-- Opciones para pregunta 13 (herramientas digitales)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (13, 'Microsoft Office', 1, NOW(), NOW()),
    (13, 'Google Workspace', 2, NOW(), NOW()),
    (13, 'Herramientas de diseño (Photoshop, Figma)', 3, NOW(), NOW()),
    (13, 'Lenguajes de programación', 4, NOW(), NOW()),
    (13, 'Bases de datos', 5, NOW(), NOW()),
    (13, 'Herramientas de análisis (Excel, Tableau)', 6, NOW(), NOW());

-- Opciones para pregunta 15 (intención emprender)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (15, 'Sí', 1, NOW(), NOW()),
    (15, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 16 (sector emprendimiento)
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (16, 'Tecnología e innovación', 1, NOW(), NOW()),
    (16, 'Comercio y retail', 2, NOW(), NOW()),
    (16, 'Servicios profesionales', 3, NOW(), NOW()),
    (16, 'Alimentos y bebidas', 4, NOW(), NOW()),
    (16, 'Turismo y entretenimiento', 5, NOW(), NOW()),
    (16, 'Salud y bienestar', 6, NOW(), NOW());

-- ============================================
-- RESPUESTAS DE GRADUADOS A ENCUESTAS
-- ============================================

-- Respuestas del graduado 1 a la encuesta 1
INSERT INTO graduate_survey_responses (survey_id, graduate_id, submitted_at, completed, created_date, modified_date)
VALUES
    (1, 15, '2025-06-25 16:16:18', true, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (1, 14, '2025-06-25 16:16:18', true, '2025-06-25 16:16:18', '2025-06-25 16:16:18');

-- Respuestas individuales a cada pregunta
INSERT INTO graduate_question_responses (graduate_survey_response_id, question_id, text_response, numeric_response, created_date, modified_date)
VALUES
    (1, 1, NULL, NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (1, 2, NULL, NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (1, 3, NULL, NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (1, 4, NULL, NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (1, 5, 'buena paga', NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (2, 1, NULL, NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (2, 2, NULL, NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (2, 3, NULL, NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (2, 4, NULL, NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18'),
    (2, 5, 'buen ambiente laboral', NULL, '2025-06-25 16:16:18', '2025-06-25 16:16:18');

-- Vinculación de respuestas con opciones específicas
INSERT INTO graduate_question_response_options (response_id, option_id, created_date)
VALUES
    (1, 5, '2025-06-25 16:16:18'),    -- Pregunta 1: "5 - Muy satisfecho"
    (2, 6, '2025-06-25 16:16:18'),    -- Pregunta 2: "Servicios" 
    (3, 12, '2025-06-25 16:16:18'),   -- Pregunta 3: Primera opción seleccionada
    (3, 13, '2025-06-25 16:16:18'),   -- Pregunta 3: Segunda opción seleccionada
    (3, 14, '2025-06-25 16:16:18'),   -- Pregunta 3: Tercera opción seleccionada
    (4, 18, '2025-06-25 16:16:18'),   -- Pregunta 4: "Sí"
    (6, 4, '2025-06-25 16:16:18'),    -- Pregunta 1: "5 - Muy satisfecho"
    (7, 6, '2025-06-25 16:16:18'),    -- Pregunta 2: "Servicios"
    (8, 12, '2025-06-25 16:16:18'),   -- Pregunta 3: Primera opción seleccionada
    (8, 13, '2025-06-25 16:16:18'),   -- Pregunta 3: Segunda opción seleccionada
    (8, 14, '2025-06-25 16:16:18');   -- Pregunta 3: Tercera opción seleccionada

-- ============================================
-- RESPUESTAS DE GRADUADOS A ENCUESTAS
-- ============================================
-- 
-- Esta sección contiene datos de prueba para validar las estadísticas.
-- Incluye respuestas del graduado 1 a la encuesta 1 con todas las
-- vinculaciones correctas entre respuestas y opciones seleccionadas.
-- ============================================
