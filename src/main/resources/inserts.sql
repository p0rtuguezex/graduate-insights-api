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
