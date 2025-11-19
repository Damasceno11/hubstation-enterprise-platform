import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ClienteCadastro {
  nome: string;
  cpf: string;
  email: string;
}

@Injectable({
  providedIn: 'root',
})
export class Cliente {
  private apiUrl = '/api/clientes';

  constructor(private http: HttpClient) {}

  cadastrar(dados: ClienteCadastro): Observable<any> {
    return this.http.post(this.apiUrl, dados);
  }
}
