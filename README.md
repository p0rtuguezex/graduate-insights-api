# Graduate Insights API

API backend para el sistema Graduate Insights desarrollado con Spring Boot.

## Desarrollo Local

Para ejecutar en desarrollo local (buildea la imagen):

```bash
docker-compose up --build
```

## Producción

Para ejecutar en producción usando la imagen de Docker Hub:

```bash
docker-compose -f docker-compose.prod.yml up
```

## Configuración

Las variables de entorno están configuradas en cada docker-compose según el ambiente.

### Desarrollo
- Base de datos: MySQL local en puerto 3306
- API: Puerto 8080
- CORS: Configurado para localhost:3000

### Producción
- Usa imagen: `unudev25/graduate-insights-api:latest`
- Configuración optimizada para producción
- CORS: Configurado para dominios de producción
