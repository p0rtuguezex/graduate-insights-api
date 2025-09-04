#!/bin/bash

echo "=== DIAGNÓSTICO DE CONFIGURACIÓN DE CORREO EN DOCKER ==="
echo ""

echo "1. Verificando variables de entorno de correo:"
echo "MAIL_HOST: $MAIL_HOST"
echo "MAIL_PORT: $MAIL_PORT"  
echo "MAIL_USERNAME: $MAIL_USERNAME"
echo "MAIL_PASSWORD: [OCULTO - $(echo $MAIL_PASSWORD | wc -c) caracteres]"
echo "MAIL_SMTP_AUTH: $MAIL_SMTP_AUTH"
echo "MAIL_STARTTLS_ENABLE: $MAIL_STARTTLS_ENABLE"
echo ""

echo "2. Probando conectividad DNS:"
nslookup $MAIL_HOST
echo ""

echo "3. Probando conectividad de red al puerto SMTP:"
nc -zv $MAIL_HOST $MAIL_PORT
echo ""

echo "4. Verificando logs de la aplicación (últimas 50 líneas):"
tail -50 /var/log/graduate-insights.log 2>/dev/null || echo "Log file not found"
echo ""

echo "5. Verificando conectividad SSL/TLS:"
openssl s_client -connect $MAIL_HOST:$MAIL_PORT -starttls smtp -verify_return_error < /dev/null
echo ""

echo "=== FIN DEL DIAGNÓSTICO ==="
