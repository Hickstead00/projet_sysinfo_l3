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
        
        this.listEnseignant = data;
        this.cdr.detectChanges();

       }
    });
  }

}
