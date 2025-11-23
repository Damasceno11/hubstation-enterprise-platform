import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FuelType } from '../models/fuel-type.model';

@Injectable({
  providedIn: 'root',
})
export class FuelTypeService {
  private http = inject(HttpClient);
  private apiUrl = '/api/tipos-combustivel';

  getAll(): Observable<FuelType[]> {
    return this.http.get<FuelType[]>(this.apiUrl);
  }

  create(fuelType: Partial<FuelType>): Observable<FuelType> {
    return this.http.post<FuelType>(this.apiUrl, fuelType);
  }

  update(id: number, fuelType: Partial<FuelType>): Observable<FuelType> {
    return this.http.put<FuelType>(`${this.apiUrl}/${id}`, fuelType);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
