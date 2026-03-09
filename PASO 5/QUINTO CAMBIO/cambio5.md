# Cambio 5: Servicios Especializados en Frontend

##  Archivos Creados

### Modelos:
- `frontend/src/app/models/person.model.ts`
- `frontend/src/app/models/course.model.ts`
- `frontend/src/app/models/auth.model.ts`
- `frontend/src/app/models/offering.model.ts`
- `frontend/src/app/models/payment.model.ts`
- `frontend/src/app/models/enrollment.model.ts`
- `frontend/src/app/models/church.model.ts`
- `frontend/src/app/models/dashboard.model.ts`
- `frontend/src/app/models/index.ts`

### Servicios:
- `frontend/src/app/services/auth/auth.service.ts`
- `frontend/src/app/services/person/person.service.ts`
- `frontend/src/app/services/course/course.service.ts`
- `frontend/src/app/services/payment/payment.service.ts`
- `frontend/src/app/services/offering/offering.service.ts`
- `frontend/src/app/services/enrollment/enrollment.service.ts`
- `frontend/src/app/services/church/church.service.ts`
- `frontend/src/app/services/dashboard/dashboard.service.ts`
- `frontend/src/app/services/index.ts`

##  Archivo Modificado
- `frontend/src/app/api.service.ts` (marcado como deprecated)

##  Descripción del Cambio
Se dividió el monolito `api.service.ts` en servicios específicos por dominio, aplicando el principio de Single Responsibility también en el frontend. Ahora cada servicio maneja un recurso específico de la API.

## 🔍 Problema que Soluciona
**Antes:** Un solo servicio con 18 métodos mezclando diferentes dominios:
- `api.service.ts` manejaba auth, people, courses, payments, etc.

**Después:** Servicios especializados:
- `AuthService`: solo autenticación
- `PersonService`: solo personas
- `CourseService`: solo cursos
- etc.

##  Código - Ejemplo de Servicio Especializado

```typescript
@Injectable({ providedIn: 'root' })
export class PersonService {
    private baseUrl = 'http://localhost:8080/api/people';

    constructor(private http: HttpClient) {}

    list(): Observable<Person[]> {
        return this.http.get<Person[]>(this.baseUrl);
    }

    create(payload: PersonPayload): Observable<Person> {
        return this.http.post<Person>(this.baseUrl, payload);
    }

    update(id: number, payload: PersonPayload): Observable<Person> {
        return this.http.put<Person>(`${this.baseUrl}/${id}`, payload);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
```