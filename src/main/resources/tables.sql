
DROP TABLE IF EXISTS graduates;

DROP TABLE IF EXISTS users;

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
    );







