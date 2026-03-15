import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TagService } from '../../service/tag-service';
import { Tag } from '../../model/tag';
import { CommonModule } from '@angular/common';
import { CreateTag } from '../../model/create-tag';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-gestion-tags',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './gestion-tags.html',
  styleUrl: './gestion-tags.scss',
})
export class GestionTags implements OnInit {
  listTags: Tag[] = [];

  tagSelectionne: Tag[] = [];

  messageErreurCreation?: string;

  messageErreurDelete?: string;

  messageSucces?: string;

  tagForm!: FormGroup;

  rechercheEffectue: boolean = false;

  nbParTag: { [id: number]: { ens: number; ue: number } } = {};

  couleurs = [
    '#7c5c3e',
    '#2563eb',
    '#16a34a',
    '#7c3aed',
    '#dc2626',
    '#ea580c',
    '#0891b2',
    '#4f46e5',
    '#0f172a',
    '#059669',
    '#d97706',
    '#db2777',
    '#2a1c42',
    '#0284c7',
  ];

  couleurSelectionnee?: string;

  constructor(
    private tagService: TagService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.allTags();
    this.initForm();
  }

  initForm() {
    this.tagForm = this.fb.group({
      nomTag: new FormControl<String>('', { nonNullable: true, validators: [Validators.required] }),
      couleur: new FormControl<String>('#000000', {
        nonNullable: true,
        validators: [Validators.required],
      }),
    });
  }

  allTags(): void {
    this.tagService.getAllTags().subscribe({
      next: (tags) => {
        // réponse du back avec succes (data) corps de ce que le back renvoie

        this.listTags = tags;
        tags.forEach((tag) => this.chargerNb(tag.id));
        this.cdr.detectChanges();
      },
    });
  }

  deleteTagById(id: number): void {
    this.tagService.deleteTag(id).subscribe({
      next: () => {
        this.messageSucces = 'Tag supprimé avec succès !';
        this.allTags();
        this.cdr.detectChanges();
      },

      error: (e) => {
        if (e.status === 404) {
          this.messageErreurDelete = 'Tag introuvable.';
          this.cdr.detectChanges();
        }
      },
    });
  }

  createTag(): void {
    if (this.tagForm.invalid) return;

    const tag: CreateTag = this.tagForm.value;

    this.messageErreurCreation = undefined;

    this.tagService.createTag(tag).subscribe({
      next: () => {
        this.messageSucces = 'Tag créé avec succès !';
        this.tagForm.reset();
        this.allTags();
        this.cdr.detectChanges();
      },

      error: (e) => {
        if (e.status === 400) {
          this.messageErreurCreation = 'Données invalides.';
        } else if (e.status === 409) {
          this.messageErreurCreation = 'Un tag avec ce nom existe déjà.';
        }

        this.cdr.detectChanges();
      },
    });
  }

  choisirCouleur(couleur: string) {
    this.couleurSelectionnee = couleur;
    this.tagForm.get('couleur')?.setValue(couleur);
  }

  tagByNom(nom: string): void {
    this.tagSelectionne = [];

    this.rechercheEffectue = false;

    this.tagService.getTagByNom(nom).subscribe({
      next: (tag) => {
        this.tagSelectionne = tag;
        this.rechercheEffectue = true;
        this.cdr.detectChanges();
      },

      error: (e) => {
        if (e.status === 404) {
          console.log('Aucun tag avec ce nom');
        }
      },
    });
  }

  chargerNb(id: number): void {
    this.nbParTag[id] = { ens: 0, ue: 0 };

    this.tagService.getNbEnsTag(id).subscribe({
      next: (nb) => {
        this.nbParTag[id].ens = nb;
        this.cdr.detectChanges();
      },
    });

    this.tagService.getNbUeTag(id).subscribe({
      next: (nb) => {
        this.nbParTag[id].ue = nb;
        this.cdr.detectChanges();
      },
    });
  }
}
