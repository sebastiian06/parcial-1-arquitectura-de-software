import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Payment, PaymentStatus } from '../../models/payment.model';

@Injectable({ providedIn: 'root' })
export class PaymentService {
    private baseUrl = 'http://localhost:8080/api/payments';

    constructor(private http: HttpClient) {}

    list(status?: PaymentStatus): Observable<Payment[]> {
        const params = status ? `?status=${status}` : '';
        return this.http.get<Payment[]>(`${this.baseUrl}${params}`);
    }

    confirmPayment(id: number): Observable<Payment> {
        return this.http.post<Payment>(`${this.baseUrl}/${id}/confirm`, {});
    }

    failPayment(id: number): Observable<Payment> {
        return this.http.post<Payment>(`${this.baseUrl}/${id}/fail`, {});
    }

    retryPayment(id: number): Observable<Payment> {
        return this.http.post<Payment>(`${this.baseUrl}/${id}/retry`, {});
    }
}