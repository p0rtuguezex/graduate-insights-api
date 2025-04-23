DELETE FROM users;
 INSERT INTO users (nombres, apellidos, fecha_nacimiento, genero, correo, dni, celular, contrasena, created_at, updated_at)
 VALUES
     ('Juan', 'Pérez', '1990-05-10', 'M', 'juan.perez@example.com', '12345678', '987654321', 'pass123', NOW(), NOW()),
     ('Lucía', 'Gómez', '1995-08-22', 'F', 'lucia.gomez@example.com', '87654321', '912345678', 'secure456', NOW(), NOW()),
     ('Carlos', 'Ramírez', '1988-11-15', 'M', 'carlos.ramirez@example.com', '11223344', '998877665', 'abc123', NOW(), NOW());