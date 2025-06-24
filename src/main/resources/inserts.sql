INSERT INTO users (nombres, apellidos, fecha_nacimiento, genero, correo, estado, dni, celular, contrasena, created_date, modified_date)
VALUES
    ('Juan', 'Pérez', '1990-05-10', 'M', 'juan.perez@example.com', '1', '12345678', '987654321', 'pass123', NOW(), NOW()),
    ('Lucía', 'Gómez', '1995-08-22', 'F', 'lucia.gomez@example.com', '1', '87654321', '912345678', 'secure456', NOW(), NOW()),
    ('Carlos', 'Ramírez', '1988-11-15', 'M', 'carlos.ramirez@example.com', '1', '11223344', '998877665', 'abc123', NOW(), NOW()),
    ('María', 'Fernández', '1992-03-12', 'F', 'maria.fernandez@example.com', '1', '44556677', '911223344', 'qwerty1', NOW(), NOW()),
    ('José', 'López', '1987-07-30', 'M', 'jose.lopez@example.com', '1', '22334455', '922334455', 'password1', NOW(), NOW()),
    ('Ana', 'Torres', '1993-01-20', 'F', 'ana.torres@example.com', '1', '55667788', '933445566', 'mysecurepass', NOW(), NOW()),
    ('Luis', 'Martínez', '1991-06-25', 'M', 'luis.martinez@example.com', '1', '66778899', '944556677', '1234abcd', NOW(), NOW()),
    ('Carmen', 'Díaz', '1994-09-10', 'F', 'carmen.diaz@example.com', '1', '77889900', '955667788', 'superpass', NOW(), NOW()),
    ('Jorge', 'Vargas', '1989-12-05', 'M', 'jorge.vargas@example.com', '1', '88990011', '966778899', 'admin2020', NOW(), NOW()),
    ('Elena', 'Castro', '1996-04-18', 'F', 'elena.castro@example.com', '1', '99001122', '977889900', 'contraseña', NOW(), NOW()),
    ('Miguel', 'Ruiz', '1990-10-01', 'M', 'miguel.ruiz@example.com', '1', '10293847', '988900112', 'clave123', NOW(), NOW()),
    ('Sofía', 'Mendoza', '1992-02-14', 'F', 'sofia.mendoza@example.com', '1', '11235813', '999011223', 'pass987', NOW(), NOW()),
    ('Andrés', 'Silva', '1985-11-20', 'M', 'andres.silva@example.com', '1', '12312312', '900122334', 'testpass', NOW(), NOW()),
    ('Laura', 'Rojas', '1993-03-08', 'F', 'laura.rojas@example.com', '1', '23423423', '911233445', 'mypass789', NOW(), NOW()),
    ('Ricardo', 'Herrera', '1997-07-07', 'M', 'ricardo.herrera@example.com', '1', '34534534', '922344556', 'rrhh2022', NOW(), NOW()),
    ('Patricia', 'Morales', '1986-05-25', 'F', 'patricia.morales@example.com', '1', '45645645', '933455667', 'login123', NOW(), NOW()),
    ('Fernando', 'Cruz', '1991-12-12', 'M', 'fernando.cruz@example.com', '1', '56756756', '944566778', 'cruzpass', NOW(), NOW()),
    ('Isabel', 'Salas', '1995-01-01', 'F', 'isabel.salas@example.com', '1', '67867867', '955677889', 'isapass', NOW(), NOW()),
    ('Diego', 'Ortega', '1990-06-15', 'M', 'diego.ortega@example.com', '1', '78978978', '966788990', '$2a$10$AzS.1vRFEWlMYCZT4SCbu.QqlRGci70s.5WiyFnljZctq2vs4xfNG', NOW(), NOW()),
    ('Valeria', 'Reyes', '1988-09-22', 'F', 'valeria.reyes@example.com', '1', '89089089', '977899001', 'reyesclave', NOW(), NOW()),
    ('Manuel', 'Aguilar', '1992-08-30', 'M', 'manuel.aguilar@example.com', '1', '90190190', '988900112', 'aguipass', NOW(), NOW()),
    ('Camila', 'Campos', '1996-03-03', 'F', 'camila.campos@example.com', '1', '81281281', '999011223', 'camp123', NOW(), NOW()),
    ('Sebastián', 'Paredes', '1987-12-28', 'M', 'sebastian.paredes@example.com', '1', '92392392', '900122334', 'par123', NOW(), NOW()),
    ('Daniela', 'Guerra', '1993-07-19', 'F', 'daniela.guerra@example.com', '1', '83483483', '911233445', 'guerra2023', NOW(), NOW()),
    ('Francisco', 'Navarro', '1989-01-11', 'M', 'francisco.navarro@example.com', '1', '74574574', '922344556', 'navarrokey', NOW(), NOW()),
    ('Gabriela', 'Peña', '1994-11-09', 'F', 'gabriela.pena@example.com', '1', '65665665', '933455667', 'gabypass', NOW(), NOW()),
    ('Álvaro', 'Mora', '1986-04-17', 'M', 'alvaro.mora@example.com', '1', '56756756', '944566778', 'moraclave', NOW(), NOW()),
    ('Paula', 'Vega', '1995-10-10', 'F', 'paula.vega@example.com', '1', '47847847', '955677889', 'vegapass', NOW(), NOW()),
    ('Julio', 'Delgado', '1990-02-02', 'M', 'julio.delgado@example.com', '1', '38938938', '966788990', 'delgado123', NOW(), NOW()),
    ('Rocío', 'Castañeda', '1991-09-09', 'F', 'rocio.castaneda@example.com', '1', '29029029', '977899001', 'rocio456', NOW(), NOW()),
    ('Mc Giver', 'Avila', '1991-09-09', 'F', 'xmcgiver12@gmail.com', '1', '71450633', '977899001', '$2a$10$AzS.1vRFEWlMYCZT4SCbu.QqlRGci70s.5WiyFnljZctq2vs4xfNG', NOW(), NOW());


INSERT INTO graduates
 (fecha_inicio, fecha_fin, cv, created_date, modified_date, user_id)
VALUES
('2018-03-01', '2022-12-15', 'cv_juan_perez.pdf', NOW(), NOW(), 1),
('2019-08-20', '2023-07-10', 'cv_ana_ramirez.docx', NOW(), NOW(), 2),
('2017-01-10', '2021-06-30', 'cv_carlos_torres.pdf', NOW(), NOW(), 3),
('2020-02-05', '2024-11-18', 'cv_luisa_fernandez.pdf', NOW(), NOW(), 4),
('2016-09-25', '2020-05-22', 'cv_pedro_gomez.docx', NOW(), NOW(), 5),
('2018-04-14', '2022-03-10', 'cv_maria_salas.pdf', NOW(), NOW(), 6),
('2019-06-11', '2023-02-28', 'cv_sofia_vega.docx', NOW(), NOW(), 7),
('2015-11-01', '2019-09-15', 'cv_andres_lopez.pdf', NOW(), NOW(), 8),
('2021-01-20', '2025-01-19', 'cv_laura_martinez.pdf', NOW(), NOW(), 9),
('2017-05-30', '2021-12-01', 'cv_miguel_herrera.pdf', NOW(), NOW(), 10),
('2016-10-05', '2020-08-17', 'cv_elena_castro.docx', NOW(), NOW(), 11),
('2020-03-12', '2024-06-29', 'cv_diego_rojas.pdf', NOW(), NOW(), 12),
('2018-07-22', '2022-05-05', 'cv_camila_nunez.docx', NOW(), NOW(), 13),
('2019-12-01', '2023-10-20', 'cv_fernando_silva.pdf', NOW(), NOW(), 14),
('2017-09-10', '2021-07-30', 'cv_valeria_reyes.docx', NOW(), NOW(), 15),
('2017-09-10', '2021-07-30', 'cv_valeria_reyes.docx', NOW(), NOW(), 31);

INSERT INTO employers
(ruc, razon_social, created_date, modified_date, user_id)
VALUES
('20733722962', 'portuguez E.I.R.L', NOW(), NOW(), 16),
('20163487962', 'Casita S.A.C.', NOW(), NOW(), 17),
('20498722962', 'Petro S.A.C.', NOW(), NOW(), 18);

INSERT INTO director
(created_date, modified_date, user_id)
VALUES
    (NOW(), NOW(), 19);


INSERT INTO jobs (compania, cargo, modalidad, estado, fecha_inicio, fecha_fin, graduate_id)
VALUES
    ('Empresa ABC S.A.', 'Analista de Datos', 'Remoto','1', '2020-01-10', '2021-06-30', 1),
    ('Tech Solutions', 'Desarrollador Backend', 'Presencial','1', '2021-07-01', '2023-02-28', 1),
    ('Consulting Group', 'Ingeniero de Software', 'Híbrido','1', '2023-03-01', NULL, 1);

INSERT INTO education_centers (estado, nombre, direccion, created_date, modified_date)
VALUES
    ('1', 'Universidad Nacional de Ucayali', 'Pucallpa', NOW(), NOW()),
    ('1', 'UTP', 'Lima', NOW(), NOW()),
    ('1', 'UNI', 'Lima', NOW(), NOW());

INSERT INTO event_types (nombre, estado, created_date, modified_date)
VALUES
    ('Ponencia', '1', NOW(), NOW()),
    ('Taller', '1', NOW(), NOW()),
    ('Capacitación', '1', NOW(), NOW());

INSERT INTO jobs_offers (titulo, link, descripcion, estado, created_date, modified_date, employer_id)
VALUES
    ('Analista de Datos', 'https://abc.com/jobs/1', 'Análisis y visualización de datos en entorno remoto.', '1', NOW(), NOW(), 1),
    ('Desarrollador Backend', 'https://techsolutions.com/jobs/2', 'Responsable del desarrollo de servicios y APIs REST.', '1', NOW(), NOW(), 1),
    ('Ingeniero de Software', 'https://consultinggroup.com/jobs/3', 'Desarrollo de software a medida para clientes internacionales.', '1', NOW(), NOW(), 1),
    ('Tester QA', 'https://qualityjobs.com/jobs/4', 'Pruebas automatizadas de calidad de software.', '1', NOW(), NOW(), 1),
    ('Líder Técnico', 'https://leadtech.com/jobs/5', 'Coordinación técnica de equipos de desarrollo.', '1', NOW(), NOW(), 1),
    ('Administrador de Base de Datos', 'https://dbadmin.com/jobs/6', 'Mantenimiento y optimización de bases de datos.', '1', NOW(), NOW(), 1),
    ('Especialista DevOps', 'https://devopsworld.com/jobs/7', 'Automatización de infraestructura y CI/CD.', '1', NOW(), NOW(), 1),
    ('Diseñador UI/UX', 'https://designhub.com/jobs/8', 'Diseño de interfaces amigables y centradas en el usuario.', '1', NOW(), NOW(), 1),
    ('Project Manager', 'https://pmjobs.com/jobs/9', 'Gestión de proyectos bajo metodología ágil.', '1', NOW(), NOW(), 1),
    ('Soporte Técnico', 'https://supportline.com/jobs/10', 'Atención al cliente y resolución de incidencias.', '1', NOW(), NOW(), 1),
    ('Data Scientist', 'https://datasciencecorp.com/jobs/11', 'Modelado estadístico y machine learning.', '1', NOW(), NOW(), 1),
    ('Scrum Master', 'https://agileteam.com/jobs/12', 'Facilitación de procesos ágiles y gestión de equipos.', '1', NOW(), NOW(), 1),
    ('Arquitecto de Software', 'https://architectureit.com/jobs/13', 'Diseño de arquitecturas escalables y robustas.', '1', NOW(), NOW(), 1),
    ('Ingeniero de Seguridad', 'https://secureit.com/jobs/14', 'Análisis de vulnerabilidades y ciberseguridad.', '1', NOW(), NOW(), 1),
    ('Administrador de Redes', 'https://networks.com/jobs/15', 'Gestión de redes LAN/WAN y seguridad perimetral.', '1', NOW(), NOW(), 1);

-- Inserts para encuestas
INSERT INTO surveys (title, description, survey_type, status, start_date, end_date, created_date, modified_date)
VALUES
    ('Encuesta de Satisfacción Laboral', 'Encuesta para evaluar la satisfacción de los egresados en sus trabajos actuales', 'EMPLOYMENT', 'ACTIVE', '2024-01-01', '2024-12-31', NOW(), NOW()),
    ('Encuesta de Seguimiento Académico', 'Encuesta para hacer seguimiento de la formación académica de los egresados', 'ACADEMIC', 'ACTIVE', '2024-01-15', '2024-06-30', NOW(), NOW()),
    ('Encuesta de Empleabilidad', 'Encuesta para evaluar la empleabilidad de los recién egresados', 'EMPLOYMENT', 'COMPLETED', '2023-01-01', '2023-12-31', NOW(), NOW());

-- Inserts para preguntas de la encuesta 1 (Satisfacción Laboral)
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (1, '¿Cuál es tu nivel de satisfacción con tu trabajo actual?', 'SCALE', true, NOW(), NOW()),
    (1, '¿En qué sector trabajas actualmente?', 'SINGLE_CHOICE', true, NOW(), NOW()),
    (1, '¿Qué aspectos valoras más de tu trabajo actual?', 'MULTIPLE_CHOICE', false, NOW(), NOW()),
    (1, '¿Recomendarías tu empresa actual a otros egresados?', 'YES_NO', true, NOW(), NOW()),
    (1, 'Comparte algún comentario adicional sobre tu experiencia laboral', 'TEXT', false, NOW(), NOW());

