import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
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
import { MaquetteService } from '../../service/maquette-service';
import { Maquette } from '../../model/maquette';
import { CreateMaquette } from '../../model/create-maquette';

@Component({
  selector: 'app-maquettes',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './maquettes.html',
  styleUrl: './maquettes.scss',
})
export class MaquettesComponent implements OnInit {
  maquetteForm!: FormGroup;
  maquettesExistantes: Maquette[] = [];
  maquetteActive?: Maquette;
  typeSelectionne?: 'LICENCE' | 'MASTER';
  messageErreur?: string;
  showModale = true;

  constructor(
    private maquetteService: MaquetteService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.chargerMaquettes();
  }

  initForm(): void {
    this.maquetteForm = this.fb.group({
      nomMaquette: new FormControl<string>('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
    });
  }

  chargerMaquettes(): void {
    this.maquetteService.getAllMaquettes().subscribe({
      next: (maquettes) => {
        this.maquettesExistantes = maquettes;
        this.cdr.detectChanges();
      },
    });
  }

  choisirType(type: 'LICENCE' | 'MASTER'): void {
    this.typeSelectionne = type;
  }

  creerMaquette(): void {
    if (this.maquetteForm.invalid || !this.typeSelectionne) return;

    const request: CreateMaquette = {
      nomMaquette: this.maquetteForm.value.nomMaquette,
      typeMaquette: this.typeSelectionne,
    };

    this.messageErreur = undefined;

    this.maquetteService.createMaquette(request).subscribe({
      next: (maquette) => {
        this.maquetteActive = maquette;
        this.showModale = false;
        this.cdr.detectChanges();
      },
      error: (e) => {
        if (e.status === 409) {
          this.messageErreur = 'Une maquette avec ce nom existe déjà.';
        } else if (e.status === 400) {
          this.messageErreur = 'Données invalides.';
        }
        this.cdr.detectChanges();
      },
    });
  }

  ouvrirMaquette(maquette: Maquette): void {
    this.maquetteActive = maquette;
    this.showModale = false;
    this.cdr.detectChanges();
  }

  supprimerMaquette(id: number, event: Event): void {
    event.stopPropagation();
    this.maquetteService.deleteMaquette(id).subscribe({
      next: () => {
        this.chargerMaquettes();
      },
    });
  }
}
