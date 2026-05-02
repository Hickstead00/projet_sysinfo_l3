import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';


// Classe qui sert à injecter automatiquement le cookie de session à chaque requête HTTP
// L'interceptor clone la requête en ajoutant withCredential: true, si 200 -> le composant reçoit les données, sinon on redirige vers login
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  const reqAvecCredentials = req.clone({ withCredentials: true });

  return next(reqAvecCredentials).pipe(
    catchError((error) => {
      if (error.status === 401 && !req.url.includes('/api/auth/')) {
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