-- Inserts para preguntas de la encuesta 2 (Seguimiento Académico)
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (2, '¿Has realizado estudios de posgrado?', 'YES_NO', true, NOW(), NOW()),
    (2, '¿Qué tipo de estudios adicionales has realizado?', 'MULTIPLE_CHOICE', false, NOW(), NOW()),
    (2, '¿Cuánto inviertes mensualmente en capacitación?', 'NUMBER', false, NOW(), NOW());

-- Opciones para pregunta 1 (satisfacción - escala 1-5)
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
    (2, 'Servicios', 6, NOW(), NOW()),
    (2, 'Otro', 7, NOW(), NOW());

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

-- Opciones para pregunta 6 (estudios de posgrado)
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

-- Respuestas de graduados a encuestas
INSERT INTO graduate_survey_responses (survey_id, graduate_id, submitted_at, completed, created_date, modified_date)
VALUES
    (1, 1, '2024-02-15 10:30:00', true, NOW(), NOW()),
    (1, 2, '2024-02-16 14:20:00', true, NOW(), NOW()),
    (1, 3, '2024-02-17 09:45:00', true, NOW(), NOW()),
    (1, 4, '2024-02-18 16:10:00', true, NOW(), NOW()),
    (1, 5, '2024-02-19 11:25:00', true, NOW(), NOW()),
    (1, 6, '2024-02-20 13:40:00', true, NOW(), NOW()),
    (1, 7, '2024-02-21 15:55:00', true, NOW(), NOW()),
    (1, 8, '2024-02-22 08:30:00', true, NOW(), NOW()),
    (2, 1, '2024-03-01 10:00:00', true, NOW(), NOW()),
    (2, 2, '2024-03-02 14:30:00', true, NOW(), NOW()),
    (2, 3, '2024-03-03 16:45:00', true, NOW(), NOW());

-- Respuestas individuales a preguntas
INSERT INTO graduate_question_responses (graduate_survey_response_id, question_id, text_response, numeric_response, created_date, modified_date)
VALUES
    -- Respuestas de graduado 1 a encuesta 1
    (1, 1, NULL, 4, NOW(), NOW()),  -- Satisfacción: 4
    (1, 5, 'Me gusta mucho el ambiente de trabajo y las oportunidades de crecimiento', NULL, NOW(), NOW()),  -- Comentario
    
    -- Respuestas de graduado 2 a encuesta 1
    (2, 1, NULL, 5, NOW(), NOW()),  -- Satisfacción: 5
    (2, 5, 'Excelente empresa, muy recomendada para otros egresados', NULL, NOW(), NOW()),  -- Comentario
    
    -- Respuestas de graduado 3 a encuesta 1
    (3, 1, NULL, 3, NOW(), NOW()),  -- Satisfacción: 3
    (3, 5, 'Podría mejorar en algunos aspectos pero en general está bien', NULL, NOW(), NOW()),  -- Comentario
    
    -- Respuestas de graduado 4 a encuesta 1
    (4, 1, NULL, 4, NOW(), NOW()),  -- Satisfacción: 4
    (4, 5, 'Buen lugar para trabajar, con posibilidades de crecimiento', NULL, NOW(), NOW()),  -- Comentario
    
    -- Respuestas de graduado 5 a encuesta 1
    (5, 1, NULL, 2, NOW(), NOW()),  -- Satisfacción: 2
    (5, 5, 'No estoy muy contento con las condiciones actuales', NULL, NOW(), NOW()),  -- Comentario
    
    -- Respuestas de graduado 6 a encuesta 1
    (6, 1, NULL, 5, NOW(), NOW()),  -- Satisfacción: 5
    (6, 5, 'Perfecta empresa, gran ambiente y buenos beneficios', NULL, NOW(), NOW()),  -- Comentario
    
    -- Respuestas de graduado 7 a encuesta 1
    (7, 1, NULL, 4, NOW(), NOW()),  -- Satisfacción: 4
    (7, 5, 'Muy buena experiencia laboral hasta ahora', NULL, NOW(), NOW()),  -- Comentario
    
    -- Respuestas de graduado 8 a encuesta 1
    (8, 1, NULL, 3, NOW(), NOW()),  -- Satisfacción: 3
    (8, 5, 'Regular, hay cosas que pueden mejorar', NULL, NOW(), NOW()),  -- Comentario
    
    -- Respuestas a encuesta 2
    (9, 8, NULL, 500, NOW(), NOW()),   -- Inversión mensual en capacitación
    (10, 8, NULL, 800, NOW(), NOW()),  -- Inversión mensual en capacitación
    (11, 8, NULL, 300, NOW(), NOW());  -- Inversión mensual en capacitación

-- Opciones seleccionadas para preguntas de opción múltiple
INSERT INTO graduate_question_response_options (response_id, option_id, created_date)
VALUES
    -- Graduado 1: sector tecnología (opción 6), aspectos: ambiente + salario + crecimiento (opciones 13,14,15), recomendación: sí (opción 19)
    (1, 6, NOW()),   -- Tecnología
    (1, 13, NOW()),  -- Buen ambiente laboral
    (1, 14, NOW()),  -- Salario competitivo
    (1, 15, NOW()),  -- Oportunidades de crecimiento
    (1, 19, NOW()),  -- Sí recomendaría
    
    -- Graduado 2: sector finanzas (opción 7), aspectos: salario + beneficios (opciones 14,18), recomendación: sí (opción 19)
    (2, 7, NOW()),   -- Finanzas
    (2, 14, NOW()),  -- Salario competitivo
    (2, 18, NOW()),  -- Beneficios adicionales
    (2, 19, NOW()),  -- Sí recomendaría
    
    -- Graduado 3: sector tecnología (opción 6), aspectos: flexibilidad (opción 16), recomendación: no (opción 20)
    (3, 6, NOW()),   -- Tecnología
    (3, 16, NOW()),  -- Flexibilidad horaria
    (3, 20, NOW()),  -- No recomendaría
    
    -- Graduado 4: sector salud (opción 8), aspectos: ambiente + capacitación (opciones 13,17), recomendación: sí (opción 19)
    (4, 8, NOW()),   -- Salud
    (4, 13, NOW()),  -- Buen ambiente laboral
    (4, 17, NOW()),  -- Capacitación constante
    (4, 19, NOW()),  -- Sí recomendaría
    
    -- Graduado 5: sector manufactura (opción 10), aspectos: solo salario (opción 14), recomendación: no (opción 20)
    (5, 10, NOW()),  -- Manufactura
    (5, 14, NOW()),  -- Salario competitivo
    (5, 20, NOW()),  -- No recomendaría
    
    -- Graduado 6: sector tecnología (opción 6), aspectos: ambiente + crecimiento + beneficios (opciones 13,15,18), recomendación: sí (opción 19)
    (6, 6, NOW()),   -- Tecnología
    (6, 13, NOW()),  -- Buen ambiente laboral
    (6, 15, NOW()),  -- Oportunidades de crecimiento
    (6, 18, NOW()),  -- Beneficios adicionales
    (6, 19, NOW()),  -- Sí recomendaría
    
    -- Graduado 7: sector educación (opción 9), aspectos: ambiente + capacitación (opciones 13,17), recomendación: sí (opción 19)
    (7, 9, NOW()),   -- Educación
    (7, 13, NOW()),  -- Buen ambiente laboral
    (7, 17, NOW()),  -- Capacitación constante
    (7, 19, NOW()),  -- Sí recomendaría
    
    -- Graduado 8: sector servicios (opción 11), aspectos: flexibilidad (opción 16), recomendación: no (opción 20)
    (8, 11, NOW()),  -- Servicios
    (8, 16, NOW()),  -- Flexibilidad horaria
    (8, 20, NOW()),  -- No recomendaría
    
    -- Respuestas a encuesta 2
    (9, 21, NOW()),  -- Sí ha realizado estudios de posgrado
    (9, 23, NOW()),  -- Maestría
    (9, 27, NOW()),  -- Cursos online
    
    (10, 21, NOW()), -- Sí ha realizado estudios de posgrado
    (10, 24, NOW()), -- Doctorado
    
    (11, 22, NOW()); -- No ha realizado estudios de posgrado

