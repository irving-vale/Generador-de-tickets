# ✈️ Generador de Tickets

Backend REST API desarrollado con **Java 21 y Spring Boot 3** para la administración de vuelos, hoteles, reservaciones y paquetes turísticos.

El proyecto implementa una arquitectura en capas, separación mediante DTOs, persistencia con JPA/Hibernate, seguridad con Spring Security y un pipeline de integración continua automatizado con GitHub Actions y SonarCloud.

---

# 📌 Descripción del Proyecto

**Generador de Tickets** es un sistema backend para una agencia de turismo que permite administrar:

- ✈️ Vuelos
- 🏨 Hoteles
- 🎟️ Tickets
- 📅 Reservaciones
- 🌎 Tours personalizados
- 👤 Clientes

El objetivo principal del sistema es gestionar paquetes turísticos combinando vuelos y hospedajes, aplicando reglas de negocio como cálculo de precios, impuestos y estadísticas de clientes.

Actualmente el proyecto se encuentra en fase de evolución continua, incorporando nuevas funcionalidades mientras se aplican buenas prácticas de ingeniería de software.

---

# 🚀 Características Principales

## Gestión de vuelos

- Consulta de vuelos disponibles.
- Filtrado por:
    - Origen.
    - Destino.
    - Rango de precios.
- Paginación.
- Ordenamiento dinámico.
- Validación defensiva de parámetros.

## Gestión de hoteles

- Administración de hoteles.
- Manejo de precios y calificaciones.
- Integración con reservaciones.

## Gestión de reservaciones

- Creación y administración de reservas.
- Cálculo automático de días de hospedaje.
- Asociación con clientes y hoteles.

## Gestión de tickets

- Creación de tickets de vuelo.
- Asociación con vuelos y clientes.
- Cálculo de precios finales.

## Gestión de tours

- Creación de paquetes turísticos.
- Asociación de múltiples vuelos y reservaciones.
- Cálculo automático:

```
Precio final = Precio base + 25% impuesto
```

## Clientes

- Registro de clientes.
- Seguimiento de estadísticas:
    - Total de vuelos.
    - Total de hospedajes.
    - Total de tours.

---

# 🏗️ Arquitectura

El proyecto utiliza una arquitectura en capas (**Layered Architecture**) separando responsabilidades:

```
src/main/java

├── api
│   ├── controllers
│   └── models
│       ├── request
│       └── responses
│
├── domain
│   ├── entities
│   ├── repositories
│   └── mappers
│
├── infraestructure
│   ├── services
│   ├── abstract_services
│   ├── exceptions
│   ├── security
│   └── helper
│
└── util
```

---

# 🧠 Principios de Diseño Aplicados

## DTO Pattern

Las entidades JPA no son expuestas directamente por la API.

Se utilizan:

- Request DTOs.
- Response DTOs.
- MapStruct para conversiones.

Beneficios:

- Menor acoplamiento.
- Mayor control sobre contratos API.
- Facilidad de evolución.

---

## Separation of Concerns

Cada capa tiene una responsabilidad definida:

| Capa | Responsabilidad |
|-|-|
| Controller | Entrada HTTP y respuestas |
| Service | Lógica de negocio |
| Repository | Acceso a datos |
| Domain | Modelo del negocio |
| Mapper | Conversión Entity ↔ DTO |

---

## Defensive Validation

El proyecto aplica validaciones como:

- Null checks.
- Validación de rangos.
- Validación de strings.
- Normalización de datos.
- Fail Fast.

---

# 🛠️ Tecnologías Utilizadas

## Backend

| Tecnología | Versión |
|-|-|
| Java | 21 |
| Spring Boot | 3.4.5 |
| Spring Data JPA | - |
| Hibernate | - |
| Spring Security | - |
| MapStruct | 1.6.0 |
| Lombok | 1.18.34 |

---

## Base de Datos

| Tecnología | Versión |
|-|-|
| PostgreSQL | 15.2 |
| Flyway | - |

---

## Testing

| Tecnología |
|-|
| JUnit Jupiter 5 |
| Mockito |
| JaCoCo |

---

## DevOps / Calidad

| Tecnología |
|-|
| Maven |
| Docker Compose |
| GitHub Actions |
| SonarCloud |

---

# 🔐 Seguridad

Actualmente implementado:

