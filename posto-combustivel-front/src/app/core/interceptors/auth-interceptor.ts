import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Se tiver token, clona a requisição e injeta o cabeçalho
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Basic ${token}`,
        'Content-Type': 'application/json',
        // Importante para evitar cache agressivo do Nginx/Browser em requisições API
        'Cache-Control': 'no-cache',
        Pragma: 'no-cache',
      },
    });
    return next(authReq);
  }

  // Se não tiver token (ex: tela de login), passa a requisição original
  return next(req);
};
