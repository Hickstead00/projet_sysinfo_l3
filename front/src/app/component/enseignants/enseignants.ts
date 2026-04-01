import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { EnseignantService } from '../../service/enseignant-service';
import { Enseignant } from '../../model/enseignant';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CreateEnseignant } from '../../model/create-enseignant';
import { Tag } from '../../model/tag';
import { TagService } from '../../service/tag-service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatPaginatorModule } from '@angular/material/paginator';
import { PageEvent } from '@angular/material/paginator';
import {
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
} from '@angular/material/autocomplete';
import { MatChipGrid, MatChipRow, MatChipsModule } from '@angular/material/chips';
import { ViewChild, ElementRef } from '@angular/core';
import {UeService} from '../../service/ue-service';

@Component({
  selector: 'app-enseignants',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatOptionModule,
    MatChipGrid,
    MatChipRow,
    MatChipsModule,
    MatAutocompleteModule,
    MatPaginatorModule,
  ],
  templateUrl: './enseignants.html',
  styleUrl: './enseignants.scss',
})
export class Enseignants implements OnInit {
  listEnseignant: Enseignant[] = [];

  messageErreurListProf?: string;

  messageErreurCreation?: string;

  messageErreurDelete?: string;

  enseignantForm!: FormGroup; // !  non nul/undefined

  tagDispo: Tag[] = [];

  enseignantSelectionne: Enseignant[] = [];

  rechercheEffectuee: boolean = false;

  tagsSelectionnes: Tag[] = [];

  tagsFiltres: Tag[] = [];

  @ViewChild('tagInput') tagInput!: ElementRef<HTMLInputElement>;

  searchControl = new FormControl('', { nonNullable: true });

  countUEattachToTeachers: number = 0;

  hourlyVolumeUETeachers : number = 0;

  currentPage = 0;

  teacherByPage = 10;

  totalTeachers = 0;

  teacherPage: Enseignant[] = [];

  enseignantEnCours?: Enseignant;

  constructor(
    private enseignantService: EnseignantService,
    private ueService: UeService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private tagService: TagService,
  ) {}

  ngOnInit(): void {
    this.initForm();

    this.allEnseignant();
    this.countTotalUEattachToTeachers();
    this.getHourlyVolumeTeachers();
    this.tagService.getAllTags().subscribe({
      next: (tags) => (this.tagDispo = tags),
    });
  }

