import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AsyncPipe } from '@angular/common';
import { AuthService } from '../../service/auth-service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, AsyncPipe],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar implements OnInit {
  utilisateur$!: typeof this.authService.utilisateur$;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.utilisateur$ = this.authService.utilisateur$;
    this.authService.me().subscribe();
  }

  deconnexion() {
    this.authService.logout().subscribe({
      next: () => this.router.navigate(['/login']),
      error: () => this.router.navigate(['/login']),
    });
  }

  getAvatarColor(nomUtilisateur: string): string {
    const couleurs = [
      '#1e3a6e',
      '#16a34a',
      '#7c3aed',
      '#dc2626',
      '#ea580c',
      '#0891b2',
      '#db2777',
      '#059669',
      '#b45309',
      '#0369a1',
      '#7c2d12',
      '#166534',
      '#581c87',
      '#9f1239',
      '#0f766e',
      '#1d4ed8',
      '#92400e',
      '#065f46',
      '#6b21a8',
      '#be123c',
      '#155e75',
      '#14532d',
      '#7e22ce',
      '#0c4a6e',
      '#854d0e',
      '#134e4a',
      '#4c1d95',
      '#881337',
      '#164e63',
      '#15803d',
      '#6d28d9',
      '#b91c1c',
      '#78350f',
      '#0e7490',
      '#7f1d1d',
      '#1e4620',
      '#3730a3',
      '#9d174d',
      '#0a3d62',
      '#1a5276',
    ];
    return couleurs[(nomUtilisateur.charCodeAt(0)) % couleurs.length];
  }
}
