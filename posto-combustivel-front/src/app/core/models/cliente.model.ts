export interface Cliente {
  id: number;
  nome: string;
  cpf: string;
  email: string;
  // Adicione outros campos se houver (telefone, endereco, etc)
}

// Interface auxiliar para o cadastro (sem ID)
export interface ClienteCadastro {
  nome: string;
  cpf: string;
  email: string;
}
