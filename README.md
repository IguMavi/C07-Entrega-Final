
---

### 1. 🏢 Gerenciar Fornecedores

| Funcionalidade | Descrição | Quem pode |
|----------------|-----------|-----------|
| Inserir Fornecedor | Cadastra novo fornecedor com nome e CNPJ | Todos |
| Listar Todos | Exibe todos os fornecedores cadastrados | Todos |
| Buscar por ID | Localiza fornecedor pelo código | Todos |
| Buscar por Nome | Pesquisa fornecedores pelo nome | Todos |
| Atualizar Fornecedor | Altera dados de um fornecedor | Apenas Admin |
| Deletar Fornecedor | Remove um fornecedor do sistema | Apenas Admin |

---

### 2. 👤 Gerenciar Clientes

| Funcionalidade | Descrição | Quem pode |
|----------------|-----------|-----------|
| Inserir Cliente | Cadastra novo cliente com nome e CPF | Todos |
| Listar Todos | Exibe todos os clientes cadastrados | Todos |
| Buscar por ID | Localiza cliente pelo código | Todos |
| Buscar por Nome | Pesquisa clientes pelo nome | Todos |
| Buscar por CPF | Localiza cliente pelo CPF exato | Todos |
| Atualizar Cliente | Altera dados de um cliente | Apenas Admin |
| Deletar Cliente | Remove um cliente do sistema | Apenas Admin |

---

### 3. 📦 Gerenciar Produtos

| Funcionalidade | Descrição | Quem pode |
|----------------|-----------|-----------|
| Inserir Produto | Cadastra novo produto | Todos |
| Listar Todos | Exibe todos os produtos | Todos |
| Buscar por ID | Localiza produto pelo código | Todos |
| Buscar por Nome | Pesquisa produtos pelo nome | Todos |
| Atualizar Produto | Altera dados de um produto | Apenas Admin |
| Deletar Produto | Remove um produto | Apenas Admin |

---

### 4. 💰 Gerenciar Vendas

| Funcionalidade | Descrição | Quem pode |
|----------------|-----------|-----------|
| Registrar Nova Venda | Cria uma nova venda com múltiplos produtos | Todos |
| Listar Todas Vendas | Exibe todas as vendas com seus itens | Todos |
| Buscar Venda por ID | Localiza venda específica | Todos |
| Buscar Vendas por Cliente | Mostra todas as vendas de um cliente | Todos |

#### Fluxo de uma Venda:

1. Seleciona o cliente
2. Sistema cria o registro de venda
3. Para cada produto:
   - Seleciona o produto
   - Informa a quantidade
   - Sistema verifica e atualiza o estoque
4. Finaliza a venda

---

### 5. 📊 Consultas Especiais (JOINs)

| Consulta | Tabelas envolvidas | O que mostra |
|----------|-------------------|--------------|
| **Relatório de Vendas** | Vendas + Clientes + Itens_Venda + Produtos | Todas as vendas com detalhes do cliente, produtos, quantidades e valores |
| **Produtos por Fornecedor** | Produtos + Fornecedores | Lista de produtos agrupados por fornecedor |
| **Total de Vendas por Cliente** | Clientes + Vendas + Itens_Venda | Quanto cada cliente gastou no total |

---

### 6. 📋 Mostrar Todas as Tabelas

Exibe o conteúdo completo de todas as tabelas do banco de dados:
- Lista de Fornecedores
- Lista de Clientes
- Lista de Produtos
- Lista de Vendas (com seus itens)

---
## 🚀 Como Executar o Projeto

### Pré-requisitos

| Requisito | Versão |
|-----------|--------|
| MySQL Server | 8.0+ |
| Java | 11+ |
| Maven | 3.6+ |
| IntelliJ IDEA | (recomendado) |

### Passo 1: Configurar o Banco de Dados

1. Abra o MySQL Workbench
2. Copie e cole o script `script.sql`
3. Execute (⚡)
4. Verifique se o banco `DB_Supermercado_Novo` foi criado

### Passo 2: Configurar o Java

1. Abra o projeto no IntelliJ IDEA
2. Aguarde o Maven baixar as dependências
3. Execute a classe `Main.java`

### Passo 3: Fazer Login
👤 Usuário: Admin
🔒 Senha: admin123
