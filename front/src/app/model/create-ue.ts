import { Enseignant } from './enseignant';
import { Tag } from './tag';
import { Ue } from './ue';

export interface CreateUe {
  nomUe: string;
  ects: number;
  cm: number;
  td: number;
  tp: number;
  description: string;
  ueObligatoire: boolean;
  tags: Tag[];
  enseignants: Enseignant[];
  referents: Enseignant[];
  prerequis: Ue[];
  nbEnseignants: number;
  nbReferents: number;
  nbPrerequis: number;
  nbSemestres: number;
  volumeHoraireTotal: number;
}
