import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  // Gerenciamento de estado reativo
  private _isLoggedIn = signal<boolean>(!!this.getToken());
  isLoggedIn = this._isLoggedIn.asReadonly();

  private readonly TOKEN_KEY = 'hub_br_token';

  constructor(private router: Router) {}

  login(user: string, pass: string): boolean {
    // Validação básica contra o que definimos no .env do Docker
    if (user === 'admin' && pass === '123456') {
      // Cria o hash Base64 (Padrão Basic Auth)
      const token = btoa(`${user}:${pass}`);

      localStorage.setItem(this.TOKEN_KEY, token);
      this._isLoggedIn.set(true);
      this.router.navigate(['/dashboard']);
      return true;
    }
    return false;
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    this._isLoggedIn.set(false);
    this.router.navigate(['/login']);
  }

  // Método auxiliar para o Interceptor ler o token
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }
}
