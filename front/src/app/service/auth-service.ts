import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthResponse } from '../model/auth-response';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiAuth = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(email: string, motDePasse: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiAuth + '/login', { email, motDePasse });
  }

  logout(): Observable<any> {
    return this.http.post(this.apiAuth + '/logout', {});
  }

  me(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(this.apiAuth + '/me');
  }
}
