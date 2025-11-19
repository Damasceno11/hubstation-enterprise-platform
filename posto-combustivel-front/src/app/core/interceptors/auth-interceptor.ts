import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authHeader = 'Basic ' + btoa('admin:123456');

  const authReq = req.clone({
    setHeaders: {
      Authorization: authHeader,
      'Content-Type': 'application/json',
    },
  });

  return next(authReq);
};
