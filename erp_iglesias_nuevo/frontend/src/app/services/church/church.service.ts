import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Church } from '../../models/church.model';

@Injectable({ providedIn: 'root' })
export class ChurchService {
    private baseUrl = 'http://localhost:8080/api/church';

    constructor(private http: HttpClient) {}

    get(): Observable<Church> {
        return this.http.get<Church>(this.baseUrl);
    }

    create(name: string, address: string): Observable<Church> {
        return this.http.post<Church>(this.baseUrl, { name, address });
    }
}