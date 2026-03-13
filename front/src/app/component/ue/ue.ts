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
import { ThumbPosition } from '@angular/material/slider/testing';

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
export class UE implements OnInit {
  listUe: UE[] = [];

  ueForm!: FormGroup;

  enseignantDispo: Enseignant[] = [];

  enseignantSelectionnes: Enseignant[] = [];

  enseignantFiltres: Enseignant[] = [];

  constructor(
    private ueService: UeService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private enseignantService: EnseignantService,
  ) {}

  ngOnInit(): void {
    this.allUe();
    this.initForm();
    this.enseignantService.getAllEnseignant().subscribe({
      next: (ens) => (this.enseignantDispo = ens),
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
      ueObligatoire: new FormControl<boolean>(false, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      tags: new FormControl<Tag[]>([]),
      enseignants: new FormControl<Enseignant[]>([]),
      referents: new FormControl<Enseignant[]>([]),
      prerequis: new FormControl<UE[]>([]),
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

    this.ueForm.patchValue({ enseignants: this.enseignantSelectionnes.map((e) => e.id) });
  }

  retirerEnseignant(ens: Enseignant): void {
    this.enseignantSelectionnes = this.enseignantSelectionnes.filter((e) => e.id !== ens.id);

    this.ueForm.patchValue({ enseignants: this.enseignantSelectionnes.map((e) => e.id) });
  }
}
