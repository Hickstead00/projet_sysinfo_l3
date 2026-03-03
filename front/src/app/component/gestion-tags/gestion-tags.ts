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

@Component({
  selector: 'app-gestion-tags',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './gestion-tags.html',
  styleUrl: './gestion-tags.scss',
})
export class GestionTags implements OnInit {
  listTags: Tag[] = [];

  tagSelectionne?: Tag;

  messageErreurRecherche?: string;

  messageErreurCreation?: string;

  messageErreurDelete?: string;

  messageErreurModif?: string;

  messageSucces?: string;

  tagForm!: FormGroup;

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
      next: (data) => {
        // réponse du back avec succes (data) corps de ce que le back renvoie

        this.listTags = data;
        this.cdr.detectChanges();
      },
    });
  }

  tagById(id: number): void {
    this.messageErreurRecherche = undefined;
    this.tagSelectionne = undefined;

    this.tagService.getTagbyId(id).subscribe({
      next: (tag) => {
        this.tagSelectionne = tag;
        this.cdr.detectChanges();
      },

      error: (e) => {
        if (e.status === 404) {
          this.messageErreurRecherche = `Le tag avec l'ID ${id} n'existe pas.`;
        } else {
          this.messageErreurRecherche = 'Une autre erreur est survenue.';
        }

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

  modifierTagById(id: number, nomTag: string, couleur: string): void {
    const tagamodif: CreateTag = { nomTag, couleur };

    this.tagService.modifierTag(id, tagamodif).subscribe({
      next: () => {
        this.messageSucces = 'Tag modifié avec succès !';
        this.allTags();
        this.cdr.detectChanges();
      },

      error: (e) => {
        if (e.status === 404) {
          this.messageErreurModif = 'Tag introuvable.';
        } else if (e.status === 409) {
          this.messageErreurModif = 'Un tag avec ce nom existe déjà.';
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
}
