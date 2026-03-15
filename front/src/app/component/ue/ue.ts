import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UeService } from '../../service/ue-service';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  FormControl,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Tag } from '../../model/tag';
import { Enseignant } from '../../model/enseignant';
import { CreateUe } from '../../model/create-ue';
import { EnseignantService } from '../../service/enseignant-service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import {
  MatAutocompleteModule,
  MatAutocompleteSelectedEvent,
} from '@angular/material/autocomplete';
import { MatChipGrid, MatChipRow, MatChipsModule } from '@angular/material/chips';
import { MatCheckbox } from '@angular/material/checkbox';
import { TagService } from '../../service/tag-service';
import { Ue } from '../../model/ue';

@Component({
  selector: 'app-ue',
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
    MatCheckbox,
  ],
  templateUrl: './ue.html',
  styleUrl: './ue.scss',
})
export class UEComponent implements OnInit {
  listUe: Ue[] = [];

  ueForm!: FormGroup;

  enseignantDispo: Enseignant[] = [];

  enseignantSelectionnes: Enseignant[] = [];

  enseignantFiltres: Enseignant[] = [];

  tagsSelectionnes: Tag[] = [];

  tagsFiltres: Tag[] = [];

  ueSelectionnes: Ue[] = [];

  ueFiltres: Ue[] = [];
  constructor(
    private ueService: UeService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private enseignantService: EnseignantService,
    private tagService: TagService,
  ) {}

  ngOnInit(): void {
    this.allUe();
    this.initForm();
    this.enseignantService.getAllEnseignant().subscribe({
      next: (ens) => (this.enseignantDispo = ens),
    });
    this.ueService.getAllUe().subscribe({
      next: (ue) => (this.listUe = ue),
    });
  }

  initForm() {
    this.ueForm = this.fb.group({
      nomUe: new FormControl<string>('', { nonNullable: true, validators: [Validators.required] }),
      ects: new FormControl<number>(0, {
        nonNullable: true,
        validators: [Validators.required, Validators.max(30)],
      }),
      cm: new FormControl<number>(0, { nonNullable: true, validators: [Validators.required] }),
      td: new FormControl<number>(0, { nonNullable: true, validators: [Validators.required] }),
      tp: new FormControl<number>(0, { nonNullable: true, validators: [Validators.required] }),
      description: new FormControl<string>(''),
      ueObligatoire: new FormControl<boolean>(true, { nonNullable: true }),
      tagIds: new FormControl<number[]>([]),
      enseignantIds: new FormControl<number[]>([]),
      referentIds: new FormControl<number[]>([]),
      prerequisIds: new FormControl<number[]>([]),
    });
  }

  allUe(): void {
    this.ueService.getAllUe().subscribe({
      next: (ue) => {
        this.listUe = ue;
        this.cdr.detectChanges();
      },
    });
  }

  createUe(): void {
    if (this.ueForm.invalid) return;

    const ue: CreateUe = this.ueForm.value;

    this.ueService.createUe(ue).subscribe({
      next: () => {
        this.allUe();
        this.ueForm.reset();
        this.enseignantSelectionnes = [];
        this.tagsSelectionnes = [];
        this.ueSelectionnes = [];
        this.cdr.detectChanges();
      },
      error: (e) => {
        if (e.status === 400) {
          console.log('données invalides');
        } else if (e.status === 409) {
          console.log('ue déjà existante');
        }
        this.cdr.detectChanges();
      },
    });
  }

  ueByNom(nom: string): void {
    if (!nom) {
      this.ueFiltres = [];
      return;
    }

    this.ueService.getUeByNom(nom).subscribe({
      next: (ue) => {
        this.ueFiltres = ue;
        this.cdr.detectChanges();
      },
    });
  }

  ajouterUe(event: MatAutocompleteSelectedEvent): void {
    const ue: Ue = event.option.value;

    if (this.ueSelectionnes.find((u) => u.id === ue.id)) return;

    this.ueSelectionnes.push(ue);

    this.ueForm.patchValue({ prerequisIds: this.ueSelectionnes.map((u) => u.id) });
  }

  retirerUe(ue: Ue): void {
    this.ueSelectionnes = this.ueSelectionnes.filter((u) => u.id !== ue.id);

    this.ueForm.patchValue({ prerequisIds: this.ueSelectionnes.map((u) => u.id) });
  }

  enseignantByNom(nom_prenom: string): void {
    if (!nom_prenom) {
      this.enseignantFiltres = [];
      return;
    }

    this.enseignantService.rechercherEnseignant(nom_prenom).subscribe({
      next: (ens) => {
        this.enseignantFiltres = ens;
        this.cdr.detectChanges();
      },
    });
  }

  ajouterEnseignant(event: MatAutocompleteSelectedEvent): void {
    const ens: Enseignant = event.option.value;

    if (this.enseignantSelectionnes.find((e) => e.id === ens.id)) return;

    this.enseignantSelectionnes.push(ens);

    this.ueForm.patchValue({ enseignantIds: this.enseignantSelectionnes.map((e) => e.id) });
  }

  retirerEnseignant(ens: Enseignant): void {
    this.enseignantSelectionnes = this.enseignantSelectionnes.filter((e) => e.id !== ens.id);

    this.ueForm.patchValue({ enseignantIds: this.enseignantSelectionnes.map((e) => e.id) });
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

    this.ueForm.patchValue({ tagIds: this.tagsSelectionnes.map((t) => t.id) }); // rempli le form avec la liste des tags selection ( que leurs id via le map)
  }

  retirerTag(tag: Tag): void {
    this.tagsSelectionnes = this.tagsSelectionnes.filter((t) => t.id !== tag.id); // garde seulement les tags qui n'ont pas l'id du tag à retirer

    this.ueForm.patchValue({ tagIds: this.tagsSelectionnes.map((t) => t.id) });
  }
}
