import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Parametres } from '../model/parametres';

@Injectable({ providedIn: 'root' })
export class ParametresService {
  private apiParametres = 'http://localhost:8080/api/parametres';

  constructor(private http: HttpClient) {}

  getParametres(): Observable<Parametres> {
    return this.http.get<Parametres>(this.apiParametres);
  }

  updateParametres(parametres: Parametres): Observable<Parametres> {
    return this.http.put<Parametres>(this.apiParametres, parametres);
  }
}
