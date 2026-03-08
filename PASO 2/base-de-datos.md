# ERP Iglesias - Inspección de Base de Datos

Este documento muestra la verificación de los contenedores Docker y la estructura de la base de datos **PostgreSQL** utilizada en el proyecto **ERP Iglesias**.

---

# 1. Contenedores Docker en ejecución

```bash
docker ps
```

```
CONTAINER ID   IMAGE                   COMMAND                  CREATED             STATUS             PORTS                                         NAMES
3ceb0b0ed00c   erp_iglesias-frontend   "/docker-entrypoint.…"   About an hour ago   Up About an hour   0.0.0.0:4200->80/tcp, [::]:4200->80/tcp       erp_iglesias-frontend-1
bf501762364e   erp_iglesias-backend    "java -jar /app/app.…"   About an hour ago   Up About an hour   0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp   erp_iglesias-backend-1
400a30b4d85f   postgres:16             "docker-entrypoint.s…"   About an hour ago   Up About an hour   0.0.0.0:5432->5432/tcp, [::]:5432->5432/tcp   erp_iglesias-db-1
```

Contenedores del sistema:

- **Frontend:** Angular (puerto 4200)
- **Backend:** Spring Boot (puerto 8080)
- **Base de datos:** PostgreSQL 16 (puerto 5432)

---

# 2. Acceso al contenedor de PostgreSQL

```bash
docker exec -it erp_iglesias-db-1 bash
```

Luego ingresar al cliente de PostgreSQL:

```bash
psql -U postgres
```

---

# 3. Bases de datos disponibles

```sql
\l
```

```
Name         | Owner
-------------|----------
iglesiadmin  | postgres
postgres     | postgres
template0    | postgres
template1    | postgres
```

Conectarse a la base de datos principal:

```sql
\c iglesiadmin
```

---

# 4. Tablas del sistema

```sql
\dt
```

```
Schema | Name
-------|-------------
public | churches
public | courses
public | enrollments
public | offerings
public | payments
public | people
public | users
```

Total de tablas: **7**

---

# 5. Estructura de las tablas

## Tabla: churches

```sql
\d churches
```

| Columna | Tipo |
|------|------|
| id | bigint |
| address | varchar(255) |
| created_at | timestamp |
| name | varchar(255) |

Relaciones:

- `people.church_id`
- `courses.church_id`

---

## Tabla: courses

```sql
\d courses
```

| Columna | Tipo |
|------|------|
| id | bigint |
| active | boolean |
| created_at | timestamp |
| description | varchar(255) |
| name | varchar(255) |
| price | numeric(12,2) |
| church_id | bigint |

Relaciones:

- FK → `churches(id)`
- Referenciada por `enrollments`

---

## Tabla: enrollments

```sql
\d enrollments
```

| Columna | Tipo |
|------|------|
| id | bigint |
| created_at | timestamp |
| payment_id | bigint |
| status | varchar |
| course_id | bigint |
| person_id | bigint |

Restricción de estado:

```
PENDIENTE
PAGADA
```

Relaciones:

- FK → `courses`
- FK → `people`

---

## Tabla: offerings

```sql
\d offerings
```

| Columna | Tipo |
|------|------|
| id | bigint |
| amount | numeric(12,2) |
| concept | varchar(255) |
| created_at | timestamp |
| payment_id | bigint |
| status | varchar |
| person_id | bigint |

Restricción de estado:

```
PENDIENTE
REGISTRADA
```

Relación:

- FK → `people`

---

## Tabla: payments

```sql
\d payments
```

| Columna | Tipo |
|------|------|
| id | bigint |
| amount | numeric(12,2) |
| attempts | integer |
| created_at | timestamp |
| reference_id | bigint |
| status | varchar |
| type | varchar |
| updated_at | timestamp |

Restricciones:

**status**

```
INICIADO
CONFIRMADO
FALLIDO
```

**type**

```
INSCRIPCION_CURSO
OFRENDA
```

---

## Tabla: people

```sql
\d people
```

| Columna | Tipo |
|------|------|
| id | bigint |
| created_at | timestamp |
| document | varchar(255) |
| email | varchar(255) |
| first_name | varchar(255) |
| last_name | varchar(255) |
| phone | varchar(255) |
| church_id | bigint |

Relaciones:

- FK → `churches`
- Referenciada por:
  - `offerings`
  - `enrollments`

---

## Tabla: users

```sql
\d users
```

| Columna | Tipo |
|------|------|
| id | bigint |
| active | boolean |
| created_at | timestamp |
| email | varchar(255) |
| password_hash | varchar(255) |
| role | varchar |

Restricción de roles:

```
ADMIN
CLIENT
```

---

# 6. Resumen del modelo de datos

Entidades principales del sistema:

- **churches** → Iglesias
- **people** → Personas pertenecientes a una iglesia
- **courses** → Cursos ofrecidos
- **enrollments** → Inscripciones a cursos
- **offerings** → Registro de ofrendas
- **payments** → Gestión de pagos
- **users** → Usuarios del sistema

Relaciones principales:

- Una **iglesia** tiene muchas **personas** y **cursos**
- Una **persona** puede realizar **ofrendas** o **inscribirse a cursos**
- Las **inscripciones** y **ofrendas** pueden tener **pagos asociados**