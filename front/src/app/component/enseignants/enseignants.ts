import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { EnseignantService } from '../../service/enseignant-service';
import { Enseignant } from '../../model/enseignant';
import { dateTimestampProvider } from 'rxjs/internal/scheduler/dateTimestampProvider';

@Component({
  selector: 'app-enseignants',
  imports: [CommonModule],
  templateUrl: './enseignants.html',
  styleUrl: './enseignants.scss',
})
export class Enseignants implements OnInit {

  listEnseignant: Enseignant[] = [];

  messageErreur?: string;



  constructor(
    private enenseignant_service: EnseignantService,
    private cdr: ChangeDetectorRef,
  ){}


  ngOnInit(): void {
    
    this.allEnseignant();

  }



  allEnseignant(): void {

    this.enenseignant_service.getAllEnseignant().subscribe({

      next:(data) => { 

        console.log("succes")
        this.listEnseignant = data;
        this.cdr.detectChanges();

       },

       error: (e)=>

       {

        console.log("erreur inconnue backend")

        if(e.status === 500 ){

        this.messageErreur = "Une erreur est survenue dans le backend dont je n'ai pas été informé (gassien)";
        this.cdr.detectChanges();

        }

       }
    });
  }

}
