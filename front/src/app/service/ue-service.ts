import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UE } from '../component/ue/ue';
import { Observable } from 'rxjs';
import { CreateUe } from '../model/create-ue';

@Injectable({
  providedIn: 'root',
})
export class UeService {
  private apiUE = 'http://localhost:8080/api/ues';

  constructor(private http: HttpClient) {}

  getAllUe(): Observable<UE[]> {
    return this.http.get<UE[]>(this.apiUE);
  }

  getUeByNom(nom: string): Observable<UE[]> {
    return this.http.get<UE[]>(this.apiUE + 'search?s=' + nom);
  }

  createUe(ue: CreateUe): Observable<UE> {
    return this.http.post<UE>(this.apiUE, ue);
  }

  deleteUe(id: number): Observable<UE> {
    return this.http.delete<UE>(this.apiUE + '/' + id);
  }

  modifierUe(id: number, ue: CreateUe): Observable<UE> {
    return this.http.put<UE>(this.apiUE + '/' + id, ue);
  }
}
