# 📧 Solución de Problemas con Correo Electrónico en Docker

## 🔍 Problema Identificado

La aplicación no envía correos cuando se ejecuta en Docker debido a:

1. **Configuración hardcodeada** en `application.yml`
2. **Falta de variables de entorno** para configuración de correo
3. **Timeouts insuficientes** en contenedores
4. **Falta de logging detallado** para debugging

## ✅ Soluciones Implementadas

### 1. **Variables de Entorno en application.yml**
```yaml
mail:
  host: ${MAIL_HOST:smtp.gmail.com}
  port: ${MAIL_PORT:587}
  username: ${MAIL_USERNAME:garlee016@gmail.com}
  password: ${MAIL_PASSWORD:azejzfwfshvwdiok}
  properties:
    mail:
      smtp:
        auth: ${MAIL_SMTP_AUTH:true}
        timeout: ${MAIL_TIMEOUT:10000}
        starttls:
          enable: ${MAIL_STARTTLS_ENABLE:true}
        ssl:
          trust: ${MAIL_SSL_TRUST:smtp.gmail.com}
```

### 2. **Docker Compose con Configuración de Correo**
```yaml
environment:
  # Configuración de correo electrónico
  - MAIL_HOST=smtp.gmail.com
  - MAIL_PORT=587
  - MAIL_USERNAME=garlee016@gmail.com
  - MAIL_PASSWORD=azejzfwfshvwdiok
  - MAIL_SMTP_AUTH=true
  - MAIL_STARTTLS_ENABLE=true
  - MAIL_TIMEOUT=10000
  - MAIL_SSL_TRUST=smtp.gmail.com
```

### 3. **Logging Mejorado**
- Logs detallados del proceso de envío
- Debugging de configuración SMTP
- Manejo de excepciones mejorado

### 4. **Herramientas de Diagnóstico**
- Script de diagnóstico de conectividad
- Herramientas de red en el contenedor
- Variables de entorno de ejemplo

## 🚀 Pasos para Aplicar la Solución

### 1. Reconstruir la Imagen
```bash
# Construir imagen local
docker build -t graduate-insights-api-fixed .

# O usar la imagen de Docker Hub actualizada
docker build -t unudev25/graduate-insights-api:v1.0.1 .
docker push unudev25/graduate-insights-api:v1.0.1
```

### 2. Actualizar Docker Compose
```bash
# Usar el docker-compose.yml actualizado
docker-compose down
docker-compose up --build -d

# O usar la versión de Docker Hub
docker-compose -f docker-compose.dockerhub.yml up -d
```

### 3. Verificar Configuración
```bash
# Verificar variables de entorno en el contenedor
docker exec graduate-insights-api env | grep MAIL

# Ver logs de la aplicación
docker logs graduate-insights-api -f

# Ejecutar diagnóstico de correo (si está disponible)
docker exec graduate-insights-api /app/scripts/diagnose-mail.sh
```

## 🔧 Troubleshooting Adicional

### Problema: "Authentication failed"
```bash
# Verificar credenciales
echo $MAIL_USERNAME
echo $MAIL_PASSWORD | wc -c

# Para Gmail, usar App Password en lugar de contraseña normal
# https://support.google.com/accounts/answer/185833
```

### Problema: "Connection timeout"
```bash
# Verificar conectividad de red
docker exec graduate-insights-api nc -zv smtp.gmail.com 587

# Aumentar timeout
MAIL_TIMEOUT=15000
```

### Problema: "SSL/TLS errors"
```bash
# Verificar configuración SSL
MAIL_SSL_TRUST=smtp.gmail.com
MAIL_STARTTLS_ENABLE=true

# Probar conexión SSL
docker exec graduate-insights-api openssl s_client -connect smtp.gmail.com:587 -starttls smtp
```

### Problema: "DNS resolution failed"
```bash
# Verificar resolución DNS
docker exec graduate-insights-api nslookup smtp.gmail.com

# Usar IP directa si es necesario
MAIL_HOST=142.250.191.109  # IP de smtp.gmail.com
```

## 📝 Configuraciones Alternativas

### Para Otros Proveedores de Correo

#### Outlook/Hotmail
```yaml
MAIL_HOST=smtp-mail.outlook.com
MAIL_PORT=587
MAIL_STARTTLS_ENABLE=true
```

#### Yahoo
```yaml
MAIL_HOST=smtp.mail.yahoo.com
MAIL_PORT=587
MAIL_STARTTLS_ENABLE=true
```

#### SMTP Personalizado
```yaml
MAIL_HOST=mail.tudominio.com
MAIL_PORT=465
MAIL_STARTTLS_ENABLE=false
MAIL_SSL_ENABLE=true
```

## 🔍 Logs a Revisar

### Logs de Éxito
```
INFO  - Intentando enviar correo a: user@email.com con asunto: Código de Recuperación
INFO  - Correo enviado exitosamente a: user@email.com
```

### Logs de Error Común
```
ERROR - Error detallado al enviar correo a user@email.com: 
        AuthenticationFailedException: 535-5.7.8 Username and Password not accepted
```

## ✅ Verificación Final

1. **Construir imagen actualizada**
2. **Configurar variables de entorno**
3. **Ejecutar contenedor**
4. **Probar envío de correo desde la API**
5. **Revisar logs para confirmar éxito**

Con estas modificaciones, el envío de correos debería funcionar correctamente en el entorno Docker.
