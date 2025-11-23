import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../core/services/dashboard.service';

interface DashboardStats {
  qtdCombustiveis: number;
  qtdBombas: number;
  qtdAbastecimentos: number;
  valorTotalVendas: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    RouterLink,
    CurrencyPipe,
    DatePipe,
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {
  private dashboardService = inject(DashboardService);

  now = new Date();

  stats: DashboardStats = {
    qtdCombustiveis: 0,
    qtdBombas: 0,
    qtdAbastecimentos: 0,
    valorTotalVendas: 0,
  };

  ngOnInit() {
    this.dashboardService.getStats().subscribe({
      next: (data) => (this.stats = data),
      error: (err) => console.error('Erro dashboard:', err),
    });
  }
}
