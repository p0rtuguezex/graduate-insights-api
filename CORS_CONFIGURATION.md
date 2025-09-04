# 🌐 Configuración de CORS - Graduate Insights API

## 📋 Descripción

Cross-Origin Resource Sharing (CORS) permite que aplicaciones web ejecutándose en un dominio accedan a recursos de otro dominio. Esta configuración es esencial para que el frontend pueda comunicarse con la API.

## ✅ Implementación Actual

### 1. **Configuración Global en SecurityConfig**

La configuración de CORS se maneja centralmente en `SecurityConfig.java` usando variables de entorno:

```java
@Value("${cors.allowed-origins:http://localhost:3000}")
private String allowedOrigins;

@Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS,PATCH}")
private String allowedMethods;

@Value("${cors.allowed-headers:*}")
private String allowedHeaders;

@Value("${cors.exposed-headers:Authorization,Content-Type}")
private String exposedHeaders;

@Value("${cors.allow-credentials:true}")
private boolean allowCredentials;

@Value("${cors.max-age:3600}")
private long maxAge;
```

### 2. **Variables de Entorno Soportadas**

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `CORS_ALLOWED_ORIGINS` | Dominios permitidos (separados por coma) | `http://localhost:3000` |
| `CORS_ALLOWED_METHODS` | Métodos HTTP permitidos | `GET,POST,PUT,DELETE,OPTIONS,PATCH` |
| `CORS_ALLOWED_HEADERS` | Headers permitidos | `*` |
| `CORS_EXPOSED_HEADERS` | Headers expuestos al cliente | `Authorization,Content-Type,X-Total-Count` |
| `CORS_ALLOW_CREDENTIALS` | Permitir credenciales | `true` |
| `CORS_MAX_AGE` | Tiempo de caché en segundos | `3600` |

### 3. **Configuración en application.yml**

```yaml
cors:
  allowed-origins: ${CORS_ALLOWED_ORIGINS:http://localhost:3000}
  allowed-methods: ${CORS_ALLOWED_METHODS:GET,POST,PUT,DELETE,OPTIONS,PATCH}
  allowed-headers: ${CORS_ALLOWED_HEADERS:*}
  exposed-headers: ${CORS_EXPOSED_HEADERS:Authorization,Content-Type,X-Total-Count}
  allow-credentials: ${CORS_ALLOW_CREDENTIALS:true}
  max-age: ${CORS_MAX_AGE:3600}
```

## 🚀 Uso en Diferentes Entornos

### Desarrollo Local
```bash
# Para múltiples puertos de desarrollo
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:3001,http://127.0.0.1:3000
```

### Staging
```bash
# Para entorno de pruebas
CORS_ALLOWED_ORIGINS=https://staging.graduate-insights.com,http://localhost:3000
```

### Producción
```bash
# Solo dominio de producción
CORS_ALLOWED_ORIGINS=https://graduate-insights.com,https://www.graduate-insights.com
```

### Docker Compose
```yaml
environment:
  - CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:3001
  - CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS,PATCH
  - CORS_ALLOW_CREDENTIALS=true
```

## 🔧 Configuraciones Específicas

### Para APIs Públicas
```bash
CORS_ALLOWED_ORIGINS=*
CORS_ALLOW_CREDENTIALS=false
```

### Para Aplicaciones Móviles
```bash
CORS_ALLOWED_ORIGINS=capacitor://localhost,ionic://localhost,http://localhost
```

### Para Microservicios
```bash
CORS_ALLOWED_ORIGINS=http://service1.local,http://service2.local,http://gateway.local
```

## 🛠️ Troubleshooting

### Problema: "CORS policy has blocked the request"

**Solución 1: Verificar origen**
```bash
# Ver configuración actual
docker exec graduate-insights-api env | grep CORS

# Logs de CORS
docker logs graduate-insights-api | grep CORS
```

**Solución 2: Agregar nuevo origen**
```bash
# Agregar nuevo dominio
CORS_ALLOWED_ORIGINS=http://localhost:3000,https://nuevo-dominio.com
```

### Problema: "Credentials not allowed"

**Solución:**
```bash
CORS_ALLOW_CREDENTIALS=true
# Y asegurar que el frontend envíe: credentials: 'include'
```

### Problema: "Method not allowed"

**Solución:**
```bash
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD
```

### Problema: "Header not allowed"

**Solución:**
```bash
# Permitir headers específicos
CORS_ALLOWED_HEADERS=Authorization,Content-Type,X-Custom-Header

# O permitir todos (no recomendado para producción)
CORS_ALLOWED_HEADERS=*
```

## 🔍 Debugging

### Ver configuración actual
```bash
# En el contenedor Docker
docker exec graduate-insights-api env | grep CORS

# En logs de la aplicación
docker logs graduate-insights-api | grep -i cors
```

### Probar CORS manualmente
```bash
# Usando curl para preflight request
curl -X OPTIONS \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  -v \
  http://localhost:8080/graduate-insights/v1/api/auth/login
```

### JavaScript para probar CORS
```javascript
// En la consola del navegador
fetch('http://localhost:8080/graduate-insights/v1/api/auth/me', {
  method: 'GET',
  credentials: 'include',
  headers: {
    'Authorization': 'Bearer your-token-here'
  }
})
.then(response => console.log('CORS OK:', response))
.catch(error => console.error('CORS Error:', error));
```

## 🔒 Seguridad

### Mejores Prácticas

1. **Nunca usar `*` en producción**
```bash
# ❌ Malo
CORS_ALLOWED_ORIGINS=*

# ✅ Bueno
CORS_ALLOWED_ORIGINS=https://mi-app.com
```

2. **Especificar métodos necesarios**
```bash
# ✅ Solo los métodos que necesitas
CORS_ALLOWED_METHODS=GET,POST,PUT
```

3. **Limitar headers expuestos**
```bash
# ✅ Solo headers necesarios
CORS_EXPOSED_HEADERS=Authorization,Content-Type
```

4. **Configurar Max-Age apropiado**
```bash
# ✅ Balance entre rendimiento y seguridad
CORS_MAX_AGE=1800  # 30 minutos
```

## 📝 Logs de Referencia

### Logs de Éxito
```
INFO  - Configurando CORS con orígenes permitidos: http://localhost:3000
DEBUG - Origen CORS permitido: http://localhost:3000
INFO  - CORS configurado exitosamente - Métodos: [GET, POST, PUT, DELETE, OPTIONS, PATCH], Credenciales: true, MaxAge: 3600s
```

### Logs de Error
```
WARN  - CORS: Request from unauthorized origin: http://malicious-site.com
ERROR - CORS: Method HEAD not allowed for origin: http://localhost:3000
```

## ✅ Testing

### Verificar configuración
```bash
# Construir y ejecutar
docker-compose up --build

# Probar desde el navegador
# Abrir: http://localhost:3000
# Hacer requests a: http://localhost:8080/graduate-insights/v1/api/
```

Con esta configuración, CORS se maneja de forma centralizada y flexible usando variables de entorno! 🎉
