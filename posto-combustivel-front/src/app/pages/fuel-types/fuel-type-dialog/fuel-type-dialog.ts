import { CommonModule } from '@angular/common';
import { Component, Inject, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FuelType } from '../../../core/models/fuel-type.model';
// Ajuste o caminho se necessário

@Component({
  selector: 'app-fuel-type-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
  ],
  templateUrl: './fuel-type-dialog.html',
  styleUrl: './fuel-type-dialog.scss',
})
export class FuelTypeDialog {
  private fb = inject(FormBuilder);

  form: FormGroup = this.fb.group({
    id: [null],
    nome: ['', [Validators.required, Validators.minLength(3)]],
    precoLitro: [null, [Validators.required, Validators.min(0.01)]],
  });

  constructor(
    public dialogRef: MatDialogRef<FuelTypeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: FuelType | null
  ) {
    if (data) this.form.patchValue(data);
  }

  onSave() {
    if (this.form.valid) this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
