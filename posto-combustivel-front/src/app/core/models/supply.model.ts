import { Cliente } from './cliente.model';
import { Pump } from './pump.model';

export interface Supply {
  id: number;
  dataAbastecimento: string;
  litragem: number;
  valorTotal: number;
  status: 'CONCLUIDO' | 'CANCELADO';
  bomba: Pump;
  cliente?: Cliente;
}
