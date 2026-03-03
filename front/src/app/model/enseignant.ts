import { Tag } from './tag';

export interface Enseignant {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  tags: Tag[];
  nbUe: number;
  nbReferent: number;
  totalCm: number;
  totalTp: number;
  totalTd: number;
}