-- ============================================
-- ENCUESTAS ADICIONALES PARA ESTADÍSTICAS
-- ============================================

-- Encuestas adicionales
INSERT INTO surveys (title, description, survey_type, status, start_date, end_date, created_date, modified_date)
VALUES
    ('Encuesta de Clima Laboral', 'Evaluación del ambiente de trabajo en las empresas donde laboran los egresados', 'EMPLOYMENT', 'ACTIVE', '2024-02-01', '2024-08-31', NOW(), NOW()),
    ('Encuesta de Competencias Digitales', 'Evaluación de habilidades tecnológicas de los egresados', 'SKILLS', 'COMPLETED', '2023-06-01', '2023-12-31', NOW(), NOW()),
    ('Encuesta de Emprendimiento', 'Evaluación de intenciones y actividades emprendedoras', 'ENTREPRENEURSHIP', 'ACTIVE', '2024-03-01', '2024-09-30', NOW(), NOW()),
    ('Encuesta de Salarios y Beneficios', 'Análisis de compensaciones en el mercado laboral', 'EMPLOYMENT', 'PAUSED', '2024-01-01', '2024-06-30', NOW(), NOW()),
    ('Encuesta de Educación Continua', 'Seguimiento de capacitación y desarrollo profesional', 'ACADEMIC', 'ACTIVE', '2024-02-15', '2024-12-31', NOW(), NOW());

-- Preguntas para Encuesta 4 (Clima Laboral) - IDs 9-15
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (4, '¿Cómo calificarías el ambiente de trabajo en tu empresa?', 'SCALE', true, NOW(), NOW()),
    (4, '¿Tu empresa ofrece trabajo remoto?', 'YES_NO', true, NOW(), NOW()),
    (4, '¿Qué modalidad de trabajo prefieres?', 'SINGLE_CHOICE', true, NOW(), NOW()),
    (4, '¿Qué aspectos del clima laboral consideras más importantes?', 'MULTIPLE_CHOICE', false, NOW(), NOW()),
    (4, '¿Cuántas horas trabajas en promedio por semana?', 'NUMBER', true, NOW(), NOW()),
    (4, 'Describe brevemente el aspecto que más te gusta de tu trabajo', 'TEXT', false, NOW(), NOW()),
    (4, '¿Recomendarías tu empresa como un buen lugar para trabajar?', 'YES_NO', true, NOW(), NOW());

-- Preguntas para Encuesta 5 (Competencias Digitales) - IDs 16-21
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (5, '¿Cómo evalúas tu nivel general de competencias digitales?', 'SCALE', true, NOW(), NOW()),
    (5, '¿Qué herramientas digitales utilizas frecuentemente en tu trabajo?', 'MULTIPLE_CHOICE', true, NOW(), NOW()),
    (5, '¿Has tomado cursos de tecnología en el último año?', 'YES_NO', true, NOW(), NOW()),
    (5, '¿Cuál es tu nivel de experiencia en programación?', 'SINGLE_CHOICE', true, NOW(), NOW()),
    (5, '¿Cuántas horas dedicas semanalmente a aprender nuevas tecnologías?', 'NUMBER', false, NOW(), NOW()),
    (5, 'Menciona la tecnología que más te gustaría aprender', 'TEXT', false, NOW(), NOW());

-- Preguntas para Encuesta 6 (Emprendimiento) - IDs 22-26
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (6, '¿Tienes intención de emprender en los próximos 2 años?', 'YES_NO', true, NOW(), NOW()),
    (6, '¿En qué sector te gustaría emprender?', 'SINGLE_CHOICE', false, NOW(), NOW()),
    (6, '¿Qué obstáculos consideras más importantes para emprender?', 'MULTIPLE_CHOICE', true, NOW(), NOW()),
    (6, '¿Cómo calificarías tu preparación para emprender?', 'SCALE', true, NOW(), NOW()),
    (6, 'Describe brevemente tu idea de negocio (si la tienes)', 'TEXT', false, NOW(), NOW());

-- Preguntas para Encuesta 7 (Salarios y Beneficios) - IDs 27-31
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (7, '¿Cuál es tu rango de salario mensual (en soles)?', 'SINGLE_CHOICE', true, NOW(), NOW()),
    (7, '¿Qué beneficios adicionales recibes?', 'MULTIPLE_CHOICE', false, NOW(), NOW()),
    (7, '¿Estás satisfecho con tu salario actual?', 'YES_NO', true, NOW(), NOW()),
    (7, '¿Con qué frecuencia recibes aumentos salariales?', 'SINGLE_CHOICE', true, NOW(), NOW()),
    (7, '¿Cuántos meses de gratificación recibes al año?', 'NUMBER', false, NOW(), NOW());

-- Preguntas para Encuesta 8 (Educación Continua) - IDs 32-36
INSERT INTO questions (survey_id, question_text, question_type, required, created_date, modified_date)
VALUES
    (8, '¿Qué tan importante consideras la educación continua?', 'SCALE', true, NOW(), NOW()),
    (8, '¿Qué tipo de capacitación has realizado recientemente?', 'MULTIPLE_CHOICE', true, NOW(), NOW()),
    (8, '¿Tu empresa financia tu capacitación?', 'YES_NO', true, NOW(), NOW()),
    (8, '¿Cuánto inviertes anualmente en tu desarrollo profesional?', 'SINGLE_CHOICE', true, NOW(), NOW()),
    (8, 'Menciona el curso o certificación que más te ha ayudado profesionalmente', 'TEXT', false, NOW(), NOW());