  initForm() {
    this.enseignantForm = this.fb.group({
      nom: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] }),
      prenom: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] }),
      email: new FormControl<string>('', {
        nonNullable: true,
        validators: [Validators.required, Validators.email],
      }),
      tagIds: new FormControl<Tag[]>([], { nonNullable: true }),
    }); // new FormControl<Type>(valeurParDéfaut, nonNullable permet de revenir à la valeur par défaut et non null)
    // Validators.required le champs ne peut pas être vide.
    // Validators.email verifie que le format est un email valide.
  }

  allEnseignant(): void {
    this.enseignantService.getAllEnseignant().subscribe({
      next: (data) => {
        this.listEnseignant = data;
        this.totalTeachers = data.length;
        this.updateTeachersByPage();
        this.cdr.detectChanges();
      },
    });
  }

  manageEnseignant(): void {
    if (this.enseignantEnCours) {
      this.updateEnseignant();
    } else {
      this.createEnseignant();
    }
    this.enseignantEnCours = undefined;
  }

  createEnseignant(): void {
    if (this.enseignantForm.invalid) {
      this.messageErreurCreation = "Un nom, prénom et email valide sont obligatoires";
      this.cdr.detectChanges();
      return;
    }

    this.messageErreurCreation = undefined;

    const enseignant: CreateEnseignant = this.enseignantForm.value;

    this.enseignantService.createEnseignant(enseignant).subscribe({
      next: () => {
        this.rechercheEffectuee = false;
        this.enseignantSelectionne = [];
        this.searchControl.setValue('');
        this.allEnseignant();
        this.updateTeachersByPage();
        this.enseignantForm.reset();
        this.tagsSelectionnes = [];
        this.cdr.detectChanges();
      },

      error: (e) => {
        if (e.status === 400) {
          this.messageErreurCreation = 'Données invalides';
        } else if (e.status === 409) {
          this.messageErreurCreation = 'Email déjà utilisé';
        }
        this.cdr.detectChanges();
      },
    });
  }

  updateEnseignant(): void {
    if (!this.enseignantEnCours) return;
    if (this.enseignantForm.invalid) {
      this.messageErreurCreation = "Un nom, prénom et email valide sont obligatoires";
      this.cdr.detectChanges();
      return;
    }

    this.messageErreurCreation = undefined;

    const enseignantData: CreateEnseignant = this.enseignantForm.value;

    this.enseignantService.editEnseignant(this.enseignantEnCours.id, enseignantData).subscribe({
      next: () => {
        this.rechercheEffectuee = false;
        this.enseignantSelectionne = [];
        this.searchControl.setValue('');
        this.allEnseignant();
        this.updateTeachersByPage();
        this.enseignantForm.reset();
        this.tagsSelectionnes = [];
        this.cdr.detectChanges();
      },

      error: (e) => {
        if (e.status === 400) {
          this.messageErreurCreation = 'Données invalides';
        } else if (e.status === 409) {
          this.messageErreurCreation = 'Email déjà utilisé';
        }
        this.cdr.detectChanges();
      },
    });
  }

  deleteEnseignantById(id: number): void {
    this.enseignantService.deleteEnseignant(id).subscribe({
      next: () => {
        this.rechercheEffectuee = false;
        this.enseignantSelectionne = [];
        this.searchControl.setValue('');
        this.allEnseignant();
        this.updateTeachersByPage();
        this.cdr.detectChanges();
      },

      error: (e) => {
        if (e.status === 404) {
          this.messageErreurDelete = 'Enseignant introuvable';
          this.cdr.detectChanges();
        }
      },
    });
  }

  rechercherEnseignantByNom(nom_prenom: string): void {
    this.enseignantSelectionne = [];

    this.rechercheEffectuee = false;

    this.enseignantService.rechercherEnseignant(nom_prenom).subscribe({
      next: (enseignant) => {
        this.enseignantSelectionne = enseignant;
        this.rechercheEffectuee = true;
        this.updateTeachersByPage();
        this.cdr.detectChanges();
      },

      error: (e) => {
        console.error('Erreur :', e);
      },
    });
  }

  getAvatarColor(prenom: string, nom: string): string {
    const couleurs = [
      '#1e3a6e',
      '#16a34a',
      '#7c3aed',
      '#dc2626',
      '#ea580c',
      '#0891b2',
      '#db2777',
      '#059669',
      '#b45309',
      '#0369a1',
      '#7c2d12',
      '#166534',
      '#581c87',
      '#9f1239',
      '#0f766e',
      '#1d4ed8',
      '#92400e',
      '#065f46',
      '#6b21a8',
      '#be123c',
      '#155e75',
      '#14532d',
      '#7e22ce',
      '#0c4a6e',
      '#854d0e',
      '#134e4a',
      '#4c1d95',
      '#881337',
      '#164e63',
      '#15803d',
      '#6d28d9',
      '#b91c1c',
      '#78350f',
      '#0e7490',
      '#7f1d1d',
      '#1e4620',
      '#3730a3',
      '#9d174d',
      '#0a3d62',
      '#1a5276',
    ];
    return couleurs[(prenom.charCodeAt(0) + nom.charCodeAt(0)) % couleurs.length];
  }

  tagByNom(nom: string): void {
    if (!nom) {
      this.tagsFiltres = [];
      return;
    }

    this.tagService.getTagByNom(nom).subscribe({
      next: (tags) => {
        this.tagsFiltres = tags;
        this.cdr.detectChanges();
      },
    });
  }

  ajouterTag(event: MatAutocompleteSelectedEvent): void {
    const tag: Tag = event.option.value; // recupre le tag cliqué depuis l'evenemnt

    if (this.tagsSelectionnes.find((t) => t.id === tag.id)) return; // si tag déjà dans la liste on s'arrête

    this.tagsSelectionnes.push(tag); // ajoute le tag à la liste de selections

    this.enseignantForm.patchValue({ tagIds: this.tagsSelectionnes.map((t) => t.id) }); // rempli le form avec la liste des tags selection ( que leurs id via le map)
    this.tagInput.nativeElement.value = '';
  }

  retirerTag(tag: Tag): void {
    this.tagsSelectionnes = this.tagsSelectionnes.filter((t) => t.id !== tag.id); // garde seulement les tags qui n'ont pas l'id du tag à retirer

    this.enseignantForm.patchValue({ tagIds: this.tagsSelectionnes.map((t) => t.id) });
  }

  getHourlyVolumeTeachers() : void {
    this.enseignantService.hourlyVolumeTeachers().subscribe({
      next: (total) =>{
        this.hourlyVolumeUETeachers = total;
        this.cdr.detectChanges();
      },

      error : (e) =>{
        console.error('Erreur dans le calcul du volume horaire UE', e);
      }
    });
  }

  countTotalUEattachToTeachers(): void {
    this.ueService.countUEattachToTeachers().subscribe({
      next: (total) => {
        this.countUEattachToTeachers = total;
        this.cdr.detectChanges();
      },

      error: (e) => {
        console.error('Erreur dans le calcul total UE :', e);
      }
    });
  }

  updateTeachersByPage(): void {
    const start = this.currentPage * this.teacherByPage;
    const end = start + this.teacherByPage;
    this.teacherPage = (this.rechercheEffectuee ? this.enseignantSelectionne : this.listEnseignant).slice(start, end);
  }

  pageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.updateTeachersByPage();
  }

  updateEnseignantForm(ens: Enseignant): void {
    this.enseignantEnCours = ens;
    this.enseignantForm.patchValue({
      prenom: ens.prenom,
      nom: ens.nom,
      email: ens.email,
      tagIds: ens.tags.map(t => t.id)
    });
    this.tagsSelectionnes = [...ens.tags];
  }

}
