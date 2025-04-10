CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    fecha_nacimiento DATE,
    genero VARCHAR(20),
    correo VARCHAR(100),
    dni VARCHAR(20),
    celular VARCHAR(20),
    contrasena VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );
