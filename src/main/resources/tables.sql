SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS graduates;
DROP TABLE IF EXISTS employers;
DROP TABLE IF EXISTS education_centers;
DROP TABLE IF EXISTS jobs_offers;
DROP TABLE IF EXISTS users;
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
    estado VARCHAR(100),
    nombre VARCHAR(100),
    direccion VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE jobs_offers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255),
  link VARCHAR(255),
  descripcion TEXT,
  estado VARCHAR(50),
  employer_id BIGINT,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (employer_id) REFERENCES employers(id)
);