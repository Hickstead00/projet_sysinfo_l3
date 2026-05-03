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
import { ViewChild, ElementRef } from '@angular/core';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';

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
    MatPaginatorModule,
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

  rechercheEffectue: boolean = false;

  @ViewChild('ensInput') ensInput!: ElementRef<HTMLInputElement>; // permet de referencer l'element html pour pouvoir le manipuler dans le ts

  @ViewChild('tagInput') tagInput!: ElementRef<HTMLInputElement>;

  @ViewChild('ueInput') ueInput!: ElementRef<HTMLInputElement>;

  messageErreurCreation?: string;

  currentPage = 0;

  ueByPage = 11;

  totalUe = 0;

  uePage: Ue[] = [];

  ueEnCours?: Ue;

  referentSelectionne: number | null = null;

  tousLesEnseignants: Enseignant[] = [];

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
      next: (ens) => {
        this.enseignantDispo = ens;
        this.tousLesEnseignants = ens;
      },
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
      referentIds: new FormControl<number[]>([], {
        nonNullable: true,
        validators: [Validators.required],
      }),
      prerequisIds: new FormControl<number[]>([]),
    });
  }

  allUe(): void {
    this.ueService.getAllUe().subscribe({
      next: (ue) => {
        this.listUe = ue;
        this.totalUe = ue.length;
        this.updateUeByPage();
        this.cdr.detectChanges();
      },
    });
  }

  createModifUe(): void {
    if (this.ueForm.invalid) {
      this.messageErreurCreation = 'Un intitulé et responsable sont obligatoires';
      this.cdr.detectChanges();
      return;
    }

    this.messageErreurCreation = undefined;
    const ue: CreateUe = this.ueForm.value;

    const modif = this.ueEnCours
      ? this.ueService.modifierUe(this.ueEnCours.id, ue)
      : this.ueService.createUe(ue);

    modif.subscribe({
      next: () => {
        this.allUe();
        this.ueForm.reset();
        this.referentSelectionne = null;
        this.enseignantSelectionnes = [];
        this.tagsSelectionnes = [];
        this.ueSelectionnes = [];
        this.ueEnCours = undefined;
        this.cdr.detectChanges();
      },
      error: (e) => {
        if (e.status === 400) {
          this.messageErreurCreation = 'Données invalides';
        } else if (e.status === 409) {
          this.messageErreurCreation = 'Une UE avec ce nom existe déjà';
        }
        this.cdr.detectChanges();
      },
    });
  }

  ueByNom(nom: string): void {
    this.rechercheEffectue = false;

    if (!nom) {
      this.ueFiltres = [];
      return;
    }

    this.ueService.getUeByNom(nom).subscribe({
      next: (ue) => {
        this.ueFiltres = ue;
        this.rechercheEffectue = true;
        this.cdr.detectChanges();
      },
    });
  }

  deleteUeById(id: number): void {
    this.ueService.deleteUe(id).subscribe({
      next: () => {
        this.rechercheEffectue = false;
        this.ueFiltres = [];
        this.allUe();
        this.cdr.detectChanges();
      },

      error: () => {
        {
          console.log('Erreur detecté par le backend');
        }
      },
    });
  }

  retirerUe(ue: Ue): void {
    this.ueSelectionnes = this.ueSelectionnes.filter((u) => u.id !== ue.id); // garde uniquement les ue dont l'id est différente l'ue a retirer

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
    //  méthode appelée quand cliques sur une option dans l'autocomplete des enseignants
    const ens: Enseignant = event.option.value; // récupère l'objet enseignant complet depuis l'option cliquée
    if (this.enseignantSelectionnes.find((e) => e.id === ens.id)) return; // si enseignant dans la liste on arrête
    this.enseignantSelectionnes.push(ens);
    this.ueForm.patchValue({ enseignantIds: this.enseignantSelectionnes.map((e) => e.id) }); // met a jour le formulaire avec uniquement les ens selectionné
    this.ensInput.nativeElement.value = '';
    this.enseignantFiltres = [];
  }

  ajouterTag(event: MatAutocompleteSelectedEvent): void {
    const tag: Tag = event.option.value;
    if (this.tagsSelectionnes.find((t) => t.id === tag.id)) return;
    this.tagsSelectionnes.push(tag);
    this.ueForm.patchValue({ tagIds: this.tagsSelectionnes.map((t) => t.id) });
    this.tagInput.nativeElement.value = '';
    this.tagsFiltres = [];
    this.filtrerEnseignantsParTags();
  }

  ajouterUe(event: MatAutocompleteSelectedEvent): void {
    const ue: Ue = event.option.value;
    if (this.ueSelectionnes.find((u) => u.id === ue.id)) return;
    this.ueSelectionnes.push(ue);
    this.ueForm.patchValue({ prerequisIds: this.ueSelectionnes.map((u) => u.id) });
    this.ueInput.nativeElement.value = '';
    this.ueFiltres = [];
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

  retirerTag(tag: Tag): void {
    this.tagsSelectionnes = this.tagsSelectionnes.filter((t) => t.id !== tag.id); // garde seulement les tags qui n'ont pas l'id du tag à retirer

    this.ueForm.patchValue({ tagIds: this.tagsSelectionnes.map((t) => t.id) });
    this.filtrerEnseignantsParTags();
  }

  filtrerEnseignantsParTags(): void {
    if (this.tagsSelectionnes.length === 0) {
      this.enseignantDispo = [...this.tousLesEnseignants]; // si aucun tag selectionné remet tous les enseignants dispo. ... cree une copie du tableau
      return;
    }

    this.enseignantDispo = this.tousLesEnseignants.filter((ens) =>
      ens.tags.some((tag) => this.tagsSelectionnes.find((t) => t.id === tag.id)),
    );

    // recreer enseignantDispo et regarde si au moins un tag est selectionné (some = true dès qu'une cond et verif)
  }

  updateUeByPage(): void {
    const start = this.currentPage * this.ueByPage; // calcule l'index de début de la page courante
    const end = start + this.ueByPage; // calcule l'index de fin de la page courante
    this.uePage = (this.rechercheEffectue ? this.ueFiltres : this.listUe).slice(start, end); // choisit le bon tableau selon si une recherche est effectuée ou non
    // puis extrait uniquement les UE entre start et end pour la page courante
  }

  pageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.updateUeByPage();
  }

  updateUeForm(ue: Ue): void {
    this.ueEnCours = ue;
    this.referentSelectionne = ue.referents?.[0]?.id ?? null;
    this.ueForm.patchValue({
      nomUe: ue.nomUe,
      ects: ue.ects,
      cm: ue.cm,
      td: ue.td,
      tp: ue.tp,
      description: ue.description,
      ueObligatoire: ue.ueObligatoire,
      tagIds: ue.tags.map((t) => t.id),
      enseignantIds: ue.enseignants.map((e) => e.id),
      referentIds: ue.referents.map((r) => r.id),
      prerequisIds: ue.prerequis.map((u) => u.id),
    });
    this.tagsSelectionnes = [...ue.tags];
    this.enseignantSelectionnes = [...ue.enseignants];
    this.ueSelectionnes = [...ue.prerequis];
  }

  annuler(): void {
    this.ueForm.reset();
    this.referentSelectionne = null;
    this.enseignantSelectionnes = [];
    this.tagsSelectionnes = [];
    this.ueSelectionnes = [];
    this.ueEnCours = undefined;
    this.messageErreurCreation = undefined;
  }
}
