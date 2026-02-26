import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {  Tag }  from '../model/tag';
import { CreateTag } from '../model/create-tag';

@Injectable({ providedIn: 'root',})
export class TagService {

  private apiTag = 'http://localhost:8080/api/tags';



  constructor(private http: HttpClient){}

    getAllTags(): Observable<Tag[]>{

      return this.http.get<Tag[]>(this.apiTag);

    }

    getTagbyId(id: number): Observable<Tag>{

      return this.http.get<Tag>(this.apiTag + '/' + id);

    }

    createTag(tag: CreateTag): Observable<Tag> {

      return this.http.post<Tag>(this.apiTag, tag);

    }

    deleteTag(id: number): Observable<Tag> {

      return this.http.delete<Tag>(this.apiTag + '/' + id);

    }

    modifierTag(id: number, tag: CreateTag): Observable<Tag> {

      return this.http.put<Tag>(this.apiTag + '/' + id, tag);

    }


    
  }
