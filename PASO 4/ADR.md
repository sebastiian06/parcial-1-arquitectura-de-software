# Architecture Decision Record (ADR)
## Mejora arquitectónica del sistema ERP Iglesias mediante patrones de diseño y principios SOLID

---

# Estado

Propuesto

---

# Contexto

El sistema **ERP Iglesias** es una aplicación web diseñada para gestionar información administrativa de iglesias. El sistema permite registrar personas, cursos, inscripciones, ofrendas, pagos y usuarios del sistema.

El proyecto está compuesto por dos componentes principales:

- **Frontend:** desarrollado en Angular
- **Backend:** desarrollado en Java utilizando Spring Boot
- **Base de datos:** PostgreSQL

La aplicación se ejecuta utilizando **Docker**, con contenedores separados para frontend, backend y base de datos.

Durante el análisis de la arquitectura actual se identificaron algunas oportunidades de mejora relacionadas con:

- acoplamiento entre componentes
- distribución de responsabilidades
- extensibilidad del sistema
- reutilización de código
- mantenibilidad a largo plazo

Para mejorar la calidad arquitectónica del sistema se propone aplicar **patrones de diseño y principios SOLID** en diferentes componentes del backend.

---

# Diagrama MER

El modelo entidad-relación del sistema está compuesto por las siguientes entidades principales:

- churches
- people
- courses
- enrollments
- offerings
- payments
- users

## Relaciones principales
- Church 1 ---- N Person
- Church 1 ---- N Course
- Person 1 ---- N Enrollment
- Course 1 ---- N Enrollment
- Person 1 ---- N Offering
- Enrollment 1 ---- 1 Payment
- Offering 1 ---- 1 Payment


Este modelo permite representar la estructura de información del sistema, donde una iglesia puede tener múltiples personas y cursos, y las personas pueden realizar inscripciones y registrar ofrendas.

---

# Decisiones Arquitectónicas Propuestas

## Decisión 1: Aplicar Repository Pattern

**Patrón:** Repository  
**Principio SOLID:** Single Responsibility Principle (SRP)

Se propone mantener el acceso a datos encapsulado dentro de repositorios específicos para cada entidad del sistema.

Esto permite que las clases responsables de la lógica de negocio no dependan directamente de la implementación de acceso a base de datos.

**Justificación**

- separación clara entre lógica de negocio y persistencia
- mayor facilidad para pruebas unitarias
- reducción del acoplamiento entre capas

---

## Decisión 2: Implementar una capa de servicios (Service Layer)

**Patrón:** Service Layer  
**Principio SOLID:** SRP

La lógica de negocio debe centralizarse en clases de servicio en lugar de implementarse directamente en los controladores.

**Justificación**

- mejora la organización del código
- evita controladores demasiado grandes
- facilita reutilizar lógica de negocio

---

## Decisión 3: Aplicar el principio Open/Closed

**Principio SOLID:** Open/Closed Principle (OCP)

El sistema debe permitir agregar nuevas funcionalidades sin modificar el código existente.

Esto es especialmente importante en funcionalidades como el manejo de pagos o tipos de transacciones.

**Justificación**

- mejora la extensibilidad del sistema
- reduce el riesgo de errores al agregar nuevas funcionalidades

---

## Decisión 4: Implementar Strategy Pattern para el procesamiento de pagos

**Patrón:** Strategy  
**Principio SOLID:** OCP

Se propone separar la lógica de procesamiento de pagos en distintas estrategias dependiendo del tipo de pago.

Ejemplos:

- pago de inscripción a curso
- pago de ofrenda

**Justificación**

- elimina múltiples condicionales en el código
- permite agregar nuevos tipos de pago fácilmente
- mejora la flexibilidad del sistema

---

## Decisión 5: Aplicar Dependency Inversion Principle

**Principio SOLID:** DIP

Las clases de alto nivel deben depender de **abstracciones** y no de implementaciones concretas.

Esto implica que los servicios deben depender de interfaces y no directamente de implementaciones específicas.

**Justificación**

- reduce el acoplamiento entre componentes
- facilita el uso de pruebas unitarias
- permite reemplazar implementaciones sin afectar otras partes del sistema

---

## Decisión 6: Implementar DTO Pattern

**Patrón:** Data Transfer Object

Se propone utilizar objetos DTO para transferir datos entre backend y frontend en lugar de exponer directamente las entidades del modelo.

**Justificación**

- mejora la seguridad de los datos
- evita exponer la estructura interna del modelo
- permite controlar qué información se envía al cliente

---

## Decisión 7: Implementar Factory Pattern para creación de pagos

**Patrón:** Factory Method

Se propone centralizar la creación de objetos relacionados con pagos mediante una fábrica que determine qué tipo de objeto crear según el contexto.

**Justificación**

- desacopla la creación de objetos de su uso
- simplifica la lógica de instanciación
- facilita agregar nuevos tipos de pago

---

## Decisión 8: Aplicar Interface Segregation Principle

**Principio SOLID:** ISP

Se propone dividir interfaces grandes en interfaces más pequeñas y específicas para evitar que las clases dependan de métodos que no utilizan.

**Justificación**

- mejora la claridad del diseño
- reduce dependencias innecesarias
- facilita la implementación de nuevas funcionalidades

---

## Decisión 9: Implementar patrón Facade

**Patrón:** Facade

Se propone crear una capa que simplifique el acceso a operaciones complejas del sistema, agrupando múltiples servicios en una interfaz más sencilla.

**Justificación**

- simplifica la interacción entre componentes
- reduce la complejidad para los controladores
- mejora la legibilidad del código

---

## Decisión 10: Centralizar validaciones del sistema

**Principio SOLID:** SRP

Las validaciones de datos deben gestionarse en clases específicas en lugar de dispersarse en controladores o servicios.

**Justificación**

- evita duplicación de lógica
- mejora la consistencia de las validaciones
- facilita el mantenimiento del código