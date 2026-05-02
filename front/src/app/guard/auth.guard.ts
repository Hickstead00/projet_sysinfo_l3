import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth-service';
import { map, catchError, of } from 'rxjs';


// Classe qui permet à chaque "moment" de navigation de faire appel a me() du back qui renvoie l'utilisateur courant
// permet notamment de rediriger vers l'accueil un utilisateur qui n'aurait pas les droits, ou vers le login quelqu'un de non connecté
export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.me().pipe(
    map((user) => {
      const roles: string[] | undefined = route.data['roles'];
      if (roles && !roles.includes(user.role)) {
        router.navigate(['/accueil']);
        return false;
      }
      return true;
    }),
    catchError(() => {
      router.navigate(['/login']);
      return of(false);
    })
  );
};
