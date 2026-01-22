import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface TestMessage {
  id: number;
  content: string;
  createdAt: string;
}

interface PingResponse {
  status: string;
  message: string;
}

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div style="padding: 20px; font-family: Arial, sans-serif;">
      <h1>🧪 Test de connexion Full Stack</h1>

      <!-- Test 1: Ping API -->
      <section style="margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 8px;">
        <h2>1. Test API Backend</h2>
        <button (click)="testPing()" style="padding: 10px 20px; cursor: pointer;">
          Ping Backend
        </button>
        <p *ngIf="pingResult" [style.color]="pingResult.status === 'OK' ? 'green' : 'red'">
          ✅ {{ pingResult.message }}
        </p>
        <p *ngIf="pingError" style="color: red;">❌ {{ pingError }}</p>
      </section>

      <!-- Test 2: Envoyer un message -->
      <section style="margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 8px;">
        <h2>2. Envoyer un message en BD</h2>
        <input
          type="text"
          [(ngModel)]="newMessage"
          placeholder="Votre message..."
          style="padding: 10px; width: 300px; margin-right: 10px;"
        />
        <button (click)="sendMessage()" style="padding: 10px 20px; cursor: pointer;">
          Envoyer
        </button>
        <p *ngIf="sendSuccess" style="color: green;">✅ Message envoyé et sauvegardé !</p>
        <p *ngIf="sendError" style="color: red;">❌ {{ sendError }}</p>
      </section>

      <!-- Test 3: Afficher les messages -->
      <section style="margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 8px;">
        <h2>3. Messages en base de données</h2>
        <button (click)="loadMessages()" style="padding: 10px 20px; cursor: pointer; margin-right: 10px;">
          Charger les messages
        </button>
        <button (click)="deleteAllMessages()" style="padding: 10px 20px; cursor: pointer; background: #ff4444; color: white; border: none;">
          Supprimer tout
        </button>

        <div *ngIf="messages.length > 0" style="margin-top: 15px;">
          <div *ngFor="let msg of messages" style="padding: 10px; margin: 5px 0; background: #f5f5f5; border-radius: 4px;">
            <strong>#{{ msg.id }}</strong> - {{ msg.content }}
            <small style="color: #666; margin-left: 10px;">{{ msg.createdAt }}</small>
          </div>
        </div>
        <p *ngIf="messages.length === 0 && messagesLoaded" style="color: #666;">
          Aucun message en base.
        </p>
      </section>

      <!-- Résumé -->
      <section style="margin: 20px 0; padding: 15px; background: #e8f5e9; border-radius: 8px;">
        <h2>📊 Résumé des tests</h2>
        <ul>
          <li>Frontend Angular: ✅ Fonctionne (vous voyez cette page)</li>
          <li>Backend Spring: {{ pingResult?.status === 'OK' ? '✅ Connecté' : '⏳ Non testé' }}</li>
          <li>Base de données: {{ dbTested ? '✅ Connectée' : '⏳ Non testée' }}</li>
        </ul>
      </section>
    </div>
  `
})
export class TestComponent implements OnInit {
  pingResult: PingResponse | null = null;
  pingError: string = '';

  newMessage: string = '';
  sendSuccess: boolean = false;
  sendError: string = '';

  messages: TestMessage[] = [];
  messagesLoaded: boolean = false;
  dbTested: boolean = false;

  private apiUrl = 'http://localhost:8080/api/test';

  constructor(private http: HttpClient) {}

  ngOnInit() {}

  testPing() {
    this.pingError = '';
    this.pingResult = null;

    this.http.get<PingResponse>(`${this.apiUrl}/ping`).subscribe({
      next: (res) => this.pingResult = res,
      error: (err) => this.pingError = 'Impossible de contacter le backend: ' + err.message
    });
  }

  sendMessage() {
    if (!this.newMessage.trim()) return;

    this.sendSuccess = false;
    this.sendError = '';

    this.http.post<TestMessage>(`${this.apiUrl}/messages`, { content: this.newMessage }).subscribe({
      next: () => {
        this.sendSuccess = true;
        this.dbTested = true;
        this.newMessage = '';
        this.loadMessages();
      },
      error: (err) => this.sendError = 'Erreur: ' + err.message
    });
  }

  loadMessages() {
    this.http.get<TestMessage[]>(`${this.apiUrl}/messages`).subscribe({
      next: (res) => {
        this.messages = res;
        this.messagesLoaded = true;
        this.dbTested = true;
      },
      error: (err) => console.error(err)
    });
  }

  deleteAllMessages() {
    this.http.delete(`${this.apiUrl}/messages`).subscribe({
      next: () => {
        this.messages = [];
        this.messagesLoaded = true;
      },
      error: (err) => console.error(err)
    });
  }
}
