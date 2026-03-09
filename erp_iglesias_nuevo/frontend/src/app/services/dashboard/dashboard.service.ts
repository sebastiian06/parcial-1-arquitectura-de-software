import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Dashboard } from '../../models/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
    private baseUrl = 'http://localhost:8080/api/dashboard';

    constructor(private http: HttpClient) {}

    get(): Observable<Dashboard> {
        return this.http.get<Dashboard>(this.baseUrl);
    }
}