- Spring Security.
- HTTP Basic Authentication.
- Autorización mediante roles.
- PasswordEncoder con DelegatingPasswordEncoder.
- Protección mediante `@PreAuthorize`.

Ejemplo:

```java
@PreAuthorize("hasAuthority('read')")
public ResponseEntity<?> findAll()
```

## Próximas mejoras

- JWT Authentication.
- Refresh Token.
- Seguridad completa para todos los módulos.
- OAuth2 / OpenID Connect.

---

# 🗄️ Base de Datos

El proyecto utiliza migraciones con Flyway.

Migraciones actuales:

```
V1__Create_schema.sql
V2__Insert_data.sql
V4__create_user_table.sql
V6__insert_data_user_table.sql
```

Principales entidades:

```
Customer

Fly

Hotel

Ticket

Reservation

Tour

User
```

---

# 🧪 Testing

El proyecto incluye pruebas unitarias y de integración.

Actualmente cuenta con:

- Tests de servicios.
- Tests de controladores.
- Tests de mappers.
- Mockito para aislamiento de dependencias.
- JaCoCo para cobertura.

Ejecutar pruebas:

```bash
./mvnw test
```

---

# ⚙️ Ejecución Local

## Requisitos

Necesitas:

- Java 21.
- Docker.
- Git.

---

## 1. Clonar repositorio

```bash
git clone <repository-url>

cd Generador-de-tickets
```

---

## 2. Configurar variables de entorno

Crear archivo:

```
.env
```

Ejemplo:

```env
DB_USER=usuario
DB_PASSWORD=password
```

---

## 3. Levantar PostgreSQL

```bash
docker compose up -d
```

---

## 4. Ejecutar aplicación

Linux / macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

---

# 🔄 CI Pipeline

El proyecto cuenta con integración continua mediante GitHub Actions.

Workflow:

```
Push / Pull Request

        ↓

Checkout Repository

        ↓

Setup Java 21

        ↓

Maven Build

        ↓

Unit Tests

        ↓

JaCoCo Coverage

        ↓

SonarCloud Analysis
```

Actualmente el pipeline valida:

- Compilación.
- Tests.
- Calidad de código.
- Cobertura.

---

# 📊 Calidad de Código

Herramientas utilizadas:

## SonarCloud

Analiza:

- Code smells.
- Bugs potenciales.
- Vulnerabilidades.
- Calidad del código.

## Refactorización continua

Ejemplos aplicados:

- Eliminación de código duplicado.
- Mejora de streams Java.
- Correcciones sugeridas por SonarCloud.
- Mejora de estructura del código.

---

# 🤖 AI Assisted Development

El proyecto utiliza un flujo de desarrollo asistido por IA basado en:

## Specification Driven Development (SDD)

Proceso:

```
Analizar proyecto

        ↓

Generar especificación

        ↓

Revisar diseño

        ↓

Implementar cambios

        ↓

Ejecutar pruebas

        ↓

Validar calidad
```

Herramientas utilizadas:

| Herramienta | Uso |
|-|-|
| Graphify | Análisis estructural del código |
| OpenCode | Contexto y generación de especificaciones |
| Aider | Implementación basada en specs |
| ChatGPT | Revisión de arquitectura y diseño |

---

# 📚 Documentación Técnica

Documentación adicional:

- [Project Overview](docs/PROJECT_OVERVIEW.md)
- [Architecture Decisions](docs/DECISIONS.md)
- [AI Development Workflow](docs/ai-workflow.md)
- [Progress Tracking](docs/PROGRESS.md)

---

# 🗺️ Roadmap

## Seguridad

- [ ] Implementar JWT.
- [ ] Implementar Refresh Tokens.
- [ ] Proteger todos los endpoints.

## Calidad

- [ ] Agregar Bean Validation.
- [ ] Mejorar cobertura de pruebas.
- [ ] Implementar Testcontainers.

## API

- [ ] Documentación OpenAPI / Swagger.
- [ ] Mejorar manejo de errores.
- [ ] Agregar pruebas de contrato.

## Infraestructura

- [ ] Dockerizar aplicación Spring Boot.
- [ ] Agregar métricas con Spring Actuator.
- [ ] Implementar cache con Redis.

---

# 👨‍💻 Autor

## José Irving Lozada Valencia

Backend Software Engineer

Especializado en:

- Java.
- Spring Boot.
- SQL.
- Arquitectura Backend.
- Cloud & AI Assisted Development.
