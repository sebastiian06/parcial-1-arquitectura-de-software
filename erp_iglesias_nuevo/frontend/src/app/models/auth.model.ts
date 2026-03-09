export interface LoginRequest {
    email: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    email: string;
    role: string;
}

export interface User {
    id: number;
    email: string;
    role: string;
}

export interface CreateUserRequest {
    email: string;
    password: string;
    role: string;
}