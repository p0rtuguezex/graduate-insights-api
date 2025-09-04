# Configuración CORS - Graduate Insights API

## Resumen de Cambios Implementados

### ✅ Configuración CORS Global Exitosa

Se ha implementado con éxito una configuración CORS global que permite la comunicación entre el frontend Nuxt.js/Vue.js y la API Spring Boot.

### 🔧 Cambios Realizados

#### 1. Eliminación de @CrossOrigin en Controladores
- **Archivos afectados**: 22+ controladores
- **Acción**: Removidas todas las anotaciones `@CrossOrigin` hardcodeadas
- **Comando utilizado**: 
  ```powershell
  Get-ChildItem -Path "src\main\java" -Recurse -Filter "*.java" | 
  ForEach-Object { (Get-Content $_.FullName) -replace '@CrossOrigin.*', '' | Set-Content $_.FullName }
  ```

#### 2. Configuración Global en SecurityConfig.java
- **Archivo**: `src/main/java/pe/com/graduate/insights/api/infrastructure/config/SecurityConfig.java`
- **Cambios principales**:
  - Configuración CORS centralizada con variables de entorno
  - Permitir peticiones OPTIONS para preflight
  - Configuración de headers y métodos permitidos

#### 3. Actualización del Filtro JWT
- **Archivo**: `src/main/java/pe/com/graduate/insights/api/infrastructure/security/JwtAuthenticationFilter.java`
- **Cambios**:
  - Saltar filtro JWT para peticiones OPTIONS
  - Actualizar rutas públicas para coincidir con la estructura real de la API

#### 4. Variables de Entorno en application.yml
```yaml
cors:
  allowed-origins: ${CORS_ALLOWED_ORIGINS:http://localhost:3000}
  allowed-methods: ${CORS_ALLOWED_METHODS:GET,POST,PUT,DELETE,OPTIONS,PATCH}
  allowed-headers: ${CORS_ALLOWED_HEADERS:Content-Type,Authorization}
  exposed-headers: ${CORS_EXPOSED_HEADERS:Authorization,Content-Type,X-Total-Count}
  allow-credentials: ${CORS_ALLOW_CREDENTIALS:true}
  max-age: ${CORS_MAX_AGE:3600}
```

### 🎯 Resultado Final

✅ **Peticiones OPTIONS (preflight) funcionando correctamente**
```http
HTTP/1.1 200 
Access-Control-Allow-Origin: http://localhost:3000
Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS,PATCH
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Allow-Credentials: true
Access-Control-Max-Age: 3600
```

### 🔑 Configuración de Producción

Para producción, configurar las siguientes variables de entorno:

```bash
# Frontend domain
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com

# Solo métodos necesarios
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS

# Headers específicos requeridos
CORS_ALLOWED_HEADERS=Content-Type,Authorization,X-Requested-With

# Headers que el frontend puede leer
CORS_EXPOSED_HEADERS=Authorization,Content-Type,X-Total-Count

# Permitir cookies/credentials si es necesario
CORS_ALLOW_CREDENTIALS=true

# Cache preflight por 1 hora
CORS_MAX_AGE=3600
```

### 🧪 Pruebas

**Comando de prueba para verificar CORS:**
```bash
curl -X OPTIONS "http://localhost:8080/graduate-insights/v1/api/auth/login" \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type,Authorization" \
  -i
```

**Respuesta esperada:** HTTP 200 con headers CORS apropiados.

### 🚀 Beneficios de esta Implementación

1. **Centralizada**: Una sola configuración CORS para toda la aplicación
2. **Configurable**: Variables de entorno para diferentes ambientes
3. **Segura**: Solo orígenes específicos permitidos
4. **Flexible**: Fácil de modificar sin cambios de código
5. **Compatible**: Funciona con Nuxt.js, Vue.js, React, Angular, etc.

### 🛠 Solución de Problemas

Si CORS sigue fallando:

1. **Verificar variables de entorno** están configuradas correctamente
2. **Comprobar orden de filtros** en Spring Security
3. **Revisar logs** para errores de configuración
4. **Validar rutas** que coincidan con las reales de la API

### 📝 Notas Técnicas

- El filtro JWT ahora ignora peticiones OPTIONS automáticamente
- Spring Security procesa CORS antes que la autenticación
- La configuración es compatible con Spring Boot 3.4.1 y Spring Security 6.x
- Las peticiones preflight se manejan correctamente sin llegar a los controladores
