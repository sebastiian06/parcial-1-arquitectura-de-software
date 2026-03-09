# Cambio 4: Corrección de Repositorios y Queries

##  Archivos Modificados
- `backend/src/main/java/com/iglesia/PersonRepository.java`
- `backend/src/main/java/com/iglesia/CourseRepository.java`
- `backend/src/main/java/com/iglesia/OfferingRepository.java`
- `backend/src/main/java/com/iglesia/PaymentRepository.java`
- `backend/src/main/java/com/iglesia/DashboardController.java` (simplificado)

##  Descripción del Cambio
Se corrigieron errores en las queries de los repositorios que impedían la compilación. El principal problema era que se intentaba acceder a `church` directamente desde `Offering` cuando la relación era `Offering.person.church`.

## 🔍 Problemas Encontrados y Soluciones

### Problema 1: OfferingRepository
**Error:** `Could not resolve attribute 'church' of 'com.iglesia.Offering'`

**Solución:** Acceder a church a través de person
```java
//  Incorrecto
@Query("SELECT SUM(o.amount) FROM Offering o WHERE o.church.id = :churchId")

//  Correcto
@Query("SELECT SUM(o.amount) FROM Offering o WHERE o.person.church.id = :churchId")
```

### Problema 2: PaymentRepository
Error: No property 'churchId' found for type 'Payment'

Solución: Query con JOINs para ofrendas e inscripciones
```java
@Query("SELECT COUNT(p) FROM Payment p WHERE " +
       "((p.type = 'OFFERING' AND p.referenceId IN (SELECT o.id FROM Offering o WHERE o.person.church.id = :churchId)) " +
       "OR (p.type = 'ENROLLMENT' AND p.referenceId IN (SELECT e.id FROM Enrollment e WHERE e.person.church.id = :churchId))) " +
       "AND p.status = :status")
```
### Problema 3: CourseRepository
Error: Faltaba findAllByChurchId

Solución: Agregar el método

```java
List<Course> findAllByChurchId(Long churchId);
```



