import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest, LoginResponse, User, CreateUserRequest } from '../../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private baseUrl = 'http://localhost:8080/api/auth';
    private tokenKey = 'auth_token';

    constructor(private http: HttpClient) {}

    login(email: string, password: string): Observable<LoginResponse> {
        const request: LoginRequest = { email, password };
        return this.http.post<LoginResponse>(`${this.baseUrl}/login`, request)
            .pipe(
                tap(response => this.setToken(response.token))
            );
    }

    logout(): void {
        localStorage.removeItem(this.tokenKey);
    }

    setToken(token: string): void {
        localStorage.setItem(this.tokenKey, token);
    }

    getToken(): string | null {
        return localStorage.getItem(this.tokenKey);
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }

    getUserRole(): string | null {
        const token = this.getToken();
        if (!token) return null;
        
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.role;
        } catch (e) {
            return null;
        }
    }
}