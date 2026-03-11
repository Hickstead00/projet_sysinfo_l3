import { Routes } from '@angular/router';
import { GestionTags } from './component/gestion-tags/gestion-tags';
import { Enseignants } from './component/enseignants/enseignants';
import { UE } from './component/ue/ue';

export const routes: Routes = [
  { path: 'gestionTags', component: GestionTags },
  { path: 'enseignants', component: Enseignants },
  { path: 'ue', component: UE },
];
