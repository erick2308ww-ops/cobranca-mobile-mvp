# MVP – Cobrança e Pedidos em Campo (No‑Code)

## Objetivo
Automatizar cobranças, reduzir inadimplência e padronizar o controle financeiro em campo, com um aplicativo mobile simples e rápido para uso interno por vendedores, cobradores e entregadores.

## Escopo (Android e iOS)
Aplicativo no‑code com fluxo de autenticação, dashboard, clientes, financeiro e relatórios. Sem mapas, rotas automáticas ou IA nesta versão.

---

## 1) Papéis de Usuário e Permissões
**Tipos de usuário**
- **Vendedor**: consulta clientes da sua rota, registra cobranças e pagamentos.
- **Cobrador**: consulta clientes da sua rota, registra cobranças e pagamentos.
- **Entregador**: consulta clientes da sua rota, registra entregas (opcional no MVP).
- **Administrador**: acesso global e gestão de cadastros.

**Regras de visibilidade**
- Usuário comum vê **somente dados da própria rota**.
- Administrador vê todas as rotas.

---

## 2) Modelo de Dados (Coleções/Tabelas)
> Estrutura preparada para escalar. Relacionamentos por IDs e referência simples.

### 2.1 Usuários
- **id**
- **nome**
- **usuario_login**
- **senha_hash** (ou credencial nativa do no‑code)
- **tipo** (vendedor, cobrador, entregador, administrador)
- **rota_id**
- **ativo** (boolean)

### 2.2 Rotas
- **id**
- **nome**
- **descricao**

### 2.3 Clientes
- **id**
- **nome**
- **cpf_cnpj**
- **endereco**
- **telefone**
- **rota_id**
- **vendedor_id**
- **status** (ativo, devendo, inativo)
- **foto_casa_url**
- **nota_avaliacao** (1 a 5)
- **comentario_avaliacao**
- **observacoes_internas**
- **criado_em**
- **atualizado_em**

### 2.4 Débitos
- **id**
- **cliente_id**
- **descricao**
- **valor**
- **data_vencimento**
- **status** (aberto, pago, cancelado)
- **criado_em**

### 2.5 Pagamentos
- **id**
- **cliente_id**
- **debito_id** (opcional)
- **valor**
- **forma_pagamento** (dinheiro, pix, transferencia)
- **data_pagamento**
- **usuario_id** (responsável)
- **observacao**

### 2.6 Cobranças (Histórico)
- **id**
- **cliente_id**
- **usuario_id**
- **data_cobranca**
- **resultado** (prometeu_pagar, pagou, sem_contato, recusou)
- **observacao**

---

## 3) Telas e Fluxos

### 3.1 Autenticação
**Tela de login**
- Campos: usuário e senha.
- Login individual.
- Após login, carregar **rota_id** e **tipo** do usuário.

### 3.2 Dashboard (por usuário logado)
Cards principais:
- **Total vendido no mês**
- **Total recebido no mês**
- **Total em aberto**
- **Quantidade de clientes inadimplentes**

**Filtros automáticos**
- Por **rota** e **usuário logado**.

### 3.3 Clientes
**Lista de clientes**
- Busca por nome, CPF/CNPJ, telefone.
- Filtro por status (ativo, devendo, inativo).
- Mostrar indicador de inadimplência.

**Cadastro completo**
- Nome, CPF/CNPJ, Endereço, Telefone, Rota, Vendedor responsável, Status.
- Upload de foto da casa.

**Detalhes do cliente**
- **Débitos pendentes** (lista com valor e vencimento)
- **Histórico de cobranças**
- **Histórico de pagamentos**
- **Observações internas**
- **Avaliação do cliente** (nota + comentário)

### 3.4 Financeiro
**Dar baixa em pagamentos**
- Formas: dinheiro, pix, transferência.
- Registro automático de **data, valor e usuário responsável**.

**Histórico financeiro**
- Por cliente.
- Por usuário.

### 3.5 Relatórios
- **Relatório de cobrança**: clientes em débito, valor em aberto, data de vencimento.
- **Relatório de vendas**: dia e mês.
- **Relatório de recebimentos**: dia e mês.
- Filtros por **data**, **rota** e **usuário**.

---

## 4) Regras de Negócio
- Usuário comum só vê dados da própria rota.
- Administrador pode ver e editar tudo.
- Débitos “abertos” entram no **Total em aberto**.
- Cliente com débitos abertos entra em **inadimplentes**.
- Pagamento vinculado a débito muda status para **pago**.

---

## 5) Componentes e Experiência
- Interface simples e rápida, foco em uso em campo.
- Listas com busca e filtros rápidos.
- Formulários enxutos e validações básicas.

---

## 6) Checklist de Entrega (No‑Code)
1. Criar tabelas/coleções conforme modelo de dados.
2. Implementar autenticação e regras de visibilidade por rota.
3. Construir telas: Login, Dashboard, Clientes (lista, cadastro, detalhe), Financeiro, Relatórios.
4. Configurar regras de segurança e filtros automáticos.
5. Testar com usuários de diferentes rotas e perfis.

---

## 7) Evoluções Futuras (fora do MVP)
- Rotas automáticas e mapas.
- Integração com ERP/contas a receber.
- Pagamentos via link e notificação automática.
- IA para previsão de inadimplência.
