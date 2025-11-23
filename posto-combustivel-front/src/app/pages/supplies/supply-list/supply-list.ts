import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
// Ajuste caminhos
import { SupplyService } from '../../../core/services/supply.service';
import { Supply } from '../../../core/models/supply.model';
import { SupplyDialog } from '../supply-dialog/supply-dialog';

@Component({
  selector: 'app-supply-list',
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
  templateUrl: './supply-list.html',
  styleUrl: './supply-list.scss',
})
export class SupplyList implements OnInit {
  private service = inject(SupplyService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'data', 'bomba', 'litros', 'total', 'status', 'actions'];
  dataSource = new MatTableDataSource<Supply>();

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.service.getAll().subscribe({
      next: (data) => {
        // Opcional: Ordenar por ID decrescente (mais novo primeiro)
        this.dataSource.data = data.sort((a, b) => b.id - a.id);
      },
      error: () => this.showSnack('Erro ao carregar registros', 'erro'),
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(SupplyDialog, {
      width: '500px', // Um pouco mais largo por causa das colunas
      panelClass: 'custom-dialog-container',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.service.create(result).subscribe({
          next: () => {
            this.showSnack('Abastecimento registrado com sucesso!', 'sucesso');
            this.loadData();
          },
          error: () => this.showSnack('Erro ao registrar venda.', 'erro'),
        });
      }
    });
  }

  cancelItem(id: number) {
    if (confirm('ATENÇÃO: Deseja realizar o estorno fiscal deste abastecimento?')) {
      this.service.cancel(id).subscribe({
        next: () => {
          this.showSnack('Registro estornado/cancelado.', 'sucesso');
          this.loadData();
        },
        error: (err) => {
          console.error(err);
          this.showSnack(err.error?.message || 'Erro ao cancelar registro.', 'erro');
        },
      });
    }
  }

  private showSnack(msg: string, type: 'sucesso' | 'erro') {
    this.snackBar.open(msg, 'OK', {
      duration: 4000,
      panelClass: type === 'erro' ? ['error-snackbar'] : ['success-snackbar'],
      verticalPosition: 'bottom',
      horizontalPosition: 'center',
    });
  }
}
