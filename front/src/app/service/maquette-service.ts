import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Maquette } from '../model/maquette';
import { CreateMaquette } from '../model/create-maquette';
import { Semestre } from '../model/semestre';

@Injectable({ providedIn: 'root' })
export class MaquetteService {
  private apiMaquette = 'http://localhost:8080/api/maquettes';

  constructor(private http: HttpClient) {}

  getAllMaquettes(): Observable<Maquette[]> {
    return this.http.get<Maquette[]>(this.apiMaquette);
  }

  getMaquetteById(id: number): Observable<Maquette> {
    return this.http.get<Maquette>(this.apiMaquette + '/' + id);
  }

  createMaquette(maquette: CreateMaquette): Observable<Maquette> {
    return this.http.post<Maquette>(this.apiMaquette, maquette);
  }

  deleteMaquette(id: number): Observable<void> {
    return this.http.delete<void>(this.apiMaquette + '/' + id);
  }

  addUeToSemestre(maquetteId: number, semestreId: number, ueId: number): Observable<Semestre> {
    return this.http.post<Semestre>(
      `${this.apiMaquette}/${maquetteId}/semestres/${semestreId}/ues/${ueId}`, {}
    );
  }

  removeUeFromSemestre(maquetteId: number, semestreId: number, ueId: number): Observable<Semestre> {
    return this.http.delete<Semestre>(
      `${this.apiMaquette}/${maquetteId}/semestres/${semestreId}/ues/${ueId}`
    );
  }
}
