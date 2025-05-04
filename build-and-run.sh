#!/bin/bash

# Limpiar y compilar el proyecto
echo "Limpiando y compilando el proyecto..."
mvn clean package -DskipTests

# Construir imagen Docker
echo "Construyendo imagen Docker..."
docker build -t veterinaria-app .

# Ejecutar contenedor
echo "Iniciando contenedor Docker..."
docker-compose up -d

# Mostrar logs
echo "Mostrando logs de la aplicación..."
docker-compose logs -f app 