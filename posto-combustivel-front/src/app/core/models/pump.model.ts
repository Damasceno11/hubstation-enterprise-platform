import { FuelType } from './fuel-type.model';

export interface Pump {
  id: number;
  nome: string;
  tipoCombustivel: FuelType; // Objeto aninhado (Vem do Backend)
}
