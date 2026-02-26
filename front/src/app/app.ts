import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './component/navbar/navbar';
import { GestionTags } from './component/gestion-tags/gestion-tags';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,Navbar,GestionTags],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('front');
}