-- ============================================
-- OPCIONES PARA LAS NUEVAS PREGUNTAS
-- ============================================

-- Opciones para pregunta 9 (Ambiente de trabajo - escala) - IDs 28-32
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (9, '1 - Muy malo', 1, NOW(), NOW()),
    (9, '2 - Malo', 2, NOW(), NOW()),
    (9, '3 - Regular', 3, NOW(), NOW()),
    (9, '4 - Bueno', 4, NOW(), NOW()),
    (9, '5 - Excelente', 5, NOW(), NOW());

-- Opciones para pregunta 10 (Trabajo remoto) - IDs 33-34
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (10, 'Sí', 1, NOW(), NOW()),
    (10, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 11 (Modalidad preferida) - IDs 35-37
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (11, 'Presencial', 1, NOW(), NOW()),
    (11, 'Remoto', 2, NOW(), NOW()),
    (11, 'Híbrido', 3, NOW(), NOW());

-- Opciones para pregunta 12 (Aspectos clima laboral) - IDs 38-43
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (12, 'Comunicación efectiva', 1, NOW(), NOW()),
    (12, 'Respeto mutuo', 2, NOW(), NOW()),
    (12, 'Equilibrio vida-trabajo', 3, NOW(), NOW()),
    (12, 'Reconocimiento', 4, NOW(), NOW()),
    (12, 'Trabajo en equipo', 5, NOW(), NOW()),
    (12, 'Liderazgo efectivo', 6, NOW(), NOW());

-- Opciones para pregunta 15 (Recomendar empresa) - IDs 44-45
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (15, 'Sí', 1, NOW(), NOW()),
    (15, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 16 (Nivel competencias digitales) - IDs 46-50
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (16, '1 - Básico', 1, NOW(), NOW()),
    (16, '2 - Intermedio bajo', 2, NOW(), NOW()),
    (16, '3 - Intermedio', 3, NOW(), NOW()),
    (16, '4 - Avanzado', 4, NOW(), NOW()),
    (16, '5 - Experto', 5, NOW(), NOW());

-- Opciones para pregunta 17 (Herramientas digitales) - IDs 51-58
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (17, 'Microsoft Office', 1, NOW(), NOW()),
    (17, 'Google Workspace', 2, NOW(), NOW()),
    (17, 'Herramientas de diseño (Photoshop, Figma)', 3, NOW(), NOW()),
    (17, 'Lenguajes de programación', 4, NOW(), NOW()),
    (17, 'Bases de datos', 5, NOW(), NOW()),
    (17, 'Herramientas de análisis (Excel, Tableau)', 6, NOW(), NOW()),
    (17, 'Plataformas de gestión (Jira, Trello)', 7, NOW(), NOW()),
    (17, 'Redes sociales profesionales', 8, NOW(), NOW());

-- Opciones para pregunta 18 (Cursos tecnología) - IDs 59-60
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (18, 'Sí', 1, NOW(), NOW()),
    (18, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 19 (Nivel programación) - IDs 61-65
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (19, 'Sin experiencia', 1, NOW(), NOW()),
    (19, 'Básico', 2, NOW(), NOW()),
    (19, 'Intermedio', 3, NOW(), NOW()),
    (19, 'Avanzado', 4, NOW(), NOW()),
    (19, 'Experto', 5, NOW(), NOW());

-- Opciones para pregunta 22 (Intención emprender) - IDs 66-67
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (22, 'Sí', 1, NOW(), NOW()),
    (22, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 23 (Sector emprendimiento) - IDs 68-74
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (23, 'Tecnología e innovación', 1, NOW(), NOW()),
    (23, 'Comercio y retail', 2, NOW(), NOW()),
    (23, 'Servicios profesionales', 3, NOW(), NOW()),
    (23, 'Alimentos y bebidas', 4, NOW(), NOW()),
    (23, 'Turismo y entretenimiento', 5, NOW(), NOW()),
    (23, 'Salud y bienestar', 6, NOW(), NOW()),
    (23, 'Educación y capacitación', 7, NOW(), NOW());

-- Opciones para pregunta 24 (Obstáculos emprendimiento) - IDs 75-80
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (24, 'Falta de capital inicial', 1, NOW(), NOW()),
    (24, 'Falta de experiencia', 2, NOW(), NOW()),
    (24, 'Temor al fracaso', 3, NOW(), NOW()),
    (24, 'Falta de contactos', 4, NOW(), NOW()),
    (24, 'Burocracia y trámites', 5, NOW(), NOW()),
    (24, 'Competencia del mercado', 6, NOW(), NOW());

-- Opciones para pregunta 25 (Preparación para emprender) - IDs 81-85
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (25, '1 - Muy poco preparado', 1, NOW(), NOW()),
    (25, '2 - Poco preparado', 2, NOW(), NOW()),
    (25, '3 - Moderadamente preparado', 3, NOW(), NOW()),
    (25, '4 - Bien preparado', 4, NOW(), NOW()),
    (25, '5 - Muy bien preparado', 5, NOW(), NOW());

-- Opciones para pregunta 27 (Rango salario) - IDs 86-91
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (27, 'Menos de S/. 1,500', 1, NOW(), NOW()),
    (27, 'S/. 1,500 - S/. 2,500', 2, NOW(), NOW()),
    (27, 'S/. 2,501 - S/. 4,000', 3, NOW(), NOW()),
    (27, 'S/. 4,001 - S/. 6,000', 4, NOW(), NOW()),
    (27, 'S/. 6,001 - S/. 10,000', 5, NOW(), NOW()),
    (27, 'Más de S/. 10,000', 6, NOW(), NOW());

-- Opciones para pregunta 28 (Beneficios adicionales) - IDs 92-98
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (28, 'Seguro médico privado', 1, NOW(), NOW()),
    (28, 'Bonos de productividad', 2, NOW(), NOW()),
    (28, 'Capacitación pagada', 3, NOW(), NOW()),
    (28, 'Horario flexible', 4, NOW(), NOW()),
    (28, 'Días adicionales de vacaciones', 5, NOW(), NOW()),
    (28, 'Transporte o movilidad', 6, NOW(), NOW()),
    (28, 'Alimentación', 7, NOW(), NOW());

-- Opciones para pregunta 29 (Satisfacción salario) - IDs 99-100
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (29, 'Sí', 1, NOW(), NOW()),
    (29, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 30 (Frecuencia aumentos) - IDs 101-105
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (30, 'Cada 6 meses', 1, NOW(), NOW()),
    (30, 'Anualmente', 2, NOW(), NOW()),
    (30, 'Cada 2 años', 3, NOW(), NOW()),
    (30, 'Rara vez o nunca', 4, NOW(), NOW()),
    (30, 'Solo por promociones', 5, NOW(), NOW());

-- Opciones para pregunta 32 (Importancia educación continua) - IDs 106-110
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (32, '1 - Nada importante', 1, NOW(), NOW()),
    (32, '2 - Poco importante', 2, NOW(), NOW()),
    (32, '3 - Moderadamente importante', 3, NOW(), NOW()),
    (32, '4 - Muy importante', 4, NOW(), NOW()),
    (32, '5 - Extremadamente importante', 5, NOW(), NOW());

-- Opciones para pregunta 33 (Tipo capacitación) - IDs 111-117
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (33, 'Cursos técnicos especializados', 1, NOW(), NOW()),
    (33, 'Certificaciones profesionales', 2, NOW(), NOW()),
    (33, 'Diplomados', 3, NOW(), NOW()),
    (33, 'Talleres y seminarios', 4, NOW(), NOW()),
    (33, 'Cursos de idiomas', 5, NOW(), NOW()),
    (33, 'Habilidades blandas', 6, NOW(), NOW()),
    (33, 'Ninguna', 7, NOW(), NOW());

-- Opciones para pregunta 34 (Empresa financia) - IDs 118-119
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (34, 'Sí', 1, NOW(), NOW()),
    (34, 'No', 2, NOW(), NOW());

-- Opciones para pregunta 35 (Inversión anual desarrollo) - IDs 120-125
INSERT INTO question_options (question_id, option_text, order_number, created_date, modified_date)
VALUES
    (35, 'S/. 0 - S/. 500', 1, NOW(), NOW()),
    (35, 'S/. 501 - S/. 1,500', 2, NOW(), NOW()),
    (35, 'S/. 1,501 - S/. 3,000', 3, NOW(), NOW()),
    (35, 'S/. 3,001 - S/. 5,000', 4, NOW(), NOW()),
    (35, 'S/. 5,001 - S/. 10,000', 5, NOW(), NOW()),
    (35, 'Más de S/. 10,000', 6, NOW(), NOW());

-- ============================================
-- RESPUESTAS DE GRADUADOS A NUEVAS ENCUESTAS
-- ============================================

-- Respuestas a encuesta 4 (Clima Laboral) - IDs 12-27
INSERT INTO graduate_survey_responses (survey_id, graduate_id, submitted_at, completed, created_date, modified_date)
VALUES
    (4, 1, '2024-03-10 09:15:00', true, NOW(), NOW()),
    (4, 2, '2024-03-11 14:30:00', true, NOW(), NOW()),
    (4, 3, '2024-03-12 10:45:00', true, NOW(), NOW()),
    (4, 4, '2024-03-13 16:20:00', true, NOW(), NOW()),
    (4, 5, '2024-03-14 11:10:00', true, NOW(), NOW()),
    (4, 6, '2024-03-15 13:25:00', true, NOW(), NOW()),
    (4, 7, '2024-03-16 15:40:00', true, NOW(), NOW()),
    (4, 8, '2024-03-17 08:55:00', true, NOW(), NOW()),
    (4, 9, '2024-03-18 12:30:00', true, NOW(), NOW()),
    (4, 10, '2024-03-19 14:15:00', true, NOW(), NOW()),
    (4, 11, '2024-03-20 16:45:00', true, NOW(), NOW()),
    (4, 12, '2024-03-21 10:20:00', true, NOW(), NOW()),
    (4, 13, '2024-03-22 13:50:00', true, NOW(), NOW()),
    (4, 14, '2024-03-23 15:10:00', true, NOW(), NOW()),
    (4, 15, '2024-03-24 09:35:00', true, NOW(), NOW()),
    (4, 16, '2024-03-25 11:40:00', true, NOW(), NOW());

-- Respuestas a encuesta 5 (Competencias Digitales) - IDs 28-39
INSERT INTO graduate_survey_responses (survey_id, graduate_id, submitted_at, completed, created_date, modified_date)
VALUES
    (5, 1, '2023-08-15 10:00:00', true, NOW(), NOW()),
    (5, 2, '2023-08-16 14:20:00', true, NOW(), NOW()),
    (5, 3, '2023-08-17 09:30:00', true, NOW(), NOW()),
    (5, 4, '2023-08-18 16:45:00', true, NOW(), NOW()),
    (5, 5, '2023-08-19 11:15:00', true, NOW(), NOW()),
    (5, 6, '2023-08-20 13:40:00', true, NOW(), NOW()),
    (5, 7, '2023-08-21 15:25:00', true, NOW(), NOW()),
    (5, 8, '2023-08-22 08:50:00', true, NOW(), NOW()),
    (5, 9, '2023-08-23 12:10:00', true, NOW(), NOW()),
    (5, 10, '2023-08-24 14:35:00', true, NOW(), NOW()),
    (5, 11, '2023-08-25 16:20:00', true, NOW(), NOW()),
    (5, 12, '2023-08-26 10:45:00', true, NOW(), NOW());

-- Respuestas a encuesta 6 (Emprendimiento) - IDs 40-47
INSERT INTO graduate_survey_responses (survey_id, graduate_id, submitted_at, completed, created_date, modified_date)
VALUES
    (6, 1, '2024-04-01 09:20:00', true, NOW(), NOW()),
    (6, 3, '2024-04-02 14:10:00', true, NOW(), NOW()),
    (6, 5, '2024-04-03 10:35:00', true, NOW(), NOW()),
    (6, 7, '2024-04-04 15:50:00', true, NOW(), NOW()),
    (6, 9, '2024-04-05 11:25:00', true, NOW(), NOW()),
    (6, 11, '2024-04-06 13:15:00', true, NOW(), NOW()),
    (6, 13, '2024-04-07 16:30:00', true, NOW(), NOW()),
    (6, 15, '2024-04-08 08:45:00', true, NOW(), NOW());

-- ============================================
-- RESPUESTAS INDIVIDUALES A PREGUNTAS NUEVAS
-- ============================================

-- Respuestas numéricas y de texto para encuesta 4 (Clima Laboral)
INSERT INTO graduate_question_responses (graduate_survey_response_id, question_id, text_response, numeric_response, created_date, modified_date)
VALUES
    -- Ambiente de trabajo (pregunta 9)
    (12, 9, NULL, 4, NOW(), NOW()),
    (13, 9, NULL, 5, NOW(), NOW()),
    (14, 9, NULL, 3, NOW(), NOW()),
    (15, 9, NULL, 4, NOW(), NOW()),
    (16, 9, NULL, 2, NOW(), NOW()),
    (17, 9, NULL, 5, NOW(), NOW()),
    (18, 9, NULL, 4, NOW(), NOW()),
    (19, 9, NULL, 3, NOW(), NOW()),
    (20, 9, NULL, 4, NOW(), NOW()),
    (21, 9, NULL, 5, NOW(), NOW()),
    (22, 9, NULL, 3, NOW(), NOW()),
    (23, 9, NULL, 4, NOW(), NOW()),
    (24, 9, NULL, 4, NOW(), NOW()),
    (25, 9, NULL, 3, NOW(), NOW()),
    (26, 9, NULL, 5, NOW(), NOW()),
    (27, 9, NULL, 4, NOW(), NOW()),
    
    -- Horas de trabajo semanales (pregunta 13)
    (12, 13, NULL, 45, NOW(), NOW()),
    (13, 13, NULL, 40, NOW(), NOW()),
    (14, 13, NULL, 48, NOW(), NOW()),
    (15, 13, NULL, 42, NOW(), NOW()),
    (16, 13, NULL, 50, NOW(), NOW()),
    (17, 13, NULL, 38, NOW(), NOW()),
    (18, 13, NULL, 44, NOW(), NOW()),
    (19, 13, NULL, 46, NOW(), NOW()),
    (20, 13, NULL, 40, NOW(), NOW()),
    (21, 13, NULL, 43, NOW(), NOW()),
    (22, 13, NULL, 47, NOW(), NOW()),
    (23, 13, NULL, 41, NOW(), NOW()),
    (24, 13, NULL, 45, NOW(), NOW()),
    (25, 13, NULL, 39, NOW(), NOW()),
    (26, 13, NULL, 42, NOW(), NOW()),
    (27, 13, NULL, 44, NOW(), NOW()),
    
    -- Comentarios sobre trabajo (pregunta 14)
    (12, 14, 'Me gusta la flexibilidad horaria y el buen ambiente', NULL, NOW(), NOW()),
    (13, 14, 'Excelente equipo de trabajo y proyectos desafiantes', NULL, NOW(), NOW()),
    (14, 14, 'Podría mejorar la comunicación entre áreas', NULL, NOW(), NOW()),
    (15, 14, 'Buenos beneficios y oportunidades de crecimiento', NULL, NOW(), NOW()),
    (16, 14, 'Mucha carga de trabajo pero buena compensación', NULL, NOW(), NOW()),
    (17, 14, 'Ambiente muy colaborativo y aprendizaje constante', NULL, NOW(), NOW()),
    (18, 14, 'Me gusta la cultura de innovación de la empresa', NULL, NOW(), NOW()),
    (19, 14, 'Trabajo interesante pero podría ser mejor pagado', NULL, NOW(), NOW()),
    (20, 14, 'Excelente balance vida-trabajo', NULL, NOW(), NOW()),
    (21, 14, 'Muy buen liderazgo y dirección clara', NULL, NOW(), NOW()),
    
    -- Respuestas para competencias digitales (encuesta 5)
    (28, 16, NULL, 4, NOW(), NOW()),
    (29, 16, NULL, 3, NOW(), NOW()),
    (30, 16, NULL, 5, NOW(), NOW()),
    (31, 16, NULL, 4, NOW(), NOW()),
    (32, 16, NULL, 3, NOW(), NOW()),
    (33, 16, NULL, 4, NOW(), NOW()),
    (34, 16, NULL, 5, NOW(), NOW()),
    (35, 16, NULL, 2, NOW(), NOW()),
    (36, 16, NULL, 4, NOW(), NOW()),
    (37, 16, NULL, 3, NOW(), NOW()),
    (38, 16, NULL, 4, NOW(), NOW()),
    (39, 16, NULL, 5, NOW(), NOW()),
    
    -- Horas semanales aprendiendo tecnología (pregunta 20)
    (28, 20, NULL, 5, NOW(), NOW()),
    (29, 20, NULL, 3, NOW(), NOW()),
    (30, 20, NULL, 8, NOW(), NOW()),
    (31, 20, NULL, 4, NOW(), NOW()),
    (32, 20, NULL, 2, NOW(), NOW()),
    (33, 20, NULL, 6, NOW(), NOW()),
    (34, 20, NULL, 10, NOW(), NOW()),
    (35, 20, NULL, 1, NOW(), NOW()),
    (36, 20, NULL, 4, NOW(), NOW()),
    (37, 20, NULL, 3, NOW(), NOW()),
    (38, 20, NULL, 5, NOW(), NOW()),
    (39, 20, NULL, 7, NOW(), NOW()),
    
    -- Tecnología que quieren aprender (pregunta 21)
    (28, 21, 'Inteligencia Artificial y Machine Learning', NULL, NOW(), NOW()),
    (29, 21, 'Desarrollo móvil con React Native', NULL, NOW(), NOW()),
    (30, 21, 'Cloud Computing y AWS', NULL, NOW(), NOW()),
    (31, 21, 'Ciencia de Datos y Python', NULL, NOW(), NOW()),
    (32, 21, 'Ciberseguridad y ethical hacking', NULL, NOW(), NOW()),
    (33, 21, 'Blockchain y criptomonedas', NULL, NOW(), NOW()),
    (34, 21, 'DevOps y automatización', NULL, NOW(), NOW()),
    (35, 21, 'Diseño UX/UI avanzado', NULL, NOW(), NOW()),
    
    -- Respuestas para emprendimiento
    (40, 25, NULL, 3, NOW(), NOW()),
    (41, 25, NULL, 4, NOW(), NOW()),
    (42, 25, NULL, 2, NOW(), NOW()),
    (43, 25, NULL, 4, NOW(), NOW()),
    (44, 25, NULL, 3, NOW(), NOW()),
    (45, 25, NULL, 5, NOW(), NOW()),
    (46, 25, NULL, 2, NOW(), NOW()),
    (47, 25, NULL, 4, NOW(), NOW()),
    
    -- Ideas de negocio (pregunta 26)
    (40, 26, 'Plataforma de e-learning para profesionales técnicos', NULL, NOW(), NOW()),
    (41, 26, 'App de delivery especializada en comida saludable', NULL, NOW(), NOW()),
    (42, 26, 'Consultora en transformación digital para PYMEs', NULL, NOW(), NOW()),
    (43, 26, 'Startup de IoT para agricultura inteligente', NULL, NOW(), NOW());

-- ============================================
-- OPCIONES SELECCIONADAS PARA NUEVAS ENCUESTAS
-- ============================================

INSERT INTO graduate_question_response_options (response_id, option_id, created_date)
VALUES
    -- Encuesta 4 (Clima Laboral) - Respuestas de opciones múltiples
    
    -- Graduado 1: trabajo remoto sí, modalidad híbrida, aspectos importantes: comunicación + respeto + equilibrio, recomienda sí
    (12, 33, NOW()),  -- Trabajo remoto: Sí
    (12, 37, NOW()),  -- Modalidad: Híbrido
    (12, 38, NOW()),  -- Comunicación efectiva
    (12, 39, NOW()),  -- Respeto mutuo
    (12, 40, NOW()),  -- Equilibrio vida-trabajo
    (12, 44, NOW()),  -- Recomienda: Sí
    
    -- Graduado 2: trabajo remoto sí, modalidad remota, aspectos: equilibrio + reconocimiento + trabajo en equipo, recomienda sí
    (13, 33, NOW()),  -- Trabajo remoto: Sí
    (13, 36, NOW()),  -- Modalidad: Remoto
    (13, 40, NOW()),  -- Equilibrio vida-trabajo
    (13, 41, NOW()),  -- Reconocimiento
    (13, 42, NOW()),  -- Trabajo en equipo
    (13, 44, NOW()),  -- Recomienda: Sí
    
    -- Graduado 3: trabajo remoto no, modalidad presencial, aspectos: liderazgo + comunicación, recomienda no
    (14, 34, NOW()),  -- Trabajo remoto: No
    (14, 35, NOW()),  -- Modalidad: Presencial
    (14, 38, NOW()),  -- Comunicación efectiva
    (14, 43, NOW()),  -- Liderazgo efectivo
    (14, 45, NOW()),  -- Recomienda: No
    
    -- Más respuestas para clima laboral...
    (15, 33, NOW()),  -- Trabajo remoto: Sí
    (15, 37, NOW()),  -- Modalidad: Híbrido
    (15, 39, NOW()),  -- Respeto mutuo
    (15, 41, NOW()),  -- Reconocimiento
    (15, 44, NOW()),  -- Recomienda: Sí
    
    (16, 34, NOW()),  -- Trabajo remoto: No
    (16, 35, NOW()),  -- Modalidad: Presencial
    (16, 38, NOW()),  -- Comunicación efectiva
    (16, 45, NOW()),  -- Recomienda: No
    
    (17, 33, NOW()),  -- Trabajo remoto: Sí
    (17, 36, NOW()),  -- Modalidad: Remoto
    (17, 40, NOW()),  -- Equilibrio vida-trabajo
    (17, 42, NOW()),  -- Trabajo en equipo
    (17, 43, NOW()),  -- Liderazgo efectivo
    (17, 44, NOW()),  -- Recomienda: Sí
    
    -- Encuesta 5 (Competencias Digitales)
    
    -- Graduado 1: herramientas múltiples, cursos sí, nivel programación avanzado
    (28, 51, NOW()),  -- Microsoft Office
    (28, 54, NOW()),  -- Lenguajes de programación
    (28, 55, NOW()),  -- Bases de datos
    (28, 59, NOW()),  -- Cursos tecnología: Sí
    (28, 64, NOW()),  -- Nivel programación: Avanzado
    
    -- Graduado 2: office + análisis, no cursos, nivel básico
    (29, 51, NOW()),  -- Microsoft Office
    (29, 56, NOW()),  -- Herramientas de análisis
    (29, 60, NOW()),  -- Cursos tecnología: No
    (29, 62, NOW()),  -- Nivel programación: Básico
    
    -- Graduado 3: programación + bases datos + gestión, cursos sí, nivel experto
    (30, 54, NOW()),  -- Lenguajes de programación
    (30, 55, NOW()),  -- Bases de datos
    (30, 57, NOW()),  -- Herramientas de gestión
    (30, 59, NOW()),  -- Cursos tecnología: Sí
    (30, 65, NOW()),  -- Nivel programación: Experto
    
    -- Más respuestas para competencias digitales...
    (31, 52, NOW()),  -- Google Workspace
    (31, 53, NOW()),  -- Herramientas de diseño
    (31, 59, NOW()),  -- Cursos tecnología: Sí
    (31, 63, NOW()),  -- Nivel programación: Intermedio
    
    (32, 51, NOW()),  -- Microsoft Office
    (32, 58, NOW()),  -- Redes sociales profesionales
    (32, 60, NOW()),  -- Cursos tecnología: No
    (32, 61, NOW()),  -- Nivel programación: Sin experiencia
    
    -- Encuesta 6 (Emprendimiento)
    
    -- Graduado 1: intención sí, sector tecnología, obstáculos: capital + experiencia
    (40, 66, NOW()),  -- Intención emprender: Sí
    (40, 68, NOW()),  -- Sector: Tecnología e innovación
    (40, 75, NOW()),  -- Obstáculo: Falta de capital inicial
    (40, 76, NOW()),  -- Obstáculo: Falta de experiencia
    
    -- Graduado 3: intención sí, sector servicios, obstáculos: capital + contactos + burocracia
    (41, 66, NOW()),  -- Intención emprender: Sí
    (41, 70, NOW()),  -- Sector: Servicios profesionales
    (41, 75, NOW()),  -- Obstáculo: Falta de capital inicial
    (41, 78, NOW()),  -- Obstáculo: Falta de contactos
    (41, 79, NOW()),  -- Obstáculo: Burocracia y trámites
    
    -- Graduado 5: intención no
    (42, 67, NOW()),  -- Intención emprender: No
    (42, 77, NOW()),  -- Obstáculo: Temor al fracaso
    (42, 80, NOW()),  -- Obstáculo: Competencia del mercado
    
    -- Más respuestas de emprendimiento...
    (43, 66, NOW()),  -- Intención emprender: Sí
    (43, 71, NOW()),  -- Sector: Alimentos y bebidas
    (43, 75, NOW()),  -- Obstáculo: Falta de capital inicial
    (43, 76, NOW()),  -- Obstáculo: Falta de experiencia
    
    (44, 66, NOW()),  -- Intención emprender: Sí
    (44, 69, NOW()),  -- Sector: Comercio y retail
    (44, 75, NOW()),  -- Obstáculo: Falta de capital inicial
    (44, 78, NOW()),  -- Obstáculo: Falta de contactos
    
    (45, 66, NOW()),  -- Intención emprender: Sí
    (45, 68, NOW()),  -- Sector: Tecnología e innovación
    (45, 76, NOW()),  -- Obstáculo: Falta de experiencia
    (45, 79, NOW()),  -- Obstáculo: Burocracia y trámites
    
    (46, 67, NOW()),  -- Intención emprender: No
    (46, 77, NOW()),  -- Obstáculo: Temor al fracaso
    
    (47, 66, NOW()),  -- Intención emprender: Sí
    (47, 72, NOW()),  -- Sector: Turismo y entretenimiento
    (47, 75, NOW()),  -- Obstáculo: Falta de capital inicial
    (47, 80, NOW());  -- Obstáculo: Competencia del mercado
