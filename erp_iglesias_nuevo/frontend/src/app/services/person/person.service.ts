import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Person, PersonPayload } from '../../models/person.model';

@Injectable({ providedIn: 'root' })
export class PersonService {
    private baseUrl = 'http://localhost:8080/api/people';

    constructor(private http: HttpClient) {}

    list(): Observable<Person[]> {
        return this.http.get<Person[]>(this.baseUrl);
    }

    getById(id: number): Observable<Person> {
        return this.http.get<Person>(`${this.baseUrl}/${id}`);
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