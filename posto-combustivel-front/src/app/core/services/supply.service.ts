import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Supply } from '../models/supply.model';

@Injectable({
  providedIn: 'root',
})
export class SupplyService {
  private http = inject(HttpClient);
  private apiUrl = '/api/abastecimentos';

  getAll(): Observable<Supply[]> {
    return this.http.get<Supply[]>(this.apiUrl);
  }

  create(supply: any): Observable<Supply> {
    return this.http.post<Supply>(this.apiUrl, supply);
  }

  // Método especial para Cancelar (PATCH)
  cancel(id: number): Observable<Supply> {
    return this.http.patch<Supply>(`${this.apiUrl}/${id}/cancelar`, {});
  }
}
