import { Routes } from '@angular/router';
import { GestionTags } from './component/gestion-tags/gestion-tags';
import { Enseignants } from './component/enseignants/enseignants';
import { UEComponent } from './component/ue/ue';
import { ParametresComponent } from './component/parametres/parametres';
import { MaquettesComponent } from './component/maquettes/maquettes';
import { LoginComponent } from './component/login/login';
import { authGuard } from './guard/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'gestionTags', component: GestionTags, canActivate: [authGuard] },
  { path: 'enseignants', component: Enseignants, canActivate: [authGuard] },
  { path: 'ue', component: UEComponent, canActivate: [authGuard] },
  { path: 'parametres', component: ParametresComponent, canActivate: [authGuard] },
  { path: 'maquettes', component: MaquettesComponent, canActivate: [authGuard] },
  { path: '', redirectTo: 'maquettes', pathMatch: 'full' },
];
