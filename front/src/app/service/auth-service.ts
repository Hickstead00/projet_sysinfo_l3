import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { AuthResponse } from '../model/auth-response';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiAuth = 'http://localhost:8080/api/auth';
  private utilisateurSubject = new BehaviorSubject<AuthResponse | null>(null);
  utilisateur$ = this.utilisateurSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(email: string, motDePasse: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiAuth + '/login', { email, motDePasse }).pipe(
      tap((user) => this.utilisateurSubject.next(user)),
    );
  }

  logout(): Observable<any> {
    return this.http.post(this.apiAuth + '/logout', {}).pipe(
      tap(() => this.utilisateurSubject.next(null)),
    );
  }

  me(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(this.apiAuth + '/me').pipe(
      tap((user) => this.utilisateurSubject.next(user)),
    );
  }
}
