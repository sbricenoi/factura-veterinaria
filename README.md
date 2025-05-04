# Proyecto Veterinaria - API REST con HATEOAS

## Descripción

Este proyecto implementa una API REST para gestión de servicios y facturas, utilizando Spring Boot y HATEOAS (Hypermedia as the Engine of Application State).

## Características de HATEOAS

La API ahora soporta HATEOAS, lo que significa que cada respuesta incluye hipervínculos que permiten la navegación dinámica entre recursos.

### Endpoints de Servicios

- `POST /api/servicio`: Crear un nuevo servicio
  - Respuesta incluye enlaces a:
    - `self`: Enlace al servicio recién creado
    - `servicios`: Enlace a la lista de servicios

- `GET /api/servicio`: Listar todos los servicios
  - Cada servicio incluye enlaces a:
    - `self`: Enlace al servicio específico
    - `servicios`: Enlace a la lista de servicios

- `GET /api/servicio/{id}`: Obtener un servicio específico
  - Respuesta incluye enlaces a:
    - `self`: Enlace al servicio actual
    - `servicios`: Enlace a la lista de servicios

### Endpoints de Facturas

- `POST /api/factura`: Crear una nueva factura
  - Respuesta incluye enlaces a:
    - `self`: Enlace a la factura recién creada
    - `pagar`: Enlace para pagar la factura
    - `facturas`: Enlace a la lista de facturas

- `GET /api/factura`: Listar todas las facturas
  - Cada factura incluye enlaces a:
    - `self`: Enlace a la factura específica
    - `pagar`: Enlace para pagar la factura
    - `facturas`: Enlace a la lista de facturas

- `GET /api/factura/{id}`: Obtener una factura específica
  - Respuesta incluye enlaces a:
    - `self`: Enlace a la factura actual
    - `pagar`: Enlace para pagar la factura
    - `facturas`: Enlace a la lista de facturas

- `PUT /api/factura/{id}/pagar`: Pagar una factura
  - Respuesta incluye enlaces a:
    - `self`: Enlace a la factura pagada
    - `facturas`: Enlace a la lista de facturas

## Beneficios de HATEOAS

- Descubrimiento dinámico de recursos
- Mayor flexibilidad en la navegación de la API
- Desacoplamiento entre cliente y servidor
- Facilita la evolución de la API sin cambios en el cliente

## Tecnologías Utilizadas

- Spring Boot
- Spring HATEOAS
- Spring Data JPA
- Oracle Database

## Requisitos

- Java 17+
- Maven
- Oracle Database

## Instalación

1. Clonar el repositorio
2. Configurar la conexión a la base de datos en `application.properties`
3. Ejecutar `mvn spring-boot:run`

## Contribuciones

Las contribuciones son bienvenidas. Por favor, leer las guías de contribución antes de enviar un pull request.

## Pruebas Unitarias

El proyecto utiliza JUnit 5 y Mockito para realizar pruebas unitarias de los controladores y servicios.

### Herramientas de Pruebas

- **JUnit 5**: Framework principal para pruebas unitarias
- **Mockito**: Librería para crear mocks y simular comportamientos
- **Spring Boot Test**: Soporte para pruebas de componentes de Spring

### Tipos de Pruebas

Las pruebas unitarias cubren los siguientes escenarios:

#### Controlador de Facturas (`FacturaControllerTest`)

- Agregar servicio exitosamente
- Manejar datos inválidos al agregar servicio
- Listar servicios
- Obtener servicio por ID
- Manejar servicio no encontrado
- Crear factura
- Obtener factura
- Pagar factura
- Listar facturas

### Ejecutar Pruebas

Para ejecutar las pruebas, utiliza el siguiente comando Maven:

```bash
mvn test
```

### Cobertura de Código

Se recomienda utilizar herramientas como JaCoCo para medir la cobertura de código de las pruebas.

## Requisitos para Desarrollo y Pruebas

- Java 17+
- Maven
- JDK configurado
- IDE con soporte para JUnit (recomendado: IntelliJ IDEA, Eclipse)

## Dockerización

### Requisitos Previos
- Docker
- Docker Compose
- Maven

### Generar JAR y Construir Imagen

Para generar el JAR y construir la imagen Docker, ejecuta:

```bash
# Dar permisos de ejecución al script
chmod +x build-and-run.sh

# Construir y ejecutar
./build-and-run.sh
```

### Comandos Docker Manuales

Construcción de imagen:
```bash
# Generar JAR
mvn clean package -DskipTests

# Construir imagen
docker build -t veterinaria-app .
```

Ejecución del contenedor:
```bash
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f app
```

### Configuraciones

- **Dockerfile**: Configura la construcción de la imagen
- **docker-compose.yml**: Define servicios de aplicación y base de datos
- **application.properties**: Configuraciones de conexión

### Puertos

- Aplicación: `8080`
- Base de datos Oracle: `1521`

### Volúmenes

- Datos de base de datos persistentes en volumen Docker

## Exportación del Proyecto

### Exportar JAR y Contenedor

Para exportar el proyecto completo, incluyendo JAR y imagen Docker:

```bash
# Dar permisos al script
chmod +x export-project.sh

# Ejecutar exportación
./export-project.sh
```

#### Contenido del Paquete Exportado

El script generará:
- JAR ejecutable
- Imagen Docker comprimida
- Archivos de configuración
- Documentación

### Métodos Alternativos de Exportación

#### Generar JAR Manualmente
```bash
mvn clean package -DskipTests
```

#### Exportar Imagen Docker
```bash
# Construir imagen
docker build -t veterinaria-app .

# Exportar a archivo tar
docker save -o veterinaria-app.tar veterinaria-app

# Importar en otro sistema
docker load -i veterinaria-app.tar
```

### Requisitos para Exportación

- Maven
- Docker
- Permisos de ejecución de script 