import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
// Ajuste os caminhos se necessário
import { FuelTypeService } from '../../../core/services/fuel-type.service';
import { FuelTypeDialog } from '../fuel-type-dialog/fuel-type-dialog';
import { FuelType } from '../../../core/models/fuel-type.model';

@Component({
  selector: 'app-fuel-type-list',
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
    CurrencyPipe,
  ],
  templateUrl: './fuel-type-list.html',
  styleUrl: './fuel-type-list.scss',
})
export class FuelTypeList implements OnInit {
  private service = inject(FuelTypeService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  displayedColumns: string[] = ['id', 'nome', 'precoLitro', 'actions'];
  dataSource = new MatTableDataSource<FuelType>();

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.service.getAll().subscribe({
      next: (data) => (this.dataSource.data = data),
      error: (err) => this.showSnack('Erro ao carregar dados', 'erro'),
    });
  }

  openDialog(fuelType?: FuelType) {
    const dialogRef = this.dialog.open(FuelTypeDialog, {
      width: '450px', // Levemente mais largo para conforto
      panelClass: 'custom-dialog-container', // Opcional, para CSS global
      data: fuelType || null,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        const action = result.id
          ? this.service.update(result.id, result)
          : this.service.create(result);

        action.subscribe({
          next: () => {
            this.showSnack('Registro salvo com sucesso!');
            this.loadData();
          },
          error: () => this.showSnack('Erro ao salvar registro.', 'erro'),
        });
      }
    });
  }

  deleteItem(id: number) {
    // Dica: No futuro, podemos criar um Dialog de Confirmação estilizado igual ao Form
    if (confirm('Tem certeza que deseja excluir este item permanentemente?')) {
      this.service.delete(id).subscribe({
        next: () => {
          this.showSnack('Registro excluído com sucesso.');
          this.loadData();
        },
        error: (err) => {
          console.error(err);
          this.showSnack('Não é possível excluir (registro em uso).', 'erro');
        },
      });
    }
  }

  private showSnack(msg: string, tipo: 'sucesso' | 'erro' = 'sucesso') {
    this.snackBar.open(msg, 'OK', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: tipo === 'erro' ? ['error-snackbar'] : ['success-snackbar'],
    });
  }
}
