import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ClienteService } from '../../../core/services/cliente.service';
import { ClienteCadastro } from '../../../core/models/cliente.model';

@Component({
  selector: 'app-cadastro-fidelidade',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
  ],
  templateUrl: './cliente-form.html',
  styleUrl: './cliente-form.scss',
})
export class CadastroFidelidade {
  private fb = inject(FormBuilder);
  private clienteService = inject(ClienteService);
  private snackBar = inject(MatSnackBar);

  form: FormGroup = this.fb.group({
    nome: ['', [Validators.required, Validators.minLength(3)]],
    cpf: ['', [Validators.required, Validators.pattern(/^\d{11}$/)]],
    email: ['', [Validators.required, Validators.email]],
  });

  isLoading = false;

  onSubmit() {
    if (this.form.valid) {
      this.isLoading = true;
      const dados: ClienteCadastro = this.form.value;

      this.clienteService.create(dados).subscribe({
        next: (res) => {
          this.mostrarNotificacao('Cliente cadastrado com sucesso!', 'sucesso');
          this.isLoading = false;
          this.form.reset();
          // Remove erros visuais dos campos após resetar
          Object.keys(this.form.controls).forEach((key) => {
            this.form.get(key)?.setErrors(null);
          });
        },
        error: (err) => {
          console.error(err);
          const msg = err.error?.message || 'Erro ao realizar cadastro.';
          this.mostrarNotificacao(msg, 'erro');
          this.isLoading = false;
        },
      });
    } else {
      this.form.markAllAsTouched();
    }
  }

  private mostrarNotificacao(mensagem: string, tipo: 'sucesso' | 'erro') {
    this.snackBar.open(mensagem, 'Fechar', {
      duration: 4000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: tipo === 'erro' ? ['error-snackbar'] : ['success-snackbar'],
    });
  }
}
