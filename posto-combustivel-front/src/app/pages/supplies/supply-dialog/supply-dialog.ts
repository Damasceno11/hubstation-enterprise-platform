import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core'; // Importante
import { MatIconModule } from '@angular/material/icon';

// Services & Models
import { PumpService } from '../../../core/services/pump.service';
import { Pump } from '../../../core/models/pump.model';

@Component({
  selector: 'app-supply-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
  ],
  templateUrl: './supply-dialog.html',
  styleUrl: './supply-dialog.scss',
})
export class SupplyDialog implements OnInit {
  private fb = inject(FormBuilder);
  private pumpService = inject(PumpService);

  pumps: Pump[] = [];

  form: FormGroup = this.fb.group({
    bombaId: [null, [Validators.required]],
    dataAbastecimento: [new Date(), [Validators.required]],
    valorTotal: [null, [Validators.required, Validators.min(1.0)]],
    cpfCliente: [''], // Opcional
  });

  constructor(public dialogRef: MatDialogRef<SupplyDialog>) {}

  ngOnInit() {
    this.pumpService.getAll().subscribe((data) => (this.pumps = data));
  }

  onSave() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
