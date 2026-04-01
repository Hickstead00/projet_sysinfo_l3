import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Enseignant } from '../model/enseignant';
import { CreateEnseignant } from '../model/create-enseignant';

@Injectable({ providedIn: 'root' })
export class EnseignantService {
  private apiEnseignant = 'http://localhost:8080/api/professeurs';

  constructor(private http: HttpClient) {}

  getAllEnseignant(): Observable<Enseignant[]> {
    return this.http.get<Enseignant[]>(this.apiEnseignant);
  }

  createEnseignant(enseignant: CreateEnseignant): Observable<Enseignant> {
    return this.http.post<Enseignant>(this.apiEnseignant, enseignant);
  }

  deleteEnseignant(id: number): Observable<Enseignant> {
    return this.http.delete<Enseignant>(this.apiEnseignant + '/' + id);
  }

  editEnseignant(id: number, enseignant: CreateEnseignant ): Observable<Enseignant> {

    return this.http.put<Enseignant>(this.apiEnseignant + '/' + id, enseignant);

  }

  rechercherEnseignant(nom_prenom: string): Observable<Enseignant[]> {

    return this.http.get<Enseignant[]>(this.apiEnseignant + '/search?s=' + nom_prenom);

  }

  hourlyVolumeTeachers() : Observable<number> {
    return this.http.get<number>(this.apiEnseignant + '/hourlyVolumeTeachers');
  }
}
