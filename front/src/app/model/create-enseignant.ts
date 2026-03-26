  import { Tag } from './tag';

export interface CreateEnseignant {
  nom: string;
  prenom: string;
  email: string;
  tags: Tag[];
}
