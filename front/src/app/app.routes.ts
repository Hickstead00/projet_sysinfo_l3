import { Routes } from '@angular/router';
import { GestionTags } from './component/gestion-tags/gestion-tags';
import { Enseignants } from './component/enseignants/enseignants';
import { UEComponent } from './component/ue/ue';
import { ParametresComponent } from './component/parametres/parametres';
import { MaquettesComponent } from './component/maquettes/maquettes';

export const routes: Routes = [
  { path: 'gestionTags', component: GestionTags },
  { path: 'enseignants', component: Enseignants },
  { path: 'ue', component: UEComponent },
  { path: 'parametres', component: ParametresComponent },
  { path: 'maquettes', component: MaquettesComponent },
];
