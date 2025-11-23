import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon'; // Adicionado Icon
import { AuthService } from '../../core/auth/auth.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet } from '@angular/router';
import { MatSidenavContainer, MatSidenav, MatSidenavContent } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatToolbar } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { Footer } from '../../layout/footer/footer'; // Geralmente se redireciona após login

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatListModule,
    MatMenuModule,
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private snack = inject(MatSnackBar);
  private router = inject(Router); // Injetar Router se precisar redirecionar

  hidePassword = true; // Controle de visibilidade da senha
  isLoading = false; // Estado de carregamento

  form = this.fb.group({
    user: ['admin', Validators.required],
    pass: ['123456', Validators.required],
  });

  onSubmit() {
    if (this.form.valid) {
      this.isLoading = true;
      const { user, pass } = this.form.value;

      // Simulando um pequeno delay para mostrar o loading (melhor UX)
      // Se seu auth.login for síncrono, pode remover o setTimeout
      setTimeout(() => {
        const logged = this.auth.login(user!, pass!);

        if (!logged) {
          this.snack.open('Usuário ou senha incorretos', 'Tentar Novamente', {
            duration: 4000,
            panelClass: ['error-snackbar'], // Usando a classe global que criamos
          });
          this.isLoading = false;
        } else {
          // Sucesso! O Router geralmente redireciona dentro do AuthService ou aqui
          // this.router.navigate(['/dashboard']);
          this.isLoading = false;
        }
      }, 600);
    } else {
      this.form.markAllAsTouched();
    }
  }
}
