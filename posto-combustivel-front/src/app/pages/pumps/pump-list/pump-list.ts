import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { PumpService } from '../../../core/services/pump.service';
import { Pump } from '../../../core/models/pump.model';
import { PumpDialog } from '../pump-dialog/pump-dialog';

@Component({
  selector: 'app-pump-list',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatSnackBarModule,
    MatCardModule,
    MatTooltipModule,
  ],
  templateUrl: './pump-list.html',
  styleUrl: './pump-list.scss',
})
export class PumpList implements OnInit {
  private service = inject(PumpService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'nome', 'combustivel', 'actions'];
  dataSource = new MatTableDataSource<Pump>();

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.service.getAll().subscribe({
      next: (data) => (this.dataSource.data = data),
      error: (err) => this.showSnack('Erro ao carregar bombas.', 'erro'),
    });
  }

  openDialog(pump?: Pump) {
    const dialogRef = this.dialog.open(PumpDialog, {
      width: '450px',
      panelClass: 'custom-dialog-container',
      data: pump || null,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        const action = result.id
          ? this.service.update(result.id, result)
          : this.service.create(result);

        action.subscribe({
          next: () => {
            this.showSnack('Bomba salva com sucesso!', 'sucesso');
            this.loadData();
          },
          error: () => this.showSnack('Erro ao salvar configuração.', 'erro'),
        });
      }
    });
  }

  deleteItem(id: number) {
    if (confirm('Deseja desativar esta bomba permanentemente?')) {
      this.service.delete(id).subscribe({
        next: () => {
          this.showSnack('Bomba removida do sistema.', 'sucesso');
          this.loadData();
        },
        error: () => this.showSnack('Não foi possível remover.', 'erro'),
      });
    }
  }

  // Corrigido o argumento opcional para default
  private showSnack(msg: string, type: 'sucesso' | 'erro' = 'sucesso') {
    this.snackBar.open(msg, 'OK', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: type === 'erro' ? ['error-snackbar'] : ['success-snackbar'],
    });
  }
}
