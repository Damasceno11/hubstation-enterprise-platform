import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth-guard';
import { Navigation } from './layout/navigation/navigation'; // Seu componente de menu
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { SupplyList } from './pages/supplies/supply-list/supply-list';
import { PumpList } from './pages/pumps/pump-list/pump-list';
import { FuelTypeList } from './pages/fuel-types/fuel-type-list/fuel-type-list';
import { ClienteList } from './pages/clientes/cliente-list/cliente-list';
import { CadastroFidelidade } from './pages/clientes/cliente-form/cliente-form';

export const routes: Routes = [
  // 1. Rota Pública (Totalmente isolada)
  {
    path: 'login',
    component: Login,
  },

  // 2. Rotas Protegidas (App Shell)
  {
    path: '',
    component: Navigation, // O Navigation agora age como um "Container"
    canActivate: [authGuard], // Protege o container inteiro de uma vez
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard },
      { path: 'abastecimentos', component: SupplyList },
      { path: 'bombas', component: PumpList },
      { path: 'combustiveis', component: FuelTypeList },
      { path: 'clientes', component: ClienteList },
      { path: 'fidelidade', component: CadastroFidelidade },
    ],
  },

  // Fallback para rotas inexistentes
  { path: '**', redirectTo: 'dashboard' },
];
