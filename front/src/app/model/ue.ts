import { Tag } from './tag';
import { Enseignant } from './enseignant';

export interface Ue {
  id: number;
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
  nbMaquettes: number;
  volumeHoraireTotal: number;
}
