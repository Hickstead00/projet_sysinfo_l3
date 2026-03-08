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
  ],
  templateUrl: './enseignants.html',
  styleUrl: './enseignants.scss',
})
export class Enseignants implements OnInit {
  listEnseignant: Enseignant[] = [];

  messageErreurListProf?: string;

  messageErreurCreation?: string;

  messageErreurDelete?: string;

  messageErreurModif?: string;

  enseignantForm!: FormGroup; // !  non nul/undefined

  tagDispo: Tag[] = [];

  enseignantSelectionne: Enseignant[] = [];

  rechercheEffectuee: boolean = false;

  constructor(
    private enseignantService: EnseignantService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private tagService: TagService,
  ) {}

  ngOnInit(): void {
    this.initForm();

    this.allEnseignant();

    this.tagService.getAllTags().subscribe({
      next: (data) => (this.tagDispo = data),
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
        this.cdr.detectChanges();
      },
    });
  }

  createEnseignant(): void {
    if (this.enseignantForm.invalid) return;

    this.messageErreurCreation = undefined;

    const enseignant: CreateEnseignant = this.enseignantForm.value;

    this.enseignantService.createEnseignant(enseignant).subscribe({
      next: () => {
        this.allEnseignant();
        this.enseignantForm.reset();
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
        this.allEnseignant();
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
    ];
    return couleurs[(prenom.charCodeAt(0) + nom.charCodeAt(0)) % couleurs.length];
  }
}
