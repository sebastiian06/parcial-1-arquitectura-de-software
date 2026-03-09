# (ADR) - ERP IGLESIAS

**Título:** Refactorización Arquitectónica Aplicando Patrones de Diseño y Principios SOLID  
**Estado:** 5 Implementados / 10 Propuestos  
**Fecha:** 09 Marzo 2026

---

# 1. Contexto

## 1.1 Stack Tecnológico Actual

| Capa | Tecnología | Versión |
|-----|------------|--------|
| Backend | Java + Spring Boot | 17 + 3.2.3 |
| Frontend | Angular + TypeScript | 17 + 5.2.0 |
| Base de Datos | PostgreSQL | 16 |
| Seguridad | JWT + Spring Security | - |
| Build | Maven | 3.9.6 |
| Contenedorización | Docker | - |

---

## 1.2 Estructura Actual del Proyecto

```text
erp_iglesias/
├── backend/
│   ├── src/main/java/com/iglesia/
│   │   ├── *.java (TODOS LOS ARCHIVOS EN UN SOLO PAQUETE)
│   │   ├── PersonController.java
│   │   ├── PersonRepository.java
│   │   ├── Person.java
│   │   ├── CourseController.java
│   │   ├── CourseRepository.java
│   │   ├── Course.java
│   │   ├── ... (40+ archivos mezclados)
│   │   └── SecurityConfig.java
│   └── src/main/resources/application.properties
├── frontend/
│   ├── src/app/
│   │   ├── api.service.ts (SERVICIO MONOLÍTICO)
│   │   ├── *.component.ts (COMPONENTES EN RAÍZ)
│   │   └── auth.guard.ts
│   └── ...
└── docker-compose.yml
```

## 1.3 Hallazgos del Diagnóstico

### Backend
###	Problema	Impacto
1.	Todo en un solo paquete (com.iglesia) Viola SRP, difícil mantenimiento
2.	Lógica de negocio en controladores Controladores hacen demasiado
3.	Validaciones inexistentes o inconsistentes	Datos inválidos
4.	DTOs como clases internas No reutilizables
5.	Queries incorrectas en repositorios Errores de compilación
6.	Manejo de errores deficiente Sin feedback claro

### Frontend
###	Problema	Impacto
7.	Servicio único api.service.ts con 18 métodos Monolito
8.	Sin modelos/interfaces Datos como any
9.	Componentes con mucha lógica Mezcla presentación y negocio
10.	Sin manejo de errores consistente Usuario sin feedback

### Datos
### Problema	Impacto
11.	Documentos duplicados Inconsistencia
12.	Campos numéricos con texto Datos inválidos
13.	Emails sin validación Formato incorrecto
---

# 2. Diagrama MER (Modelo Entidad-Relación)

![alt text](<Diagrama MER erp_iglesias.jpg>)
-- 

# 3. Decisiones Arquitectónicas

## 3.1 Decisiones Implementadas

### Decisión 1 — Service Layer Pattern

#### Estado: Implementado
#### Patrón: Single Responsibility Principle

#### Descripción

Separar la lógica de negocio de los controladores creando una capa de servicios.

#### Justificación

Los controladores solo deben manejar peticiones HTTP.
La lógica de negocio debe estar encapsulada en servicios.

#### Archivos

backend/service/PersonService.java
backend/PersonController.java
backend/PersonRepository.java

---

### Decisión 2 — Strategy Pattern para Validaciones

#### Estado: Implementado
#### Patrón: Strategy Pattern + Open/Closed Principle

#### Descripción

- Implementar validadores específicos para cada tipo de campo:

    - documento

    - teléfono

    - email

#### Archivos

- validation/Validator.java
- validation/DocumentValidator.java
- validation/PhoneValidator.java
- validation/EmailValidator.java
- validation/ValidationService.java
- service/PersonService.java

#### Ejemplo:

```public interface Validator<T> {
    void validate(T value);
}

public class DocumentValidator implements Validator<String> {
    public void validate(String value) {
        if(!value.matches("^\\d+$")){
            throw new ValidationException("Documento inválido");
        }
    }
}
```
---

### Decisión 3 — DTO Pattern

