#!/bin/bash

# Colores para mensajes
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # Sin color

# Versión del proyecto
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
PROJECT_NAME=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)

# Directorio de exportación
EXPORT_DIR="export-${PROJECT_NAME}-${VERSION}"

# Limpiar y crear directorio de exportación
rm -rf ${EXPORT_DIR}
mkdir -p ${EXPORT_DIR}

# 1. Generar JAR
echo -e "${YELLOW}Generando JAR...${NC}"
mvn clean package -DskipTests
JAR_FILE=$(find target -name "*.jar")
cp ${JAR_FILE} ${EXPORT_DIR}/

# 2. Exportar imagen Docker
echo -e "${YELLOW}Exportando imagen Docker...${NC}"
docker build -t ${PROJECT_NAME}:${VERSION} .
docker save -o ${EXPORT_DIR}/${PROJECT_NAME}-${VERSION}.tar ${PROJECT_NAME}:${VERSION}

# 3. Copiar archivos de configuración importantes
echo -e "${YELLOW}Copiando archivos de configuración...${NC}"
cp Dockerfile docker-compose.yml README.md ${EXPORT_DIR}/
cp src/main/resources/application.properties ${EXPORT_DIR}/

# 4. Generar documentación de la API (opcional, requiere Swagger)
# Descomentar si tienes Swagger configurado
# mvn swagger-maven-plugin:generate

# 5. Comprimir todo en un archivo
echo -e "${YELLOW}Comprimiendo archivos...${NC}"
tar -czvf ${PROJECT_NAME}-${VERSION}-export.tar.gz ${EXPORT_DIR}

# Limpiar directorio temporal
rm -rf ${EXPORT_DIR}

echo -e "${GREEN}Exportación completada:${NC}"
echo "- JAR generado"
echo "- Imagen Docker exportada"
echo "- Archivos de configuración incluidos"
echo "- Paquete comprimido: ${PROJECT_NAME}-${VERSION}-export.tar.gz" 