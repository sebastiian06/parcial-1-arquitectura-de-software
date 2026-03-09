import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course, CoursePayload } from '../../models/course.model';

@Injectable({ providedIn: 'root' })
export class CourseService {
    private baseUrl = 'http://localhost:8080/api/courses';

    constructor(private http: HttpClient) {}

    list(): Observable<Course[]> {
        return this.http.get<Course[]>(this.baseUrl);
    }

    create(payload: CoursePayload): Observable<Course> {
        return this.http.post<Course>(this.baseUrl, payload);
    }
}