#### Estado: Implementado
#### Patrón: DTO / Separation of Concerns

#### Descripción

- Separar DTOs en paquetes independientes.

#### Archivos

- dto/request/PersonRequest.java
- dto/response/PersonResponse.java

#### Ejemplo:

```
public record PersonRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email
){}
```

### Decisión 4 — Corrección de Repositorios

#### Estado: Implementado
#### Patrón: Repository Pattern

#### Problema

#### Query incorrecta:

- SELECT SUM(o.amount) FROM Offering o WHERE o.church.id = :churchId

#### Corrección

```
SELECT SUM(o.amount) 
FROM Offering o 
WHERE o.person.church.id = :churchId
```

### Decisión 5 — Servicios Especializados en Frontend

#### Estado: Implementado
#### Patrón: SRP / Separation of Concerns

#### Antes:

``` 
export class ApiService {
 login()
 listPeople()
 createPerson()
 listCourses()
 // 18 métodos
}
```

#### Después:

```
export class PersonService {
 list()
 create()
 update()
 delete()
}
```
```
export class AuthService {
 login()
 logout()
}
``` 

## 3.2 Decisiones Propuestas

### Decisión 6 — Factory Pattern

#### Estado: Propuesto

#### Encapsular creación de objetos complejos.

```
public class PersonFactory {

 public static Person createFromRequest(PersonRequest request, Church church){

  Person p = new Person();
  p.setFirstName(request.firstName());
  p.setLastName(request.lastName());
  p.setDocument(request.document());
  p.setPhone(request.phone());
  p.setEmail(request.email());
  p.setChurch(church);

  return p;
 }
}
```

### Decisión 7 — Observer Pattern

#### Estado: Propuesto

#### Sistema de eventos para notificaciones.

```
public class PaymentCompletedEvent {
 private final Payment payment;
}

Listener:

@EventListener
public void handlePaymentCompleted(PaymentCompletedEvent event){
 // enviar email
}
```

### Decisión 8 — Template Method

#### Estado: Propuesto

#### Estandarizar procesos de negocio.

```
public abstract class PaymentProcess {

 public final Payment execute(PaymentRequest request){
  validate(request);
  Payment payment = createPayment(request);
  processPayment(payment);
  notify(payment);
  return payment;
 }

 protected abstract void validate(PaymentRequest request);
}
```

### Decisión 9 — Facade Pattern

#### Estado: Propuesto

#### Simplificar consumo de servicios.

```
@Injectable()
export class ApiFacade {

 constructor(
  private personService: PersonService,
  private courseService: CourseService,
  private paymentService: PaymentService
 ){}

 getDashboardData(){
  return forkJoin({
   people: this.personService.list(),
   courses: this.courseService.list(),
   payments: this.paymentService.list()
  });
 }
}
```

### Decisión 10 — Decorator Pattern

#### Estado: Propuesto

#### Logging y monitoreo transversal.

```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {}
```

#### Aspecto:

```
@Around("@annotation(LogExecutionTime)")
public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

 long start = System.currentTimeMillis();

 Object result = joinPoint.proceed();

 long elapsed = System.currentTimeMillis() - start;

 logger.info("{} ejecutado en {} ms", joinPoint.getSignature(), elapsed);

 return result;
}
```

# 4. Consecuencias

## 4.1 Impacto Positivo
| Aspecto | Antes | Después |
|--------|------|--------|
| Mantenibilidad | Código mezclado | Separación por capas |
| Validación | Datos inválidos | Validaciones robustas |
| Organización | Un paquete | Arquitectura por capas |
| Frontend | Servicio monolítico | Servicios especializados |
| Reutilización | DTO internos | DTO reutilizables |
| Compilación | Queries erróneas | Queries corregidas |

## 4.2 Trade-offs
| Aspecto | Consideración |
|--------|--------------|
| Complejidad inicial | Más archivos |
| Curva de aprendizaje | Nuevos patrones |
| Tiempo de desarrollo | Refactorización inicial |
| Performance | Impacto mínimo |


# 5. Enlaces

# Repositorio: https://github.com/sebastiian06/parcial-1-arquitectura-de-software.git

