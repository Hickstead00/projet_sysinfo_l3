import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth-service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent {
  email = '';
  motDePasse = '';
  erreur = '';

  constructor(private authService: AuthService, private router: Router) {}

  // Petite méthode auxiliaire permettant de remplir les champs avec les données fixes de la page pour la démo
  remplirDemo(email: string, motDePasse: string) {
    this.email = email;
    this.motDePasse = motDePasse;
  }

  onSubmit() {
    this.erreur = '';
    this.authService.login(this.email, this.motDePasse).subscribe({
      next: () => this.router.navigate(['/accueil']),
      error: () => this.erreur = 'Email ou mot de passe incorrect.',
    });
  }
}
