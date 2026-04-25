import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../service/auth-service';
import { AuthResponse } from '../../model/auth-response';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar implements OnInit {
  utilisateur: AuthResponse | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.authService.me().subscribe({
      next: (user) => this.utilisateur = user,
      error: () => this.utilisateur = null,
    });
  }

  deconnexion() {
    this.authService.logout().subscribe(() => {
      this.utilisateur = null;
      this.router.navigate(['/login']);
    });
  }
}
