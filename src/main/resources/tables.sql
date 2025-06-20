SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS graduates;
DROP TABLE IF EXISTS director;
DROP TABLE IF EXISTS employers;
DROP TABLE IF EXISTS education_centers;
DROP TABLE IF EXISTS jobs_offers;
DROP TABLE IF EXISTS event_types;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS surveys;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS question_options;
DROP TABLE IF EXISTS graduate_survey_responses;
DROP TABLE IF EXISTS graduate_question_responses;
DROP TABLE IF EXISTS graduate_question_response_options;
SET FOREIGN_KEY_CHECKS = 1;


  CREATE TABLE users(
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
        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );

   CREATE TABLE graduates(
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     user_id BIGINT UNIQUE,
     fecha_inicio DATE,
     fecha_fin DATE,
     cv TEXT,
     created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY(user_id) REFERENCES users(id)
         ON DELETE CASCADE
         ON UPDATE CASCADE
    );

CREATE TABLE director(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNIQUE,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY(user_id) REFERENCES users(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);

   CREATE TABLE employers(
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       user_id BIGINT UNIQUE,
       ruc VARCHAR(12),
       razon_social VARCHAR(150),
       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY(user_id) REFERENCES users(id)
           ON DELETE CASCADE
           ON UPDATE CASCADE
   );

    CREATE TABLE jobs (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      compania VARCHAR(255),
      cargo VARCHAR(255),
      modalidad VARCHAR(100),
      estado CHAR(1),
      fecha_inicio DATE,
      fecha_fin DATE,
      graduate_id BIGINT,
      created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (graduate_id) REFERENCES graduates(id)
          ON DELETE CASCADE
          ON UPDATE CASCADE
    );

CREATE TABLE education_centers(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estado CHAR(1),
    nombre VARCHAR(100),
    direccion VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event_types(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    estado CHAR(1),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE jobs_offers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255),
  link VARCHAR(255),
  descripcion TEXT,
  estado CHAR(1),
  employer_id BIGINT,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (employer_id) REFERENCES employers(id)
);

-- Tabla de encuestas
CREATE TABLE IF NOT EXISTS surveys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    survey_type VARCHAR(50) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de preguntas
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_id BIGINT NOT NULL,
    question_text VARCHAR(500) NOT NULL,
    question_type VARCHAR(50) NOT NULL,
    required BOOLEAN DEFAULT false,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id) ON DELETE CASCADE
);

-- Tabla de opciones de respuesta
CREATE TABLE IF NOT EXISTS question_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT NOT NULL,
    option_text VARCHAR(255) NOT NULL,
    order_number INT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- Tabla de respuestas de graduados a encuestas
CREATE TABLE IF NOT EXISTS graduate_survey_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    survey_id BIGINT NOT NULL,
    graduate_id BIGINT NOT NULL,
    current_position VARCHAR(255),
    current_company VARCHAR(255),
    current_salary_range DECIMAL(10,2),
    location VARCHAR(255),
    submitted_at TIMESTAMP,
    completed BOOLEAN DEFAULT false,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (survey_id) REFERENCES surveys(id),
    FOREIGN KEY (graduate_id) REFERENCES graduates(id),
    UNIQUE KEY unique_survey_graduate (survey_id, graduate_id)
);

-- Tabla de respuestas individuales a preguntas
CREATE TABLE IF NOT EXISTS graduate_question_responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    graduate_survey_response_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    text_response TEXT,
    numeric_response INT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (graduate_survey_response_id) REFERENCES graduate_survey_responses(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);

-- Tabla de relación entre respuestas y opciones seleccionadas
CREATE TABLE IF NOT EXISTS graduate_question_response_options (
    response_id BIGINT NOT NULL,
    option_id BIGINT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (response_id, option_id),
    FOREIGN KEY (response_id) REFERENCES graduate_question_responses(id) ON DELETE CASCADE,
    FOREIGN KEY (option_id) REFERENCES question_options(id)
);