import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterLink } from '@angular/router';
import { ClienteService } from '../../../core/services/cliente.service';
import { Cliente } from '../../../core/models/cliente.model';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatSnackBarModule,
    RouterLink,
  ],
  templateUrl: './cliente-list.html',
  styleUrl: './cliente-list.scss',
})
export class ClienteList implements OnInit {
  private service = inject(ClienteService);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'nome', 'cpf', 'email', 'status', 'actions'];

  // CORREÇÃO 2: Tipagem forte aqui
  dataSource = new MatTableDataSource<Cliente>();

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.service.getAll().subscribe({
      // CORREÇÃO 3: Tipagem explícita dos dados recebidos
      next: (data: Cliente[]) => {
        this.dataSource.data = data;

        // Predicado de filtro customizado (Enterprise UX)
        this.dataSource.filterPredicate = (data: Cliente, filter: string) => {
          const dataStr = (data.nome + data.cpf + data.email).toLowerCase();
          return dataStr.includes(filter);
        };
      },
      // CORREÇÃO 4: Tipagem explícita do erro
      error: (err: any) => {
        console.error('Erro API:', err);
        this.snackBar.open('Erro ao carregar clientes.', 'Fechar', {
          duration: 5000,
          panelClass: ['error-snackbar'],
        });
      },
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  deleteItem(id: number) {
    if (confirm('Tem certeza que deseja remover este cliente da base?')) {
      this.service.delete(id).subscribe({
        next: () => {
          this.snackBar.open('Cliente removido com sucesso.', 'OK', {
            duration: 3000,
            panelClass: ['success-snackbar'],
          });
          this.loadData(); // Atualiza a tabela
        },
        error: (err: any) => {
          this.snackBar.open('Não foi possível remover (pode ter vínculos).', 'Erro', {
            duration: 4000,
            panelClass: ['error-snackbar'],
          });
        },
      });
    }
  }
}
