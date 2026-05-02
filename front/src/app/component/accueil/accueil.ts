import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth-service';
import { AuthResponse } from '../../model/auth-response';

@Component({
  selector: 'app-accueil',
  templateUrl: './accueil.html',
  styleUrl: './accueil.scss',
})
export class AccueilComponent implements OnInit {
  utilisateur: AuthResponse | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.me().subscribe({
      next: (user) => this.utilisateur = user,
    });
  }
}
