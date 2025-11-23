import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Cliente, ClienteCadastro } from '../../core/models/cliente.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private http = inject(HttpClient);

  // Usa a URL do environment (boas práticas de DevOps)
  private readonly apiUrl = `${environment.apiUrl}/clientes`;

  getAll(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  getById(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`);
  }

  create(dados: ClienteCadastro): Observable<Cliente> {
    return this.http.post<Cliente>(this.apiUrl, dados);
  }

  update(id: number, dados: ClienteCadastro): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.apiUrl}/${id}`, dados);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
