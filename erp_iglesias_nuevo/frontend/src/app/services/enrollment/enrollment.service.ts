import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Enrollment, EnrollmentPayload } from '../../models/enrollment.model';

@Injectable({ providedIn: 'root' })
export class EnrollmentService {
    private baseUrl = 'http://localhost:8080/api/enrollments';

    constructor(private http: HttpClient) {}

    list(): Observable<Enrollment[]> {
        return this.http.get<Enrollment[]>(this.baseUrl);
    }

    create(payload: EnrollmentPayload): Observable<Enrollment> {
        return this.http.post<Enrollment>(this.baseUrl, payload);
    }
}