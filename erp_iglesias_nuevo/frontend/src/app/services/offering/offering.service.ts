import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Offering, OfferingPayload } from '../../models/offering.model';

@Injectable({ providedIn: 'root' })
export class OfferingService {
    private baseUrl = 'http://localhost:8080/api/offerings';

    constructor(private http: HttpClient) {}

    list(): Observable<Offering[]> {
        return this.http.get<Offering[]>(this.baseUrl);
    }

    create(payload: OfferingPayload): Observable<Offering> {
        return this.http.post<Offering>(this.baseUrl, payload);
    }
}