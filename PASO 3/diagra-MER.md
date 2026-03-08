# Modelo de Datos - ERP Iglesias

Este documento describe las **entidades, atributos y relaciones** del modelo de datos del sistema **ERP Iglesias**.

---

# Entidades del Sistema

## 1. Church (Iglesia)

**Tabla:** `churches`

### Atributos

| Campo | Tipo |
|------|------|
| id | PK |
| name | string |
| address | string |
| created_at | timestamp |

---

## 2. Person (Persona / Miembro)

**Tabla:** `people`

### Atributos

| Campo | Tipo |
|------|------|
| id | PK |
| first_name | string |
| last_name | string |
| document | string |
| email | string |
| phone | string |
| created_at | timestamp |
| church_id | FK |

---

## 3. Course (Curso)

**Tabla:** `courses`

### Atributos

| Campo | Tipo |
|------|------|
| id | PK |
| name | string |
| description | string |
| price | decimal |
| active | boolean |
| created_at | timestamp |
| church_id | FK |

---

## 4. Enrollment (Inscripción)

**Tabla:** `enrollments`

### Atributos

| Campo | Tipo |
|------|------|
| id | PK |
| created_at | timestamp |
| status | string |
| payment_id | FK |
| course_id | FK |
| person_id | FK |

### Estados

```
PENDIENTE
PAGADA
```

---

## 5. Offering (Ofrenda)

**Tabla:** `offerings`

### Atributos

| Campo | Tipo |
|------|------|
| id | PK |
| amount | decimal |
| concept | string |
| status | string |
| created_at | timestamp |
| payment_id | FK |
| person_id | FK |

### Estados

```
PENDIENTE
REGISTRADA
```

---

## 6. Payment (Pago)

**Tabla:** `payments`

### Atributos

| Campo | Tipo |
|------|------|
| id | PK |
| amount | decimal |
| attempts | integer |
| reference_id | bigint |
| status | string |
| type | string |
| created_at | timestamp |
| updated_at | timestamp |

### Estados

```
INICIADO
CONFIRMADO
FALLIDO
```

### Tipos

```
INSCRIPCION_CURSO
OFRENDA
```

---

## 7. User (Usuario del Sistema)

**Tabla:** `users`

### Atributos

| Campo | Tipo |
|------|------|
| id | PK |
| email | string |
| password_hash | string |
| role | string |
| active | boolean |
| created_at | timestamp |

### Roles

```
ADMIN
CLIENT
```

---

# Relaciones del MER

### Iglesia → Personas

```
Church 1 -------- N Person
```

Una iglesia tiene muchas personas.

---

### Iglesia → Cursos

```
Church 1 -------- N Course
```

Una iglesia puede ofrecer varios cursos.

---

### Persona → Inscripción

```
Person 1 -------- N Enrollment
```

Una persona puede inscribirse en varios cursos.

---

### Curso → Inscripción

```
Course 1 -------- N Enrollment
```

Un curso puede tener muchas inscripciones.

---

### Persona → Ofrenda

```
Person 1 -------- N Offering
```

Una persona puede realizar múltiples ofrendas.

---

### Pago → Ofrenda

```
Payment 1 -------- 1 Offering
```

Un pago puede corresponder a una ofrenda.

---

### Pago → Inscripción

```
Payment 1 -------- 1 Enrollment
```

Un pago puede corresponder a una inscripción.

---

# Estructura Lógica del MER

```
CHURCH
   │
   ├── PERSON
   │       ├── OFFERING
   │       │       └── PAYMENT
   │       │
   │       └── ENROLLMENT
   │               ├── COURSE
   │               └── PAYMENT
   │
   └── COURSE
```

---

#  Clasificación de Entidades

## Entidades fuertes

- Church
- Person
- Course
- User

## Entidades transaccionales

- Enrollment
- Offering
- Payment

---

#  Cardinalidades

| Relación | Cardinalidad |
|------|------|
| Church → Person | 1 : N |
| Church → Course | 1 : N |
| Person → Enrollment | 1 : N |
| Course → Enrollment | 1 : N |
| Person → Offering | 1 : N |
| Payment → Offering | 1 : 1 |
| Payment → Enrollment | 1 : 1 |

---