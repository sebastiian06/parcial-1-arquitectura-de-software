# Cambio 1 — Centralización de la lógica de Church usando Service Layer

## 1. Descripción del cambio

Se implementó un **Service Layer** para centralizar la lógica de obtención de la iglesia actual del sistema.
Anteriormente, varios controladores del sistema incluían lógica repetida para obtener la iglesia registrada mediante el método `requireChurch()`.

Este cambio consistió en:

* Crear una nueva clase `ChurchService`
* Mover la lógica de obtención de la iglesia a este servicio
* Modificar los controladores para utilizar el servicio en lugar de duplicar la lógica

De esta manera se mejora la arquitectura del sistema y se evita la duplicación de código.

---

# 2. Problema detectado

Antes del cambio, múltiples controladores incluían el siguiente método:

```java
private Church requireChurch() {
    return churchRepository.findAll()
        .stream()
        .findFirst()
        .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Debe registrar una iglesia primero"
            )
        );
}
```

Este método aparecía repetido en varios controladores:

* PersonController
* CourseController
* EnrollmentController
* OfferingController
* DashboardController

Esto generaba los siguientes problemas:

* **Duplicación de código**
* Violación del principio **DRY (Don't Repeat Yourself)**
* Controladores con responsabilidades adicionales
* Mayor dificultad para mantenimiento

---

# 3. Solución implementada

Se creó una capa de servicio llamada **ChurchService** que centraliza esta lógica.

Los controladores ahora delegan esta responsabilidad al servicio.

Arquitectura antes:

```
Controller
   └── ChurchRepository
   └── requireChurch() duplicado
```

Arquitectura después:

```
Controller
   └── ChurchService
           └── ChurchRepository
```

---

# 4. Principios de diseño aplicados

## DRY (Don't Repeat Yourself)

Se eliminó la duplicación de lógica en múltiples controladores.

Antes:

```
5 controladores con el mismo método requireChurch()
```

Ahora:

```
1 servicio centralizado
```

---

## Single Responsibility Principle (SRP)

Antes los controladores tenían dos responsabilidades:

* manejar endpoints HTTP
* gestionar lógica de obtención de la iglesia

Después del cambio:

* **Controller → maneja HTTP**
* **Service → maneja lógica de negocio**

---

## Service Layer Pattern

Se implementó una capa de servicio para encapsular lógica de negocio reutilizable.

---

# 5. Archivos modificados

Se modificaron los siguientes controladores:

```
PersonController.java
CourseController.java
EnrollmentController.java
OfferingController.java
DashboardController.java
```

Se creó el siguiente archivo nuevo:

```
ChurchService.java
```

---

# 6. Implementación

## ChurchService.java

Archivo creado:

```
src/main/java/com/iglesia/ChurchService.java
```

Código implementado:

```java
package com.iglesia;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChurchService {

    private final ChurchRepository churchRepository;

    public ChurchService(ChurchRepository churchRepository) {
        this.churchRepository = churchRepository;
    }

    public Church getCurrentChurch() {
        return churchRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Debe registrar una iglesia primero"
                        )
                );
    }
}
```

---

# 7. Ejemplo de cambio en los controladores

## Antes

```java
private final ChurchRepository churchRepository;

Church church = requireChurch();
```

Además cada controlador tenía el método:

```java
private Church requireChurch() {
    return churchRepository.findAll()
        .stream()
        .findFirst()
        .orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Debe registrar una iglesia primero"
            )
        );
}
```

---

## Después

```java
private final ChurchService churchService;

Church church = churchService.getCurrentChurch();
```

El método `requireChurch()` fue eliminado de todos los controladores.

---

# 8. Controladores actualizados

Los siguientes controladores fueron modificados para utilizar `ChurchService`.

## PersonController

Se reemplazó la lógica de obtención de iglesia por:

```java
Church church = churchService.getCurrentChurch();
```

---

## CourseController

Se eliminó `requireChurch()` y se utiliza:

```java
Church church = churchService.getCurrentChurch();
```

---

## EnrollmentController

Se actualizó la creación y consulta de inscripciones utilizando el servicio.

---

## OfferingController

Se centralizó la obtención de la iglesia para registrar las ofrendas.

---

## DashboardController

El dashboard ahora obtiene la iglesia actual desde `ChurchService`.

---

# 9. Impacto del cambio

Este cambio **no afecta la funcionalidad del sistema**, ya que únicamente reorganiza la arquitectura interna.

Los endpoints, respuestas y comportamiento del sistema permanecen iguales.

Beneficios obtenidos:

* Código más limpio
* Arquitectura más mantenible
* Eliminación de duplicación
* Mejor separación de responsabilidades

---

# 10. Pruebas funcionales

Para validar que el cambio no afectó el funcionamiento del sistema, se realizaron las siguientes pruebas.

## Prueba 1 — Crear persona


![alt text](pruebas-cambio1/image.png)

---

## Prueba 2 — Crear curso

![alt text](pruebas-cambio1/image4.png)
---

## Prueba 3 — Crear inscripción

![alt text](pruebas-cambio1/image-1.png)

---

## Prueba 4 — Registrar ofrenda

![alt text](pruebas-cambio1/image-2.png)

---

## Prueba 5 — Visualizar dashboard

El dashboard muestra correctamente los datos del sistema

![alt text](pruebas-cambio1/image-3.png)

---

# 11. Conclusión

La implementación del `ChurchService` permitió mejorar la arquitectura del sistema aplicando principios de diseño como **DRY, SRP y Service Layer Pattern**.

El cambio reduce la duplicación de código y facilita el mantenimiento futuro del sistema sin afectar su funcionalidad ni usabilidad.
