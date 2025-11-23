import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Pump } from '../models/pump.model';

@Injectable({
  providedIn: 'root',
})
export class PumpService {
  private http = inject(HttpClient);
  private apiUrl = '/api/bombas';

  getAll(): Observable<Pump[]> {
    return this.http.get<Pump[]>(this.apiUrl);
  }
  create(pump: any): Observable<Pump> {
    return this.http.post<Pump>(this.apiUrl, pump);
  }
  update(id: number, pump: any): Observable<Pump> {
    return this.http.put<Pump>(`${this.apiUrl}/${id}`, pump);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
