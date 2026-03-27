import { ChangeDetectorRef, Component, NgZone, OnInit } from '@angular/core';
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
import {
  CdkDragDrop,
  DragDropModule,
} from '@angular/cdk/drag-drop';
import { MaquetteService } from '../../service/maquette-service';
import { UeService } from '../../service/ue-service';
import { ParametresService } from '../../service/parametres-service';
import { Maquette } from '../../model/maquette';
import { CreateMaquette } from '../../model/create-maquette';
import { Ue } from '../../model/ue';
import { Parametres } from '../../model/parametres';
import { Semestre } from '../../model/semestre';

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
    DragDropModule,
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
  isDragging = false;
  semestreSurvoleId?: number;
  erreurSemestreId?: number;
  erreurSemestreMessage?: string;
  private erreurTimeout?: ReturnType<typeof setTimeout>;

  bibliothequeUe: Ue[] = [];
  bibliothequeFiltre: Ue[] = [];
  parametres?: Parametres;
  anneeActive = 0;

  constructor(
    private maquetteService: MaquetteService,
    private ueService: UeService,
    private parametresService: ParametresService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone,
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
        this.chargerPageMaquette();
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
    this.chargerPageMaquette();
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

  chargerPageMaquette(): void {
    this.ueService.getAllUe().subscribe({
      next: (ues) => {
        this.bibliothequeUe = ues;
        this.bibliothequeFiltre = ues;
        this.cdr.detectChanges();
      },
    });

    this.parametresService.getParametres().subscribe({
      next: (params) => {
        this.parametres = params;
        this.cdr.detectChanges();
      },
    });
  }

  filtrerBibliotheque(valeur: string): void {
    if (!valeur) {
      this.bibliothequeFiltre = this.bibliothequeUe;
    } else {
      const recherche = valeur.toLowerCase();
      this.bibliothequeFiltre = this.bibliothequeUe.filter((ue) =>
        ue.nomUe.toLowerCase().includes(recherche),
      );
    }
  }

  choisirAnnee(index: number): void {
    this.anneeActive = index;
  }

  getAnnees(): string[] {
    if (!this.maquetteActive) return [];
    if (this.maquetteActive.typeMaquette === 'LICENCE') {
      return ['L1', 'L2', 'L3'];
    }
    return ['M1', 'M2'];
  }

  getSemestresAnnee(): Semestre[] {
    if (!this.maquetteActive) return [];
    const debut = this.anneeActive * 2;
    return this.maquetteActive.semestres
      .filter((s) => s.numeroSemestre === debut + 1 || s.numeroSemestre === debut + 2)
      .sort((a, b) => a.numeroSemestre - b.numeroSemestre);
  }

  getCout(type: 'cm' | 'td' | 'tp'): number {
    if (!this.maquetteActive || !this.parametres) return 0;
    const tarif =
      type === 'cm'
        ? this.parametres.tarifCm
        : type === 'td'
          ? this.parametres.tarifTd
          : this.parametres.tarifTp;
    const heures = this.maquetteActive.semestres
      .flatMap((s) => s.ues)
      .reduce((sum, ue) => sum + ue[type], 0);
    return heures * tarif;
  }

  getVolumeTotal(type: 'cm' | 'td' | 'tp'): number {
    if (!this.maquetteActive) return 0;
    return this.maquetteActive.semestres
      .flatMap((s) => s.ues)
      .reduce((sum, ue) => sum + ue[type], 0);
  }

  isUePlacee(ue: Ue): boolean {
    if (!this.maquetteActive) return false;
    return this.maquetteActive.semestres.some((s) =>
      s.ues.some((u) => u.id === ue.id),
    );
  }

  getDropListIds(): string[] {
    return this.getSemestresAnnee().map((s) => 'semestre-' + s.id);
  }

  onDragStarted(): void {
    document.body.classList.add('dragging-active');
    this.isDragging = true;
  }

  onDragEnded(): void {
    document.body.classList.remove('dragging-active');
    this.isDragging = false;
    this.semestreSurvoleId = undefined;
  }

  onDropListEntered(semestreId: number): void {
    this.semestreSurvoleId = semestreId;
  }

  onDrop(event: CdkDragDrop<Ue[]>, semestre: Semestre): void {
    if (!this.maquetteActive) return;

    const ue: Ue = event.item.data;

    this.ngZone.run(() => {
      this.maquetteService
        .addUeToSemestre(this.maquetteActive!.id, semestre.id, ue.id)
        .subscribe({
          next: () => this.rafraichirMaquette(),
          error: (e) => {
            if (e.status === 409) {
              this.afficherErreurSemestre(semestre.id, `"${ue.nomUe}" est déjà placée dans cette maquette.`);
            } else if (e.status === 422) {
              this.afficherErreurSemestre(semestre.id, e.error?.message || `Prérequis non respectés pour "${ue.nomUe}".`);
            }
          },
        });
    });
  }

  retirerUe(semestre: Semestre, ue: Ue): void {
    if (!this.maquetteActive) return;

    this.ngZone.run(() => {
      this.maquetteService
        .removeUeFromSemestre(this.maquetteActive!.id, semestre.id, ue.id)
        .subscribe({
          next: () => this.rafraichirMaquette(),
          error: (e) => {
            if (e.status === 422) {
              this.afficherErreurSemestre(semestre.id, e.error?.message || `Impossible de retirer "${ue.nomUe}" : des UE en dépendent.`);
            }
          },
        });
    });
  }

  private afficherErreurSemestre(semestreId: number, message: string): void {
    if (this.erreurTimeout) clearTimeout(this.erreurTimeout);
    this.erreurSemestreId = semestreId;
    this.erreurSemestreMessage = message;
    this.cdr.detectChanges();
    this.erreurTimeout = setTimeout(() => {
      this.erreurSemestreId = undefined;
      this.erreurSemestreMessage = undefined;
      this.cdr.detectChanges();
    }, 3000);
  }

  private rafraichirMaquette(): void {
    if (!this.maquetteActive) return;

    this.ngZone.run(() => {
      this.maquetteService.getMaquetteById(this.maquetteActive!.id).subscribe({
        next: (maquette) => {
          this.maquetteActive = maquette;
          this.cdr.detectChanges();
        },
      });
    });
  }
}
