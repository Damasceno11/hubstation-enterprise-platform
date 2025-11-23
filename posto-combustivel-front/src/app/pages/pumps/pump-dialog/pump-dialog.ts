import { CommonModule } from '@angular/common';
import { Component, Inject, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
// Ajuste os caminhos conforme seu projeto
import { FuelTypeService } from '../../../core/services/fuel-type.service';

import { Pump } from '../../../core/models/pump.model';
import { FuelType } from '../../../core/models/fuel-type.model';

@Component({
  selector: 'app-pump-dialog',
  standalone: true, // Importante: standalone
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
  ],
  templateUrl: './pump-dialog.html',
  styleUrl: './pump-dialog.scss',
})
export class PumpDialog implements OnInit {
  private fb = inject(FormBuilder);
  private fuelService = inject(FuelTypeService);

  fuelTypes: FuelType[] = [];

  form: FormGroup = this.fb.group({
    id: [null],
    nome: ['', [Validators.required]],
    tipoCombustivelId: [null, [Validators.required]],
  });

  constructor(
    public dialogRef: MatDialogRef<PumpDialog>,
    @Inject(MAT_DIALOG_DATA) public data: Pump | null
  ) {
    if (data) {
      this.form.patchValue({
        id: data.id,
        nome: data.nome,
        tipoCombustivelId: data.tipoCombustivel.id,
      });
    }
  }

  ngOnInit() {
    this.fuelService.getAll().subscribe((data) => (this.fuelTypes = data));
  }

  onSave() {
    if (this.form.valid) this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
