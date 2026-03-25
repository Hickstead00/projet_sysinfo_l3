import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ParametresService } from '../../service/parametres-service';
import { Parametres } from '../../model/parametres';

@Component({
  selector: 'app-parametres',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSlideToggleModule,
  ],
  templateUrl: './parametres.html',
  styleUrl: './parametres.scss',
})
export class ParametresComponent implements OnInit {
  parametresForm!: FormGroup;

  parametresChargees?: Parametres;

  messageErreur?: string;

  messageSucces?: string;

  sectionActive: string = 'section-ects';

  constructor(
    private parametresService: ParametresService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.chargerParametres();
  }

  initForm(): void {
    this.parametresForm = this.fb.group({
      tarifCm: new FormControl<number>(45, {
        nonNullable: true,
        validators: [Validators.required, Validators.min(0)],
      }),
      tarifTd: new FormControl<number>(37.5, {
        nonNullable: true,
        validators: [Validators.required, Validators.min(0)],
      }),
      tarifTp: new FormControl<number>(37.5, {
        nonNullable: true,
        validators: [Validators.required, Validators.min(0)],
      }),
      budgetMax: new FormControl<number>(0, {
        nonNullable: true,
        validators: [Validators.required, Validators.min(0)],
      }),
      alertesEctsActives: new FormControl<boolean>(true, { nonNullable: true }),
      alertesPrerequisActives: new FormControl<boolean>(true, { nonNullable: true }),
    });
  }

  chargerParametres(): void {
    this.parametresService.getParametres().subscribe({
      next: (parametres) => {
        this.parametresChargees = parametres;
        this.parametresForm.patchValue(parametres);
        this.cdr.detectChanges();
      },
      error: () => {
        this.messageErreur = 'Impossible de charger les paramètres.';
        this.cdr.detectChanges();
      },
    });
  }

  enregistrer(): void {
    if (this.parametresForm.invalid) return;

    this.messageErreur = undefined;
    this.messageSucces = undefined;

    const parametres: Parametres = this.parametresForm.value;

    this.parametresService.updateParametres(parametres).subscribe({
      next: (parametresMisAJour) => {
        this.parametresChargees = parametresMisAJour;
        this.messageSucces = 'Paramètres enregistrés avec succès !';
        this.cdr.detectChanges();
      },
      error: (e) => {
        if (e.status === 400) {
          this.messageErreur = 'Données invalides.';
        } else {
          this.messageErreur = 'Une erreur est survenue lors de la sauvegarde.';
        }
        this.cdr.detectChanges();
      },
    });
  }

  scrollVers(id: string): void {
    this.sectionActive = id;
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' });
  }

  reinitialiser(): void {
    if (this.parametresChargees) {
      this.parametresForm.patchValue(this.parametresChargees);
      this.messageErreur = undefined;
      this.messageSucces = undefined;
      this.cdr.detectChanges();
    }
  }
}
