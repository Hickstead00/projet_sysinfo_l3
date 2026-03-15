import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Ue } from '../model/ue';
import { Observable } from 'rxjs';
import { CreateUe } from '../model/create-ue';

@Injectable({
  providedIn: 'root',
})
export class UeService {
  private apiUE = 'http://localhost:8080/api/ues';

  constructor(private http: HttpClient) {}

  getAllUe(): Observable<Ue[]> {
    return this.http.get<Ue[]>(this.apiUE);
  }

  getUeByNom(nom: string): Observable<Ue[]> {
    return this.http.get<Ue[]>(this.apiUE + '/search?s=' + nom);
  }

  createUe(ue: CreateUe): Observable<Ue> {
    return this.http.post<Ue>(this.apiUE, ue);
  }

  deleteUe(id: number): Observable<Ue> {
    return this.http.delete<Ue>(this.apiUE + '/' + id);
  }

  modifierUe(id: number, ue: CreateUe): Observable<Ue> {
    return this.http.put<Ue>(this.apiUE + '/' + id, ue);
  }
}
