#!/bin/bash

# Script para remover anotaciones @CrossOrigin redundantes de los controladores
# ya que ahora usamos configuración global de CORS

echo "Removiendo anotaciones @CrossOrigin redundantes de los controladores..."

CONTROLLERS_PATH="src/main/java/pe/com/graduate/insights/api/infrastructure/controller"

# Buscar todos los archivos de controladores que tengan @CrossOrigin
find "$CONTROLLERS_PATH" -name "*.java" -exec grep -l "@CrossOrigin" {} \; | while read file; do
    echo "Procesando: $file"
    
    # Crear backup del archivo
    cp "$file" "$file.backup"
    
    # Remover línea con @CrossOrigin
    sed -i '/@CrossOrigin/d' "$file"
    
    echo "  - Removida anotación @CrossOrigin"
done

echo "¡Proceso completado!"
echo ""
echo "NOTA: Se crearon archivos .backup por seguridad."
echo "Si todo funciona correctamente, puedes eliminarlos con:"
echo "find $CONTROLLERS_PATH -name '*.backup' -delete"
