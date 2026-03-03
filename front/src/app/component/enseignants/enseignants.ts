import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { EnseignantService } from '../../service/enseignant-service';
import { Enseignant } from '../../model/enseignant';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CreateEnseignant } from '../../model/create-enseignant';
import { Tag } from '../../model/tag';
import { TagService } from '../../service/tag-service';


@Component({
  selector: 'app-enseignants',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './enseignants.html',
  styleUrl: './enseignants.scss',
})
export class Enseignants implements OnInit {

  listEnseignant: Enseignant[] = [];

  messageErreurListProf?: string;

  messageErreurCreation?: string;

  enseignantForm!: FormGroup;

  tagDispo: Tag[] = [];



  constructor(
    private enenseignant_service: EnseignantService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private tagService: TagService,
  ){}


  ngOnInit(): void {
    this.initForm();
    this.allEnseignant();
    this.tagService.getAllTags().subscribe({
      next: (data) => this.tagDispo = data
    });
  }

  initForm(){

    this.enseignantForm = this.fb.group({

    nom: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] }),
    prenom: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] }),
    email: new FormControl<string>('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    tagIds: new FormControl<Tag[]>([], { nonNullable: true })

    });
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

        this.messageErreurListProf = "Une erreur est survenue dans le backend dont je n'ai pas été informé (gassien)";
        this.cdr.detectChanges();

        }

       }
    });
  }

  createEnseignant(): void{

    if(this.enseignantForm.invalid) return;

    this.messageErreurCreation = undefined;

    const enseignant: CreateEnseignant = this.enseignantForm.value;
    

    this.enenseignant_service.createEnseignant(enseignant).subscribe({

      next: () => {

        this.allEnseignant();
        this.enseignantForm.reset();
        this.cdr.detectChanges();
      },

      error: (e) => {

        if(e.status === 400){

          this.messageErreurCreation = " Données invalides";

        }

        else if(e.status === 409){

          this.messageErreurCreation = "Email déjà utilisé";
          
        }


      }

    });
  
  };


  compareTags(t1: Tag, t2: Tag): boolean {
    return t1 && t2 ? t1.id === t2.id : t1 === t2;
  }





}
