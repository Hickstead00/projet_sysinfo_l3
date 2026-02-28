import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Enseignant } from '../model/enseignant';

@Injectable({ providedIn: 'root',})
export class EnseignantService {

  private apiEnseignant = "http://localhost:8080/api/professeurs";

  constructor(private http:HttpClient){}


  getAllEnseignant(): Observable<Enseignant[]>{


          return this.http.get<Enseignant[]>(this.apiEnseignant);

      }






}
  

