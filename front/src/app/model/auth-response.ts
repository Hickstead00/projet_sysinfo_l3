export interface AuthResponse {
  email: string;
  nomUtilisateur: string;
  role: 'ADMIN' | 'SECRETAIRE' | 'RESPONSABLE';
}
