package com.supermercado;

import com.supermercado.dao.*;
import com.supermercado.models.*;
import com.supermercado.db.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static ProdutoDAO produtoDAO = new ProdutoDAO();
    private static VendaDAO vendaDAO = new VendaDAO();
    private static ItemVendaDAO itemVendaDAO = new ItemVendaDAO();

    // Variável para armazenar o tipo de usuário logado
    private static String tipoUsuario = "";
    private static String nomeUsuario = "";

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTÃO DE SUPERMERCADO   ║");
        System.out.println("╚════════════════════════════════════════╝");

        // =============================================
        // SISTEMA DE LOGIN COM USUÁRIO E SENHA
        // =============================================

        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║              LOGIN DO SISTEMA          ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("\n📋 Usuários disponíveis:");
        System.out.println("┌─────────────────────────────────────────────────┐");
        System.out.println("│ 1-OperadorCaixa1 → Apenas CONSULTAR e INSERIR    │");
        System.out.println("│ 2-Admin          → Acesso TOTAL (UPDATE/DELETE)  │");
        System.out.println("└─────────────────────────────────────────────────┘");

        System.out.println("\n0. Sair");

        String usuario = "";
        String senha = "";

        // Loop até login bem sucedido
        boolean loginOk = false;
        while (!loginOk) {
            usuario = lerString("\n👤 Usuário: ");

            if (usuario.equals("0")) {
                System.out.println("\n🔒 Sistema encerrado.");
                scanner.close();
                return;
            }

            senha = lerString("🔒 Senha: ");

            // Verificar credenciais
            if (usuario.equals("OperadorCaixa1") && senha.equals("senhaCaixa01")) {
                tipoUsuario = "OPERADOR";
                nomeUsuario = "OperadorCaixa1";
                loginOk = true;
            }
            else if (usuario.equals("Admin") && senha.equals("admin123")) {
                tipoUsuario = "ADMIN";
                nomeUsuario = "Admin";
                loginOk = true;
            }
            else {
                System.out.println("\n❌ Usuário ou senha inválidos! Tente novamente.\n");
            }
        }

        // Tenta conectar com o usuário escolhido
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/DB_Supermercado_Novo",
                    usuario,
                    senha
            );
            DatabaseConnection.getInstance().setConnection(conn);

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         LOGIN REALIZADO COM SUCESSO!    ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("\n👤 Perfil: " + tipoUsuario);
            System.out.println("📌 Conectado como: " + nomeUsuario);

            if (tipoUsuario.equals("OPERADOR")) {
                System.out.println("\n⚠️  ATENÇÃO: Você tem permissão apenas para CONSULTAR e INSERIR!");
                System.out.println("   Operações de UPDATE e DELETE NÃO estarão disponíveis.\n");
            } else {
                System.out.println("\n🔓 Você tem ACESSO TOTAL ao sistema.");
                System.out.println("   Pode realizar qualquer operação (INSERT, UPDATE, DELETE).\n");
            }

        } catch (SQLException e) {
            System.out.println("\n❌ Falha no login: " + e.getMessage());
            System.out.println("Verifique se o usuário existe no MySQL.");
            scanner.close();
            return;
        }

        // =============================================
        // MENU PRINCIPAL (APÓS LOGIN)
        // =============================================

        int opcao;
        do {
            mostrarMenuPrincipal();
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1: menuFornecedores(); break;
                    case 2: menuClientes(); break;
                    case 3: menuProdutos(); break;
                    case 4: menuVendas(); break;
                    case 5: menuConsultasEspeciais(); break;
                    case 6: mostrarTodasTabelas(); break;
                    case 0:
                        System.out.println("\n🔒 Saindo do sistema...");
                        DatabaseConnection.getInstance().disconnect();
                        break;
                    default: System.out.println("❌ Opção inválida!");
                }
            } catch (SQLException e) {
                if (e.getMessage().contains("DELETE command denied")) {
                    System.out.println("\n⚠️  OPERADOR não pode DELETAR! Use ADMIN.");
                } else if (e.getMessage().contains("UPDATE command denied")) {
                    System.out.println("\n⚠️  OPERADOR não pode ATUALIZAR! Use ADMIN.");
                } else {
                    e.printStackTrace();
                }
            }
        } while (opcao != 0);

        scanner.close();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.print("❌ Valor inválido! Digite um número: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private static BigDecimal lerBigDecimal(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextBigDecimal()) {
            System.out.print("❌ Valor inválido! Digite um número válido: ");
            scanner.next();
        }
        BigDecimal valor = scanner.nextBigDecimal();
        scanner.nextLine();
        return valor;
    }

    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║             MENU PRINCIPAL             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. 🏢 Gerenciar Fornecedores");
        System.out.println("2. 👤 Gerenciar Clientes");
        System.out.println("3. 📦 Gerenciar Produtos");
        System.out.println("4. 💰 Gerenciar Vendas");
        System.out.println("5. 📊 Consultas Especiais");
        System.out.println("6. 📋 Mostrar todas as tabelas");
        System.out.println("0. 🚪 Sair");

        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️  Modo OPERADOR: Apenas CONSULTAS e INSERÇÕES");
            System.out.println("   (UPDATE e DELETE bloqueados)");
        } else {
            System.out.println("\n🔓 Modo ADMIN: Acesso TOTAL");
            System.out.println("   (INSERT, UPDATE, DELETE liberados)");
        }
    }

    // ==================== MENU FORNECEDORES ====================

    private static void menuFornecedores() throws SQLException {
        int opcao;
        do {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║           GERENCIAR FORNECEDORES       ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. ➕ Inserir Fornecedor");
            System.out.println("2. 📋 Listar Todos");
            System.out.println("3. 🔍 Buscar por ID");
            System.out.println("4. 🔍 Buscar por Nome");
            System.out.println("5. ✏️ Atualizar Fornecedor");
            System.out.println("6. 🗑️ Deletar Fornecedor");
            System.out.println("0. ↩️ Voltar");

            opcao = lerInteiro("Opção: ");

            switch (opcao) {
                case 1: inserirFornecedor(); break;
                case 2: listarFornecedores(); break;
                case 3: buscarFornecedorPorId(); break;
                case 4: buscarFornecedorPorNome(); break;
                case 5: atualizarFornecedor(); break;
                case 6: deletarFornecedor(); break;
            }
        } while (opcao != 0);
    }

    private static void inserirFornecedor() throws SQLException {
        System.out.println("\n--- NOVO FORNECEDOR ---");
        String nome = lerString("Nome: ");
        String cnpj = lerString("CNPJ: ");

        Fornecedor fornecedor = new Fornecedor(nome, cnpj);
        fornecedorDAO.insert(fornecedor);
        System.out.println("✅ Fornecedor cadastrado com sucesso! ID: " + fornecedor.getId());
    }

    private static void listarFornecedores() throws SQLException {
        List<Fornecedor> fornecedores = fornecedorDAO.findAll();
        System.out.println("\n--- LISTA DE FORNECEDORES ---");
        if (fornecedores.isEmpty()) {
            System.out.println("📭 Nenhum fornecedor cadastrado.");
        } else {
            for (Fornecedor f : fornecedores) {
                System.out.println(f);
            }
        }
    }

    private static void buscarFornecedorPorId() throws SQLException {
        int id = lerInteiro("ID do fornecedor: ");
        Fornecedor f = fornecedorDAO.findById(id);
        if (f != null) {
            System.out.println(f);
        } else {
            System.out.println("❌ Fornecedor não encontrado!");
        }
    }

    private static void buscarFornecedorPorNome() throws SQLException {
        String nome = lerString("Nome para busca: ");
        List<Fornecedor> fornecedores = fornecedorDAO.searchByNome(nome);
        System.out.println("\n--- RESULTADOS ---");
        if (fornecedores.isEmpty()) {
            System.out.println("📭 Nenhum fornecedor encontrado.");
        } else {
            for (Fornecedor f : fornecedores) {
                System.out.println(f);
            }
        }
    }

    private static void atualizarFornecedor() throws SQLException {
        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️  OPERADOR não tem permissão para ATUALIZAR!");
            System.out.println("   Faça login como ADMIN para esta operação.");
            return;
        }

        int id = lerInteiro("ID do fornecedor a atualizar: ");
        Fornecedor f = fornecedorDAO.findById(id);
        if (f != null) {
            System.out.println("Dados atuais: " + f);
            String novoNome = lerString("Novo nome (Enter para manter): ");
            if (!novoNome.isEmpty()) {
                f.setNomeFantasia(novoNome);
            }
            String novoCnpj = lerString("Novo CNPJ (Enter para manter): ");
            if (!novoCnpj.isEmpty()) {
                f.setCnpj(novoCnpj);
            }
            fornecedorDAO.update(f);
            System.out.println("✅ Fornecedor atualizado com sucesso!");
        } else {
            System.out.println("❌ Fornecedor não encontrado!");
        }
    }

    private static void deletarFornecedor() throws SQLException {
        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️  OPERADOR não tem permissão para DELETAR!");
            System.out.println("   Faça login como ADMIN para esta operação.");
            return;
        }

        int id = lerInteiro("ID do fornecedor a deletar: ");
        fornecedorDAO.delete(id);
        System.out.println("✅ Fornecedor deletado com sucesso!");
    }

    // ==================== MENU CLIENTES ====================

    private static void menuClientes() throws SQLException {
        int opcao;
        do {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║            GERENCIAR CLIENTES          ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. ➕ Inserir Cliente");
            System.out.println("2. 📋 Listar Todos");
            System.out.println("3. 🔍 Buscar por ID");
            System.out.println("4. 🔍 Buscar por Nome");
            System.out.println("5. 🔍 Buscar por CPF");
            System.out.println("6. ✏️ Atualizar Cliente");
            System.out.println("7. 🗑️ Deletar Cliente");
            System.out.println("0. ↩️ Voltar");

            opcao = lerInteiro("Opção: ");

            switch (opcao) {
                case 1: inserirCliente(); break;
                case 2: listarClientes(); break;
                case 3: buscarClientePorId(); break;
                case 4: buscarClientePorNome(); break;
                case 5: buscarClientePorCpf(); break;
                case 6: atualizarCliente(); break;
                case 7: deletarCliente(); break;
            }
        } while (opcao != 0);
    }

    private static void inserirCliente() throws SQLException {
        System.out.println("\n--- NOVO CLIENTE ---");
        String nome = lerString("Nome: ");
        String cpf = lerString("CPF: ");

        Cliente cliente = new Cliente(nome, cpf);
        clienteDAO.insert(cliente);
        System.out.println("✅ Cliente cadastrado com sucesso! ID: " + cliente.getId());
    }

    private static void listarClientes() throws SQLException {
        List<Cliente> clientes = clienteDAO.findAll();
        System.out.println("\n--- LISTA DE CLIENTES ---");
        if (clientes.isEmpty()) {
            System.out.println("📭 Nenhum cliente cadastrado.");
        } else {
            for (Cliente c : clientes) {
                System.out.println(c);
            }
        }
    }

    private static void buscarClientePorId() throws SQLException {
        int id = lerInteiro("ID do cliente: ");
        Cliente c = clienteDAO.findById(id);
        if (c != null) {
            System.out.println(c);
        } else {
            System.out.println("❌ Cliente não encontrado!");
        }
    }

    private static void buscarClientePorNome() throws SQLException {
        String nome = lerString("Nome para busca: ");
        List<Cliente> clientes = clienteDAO.searchByNome(nome);
        System.out.println("\n--- RESULTADOS ---");
        if (clientes.isEmpty()) {
            System.out.println("📭 Nenhum cliente encontrado.");
        } else {
            for (Cliente c : clientes) {
                System.out.println(c);
            }
        }
    }

    private static void buscarClientePorCpf() throws SQLException {
        String cpf = lerString("CPF para busca: ");
        Cliente c = clienteDAO.searchByCpf(cpf);
        if (c != null) {
            System.out.println(c);
        } else {
            System.out.println("❌ Cliente não encontrado!");
        }
    }

    private static void atualizarCliente() throws SQLException {
        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️  OPERADOR não tem permissão para ATUALIZAR!");
            System.out.println("   Faça login como ADMIN para esta operação.");
            return;
        }

        int id = lerInteiro("ID do cliente a atualizar: ");
        Cliente c = clienteDAO.findById(id);
        if (c != null) {
            System.out.println("Dados atuais: " + c);
            String novoNome = lerString("Novo nome (Enter para manter): ");
            if (!novoNome.isEmpty()) {
                c.setNome(novoNome);
            }
            String novoCpf = lerString("Novo CPF (Enter para manter): ");
            if (!novoCpf.isEmpty()) {
                c.setCpf(novoCpf);
            }
            clienteDAO.update(c);
            System.out.println("✅ Cliente atualizado com sucesso!");
        } else {
            System.out.println("❌ Cliente não encontrado!");
        }
    }

    private static void deletarCliente() throws SQLException {
        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️  OPERADOR não tem permissão para DELETAR!");
            System.out.println("   Faça login como ADMIN para esta operação.");
            return;
        }

        int id = lerInteiro("ID do cliente a deletar: ");
        clienteDAO.delete(id);
        System.out.println("✅ Cliente deletado com sucesso!");
    }

    // ==================== MENU PRODUTOS ====================

    private static void menuProdutos() throws SQLException {
        int opcao;
        do {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║            GERENCIAR PRODUTOS          ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. ➕ Inserir Produto");
            System.out.println("2. 📋 Listar Todos");
            System.out.println("3. 🔍 Buscar por ID");
            System.out.println("4. 🔍 Buscar por Nome");
            System.out.println("5. ✏️ Atualizar Produto");
            System.out.println("6. 🗑️ Deletar Produto");
            System.out.println("0. ↩️ Voltar");

            opcao = lerInteiro("Opção: ");

            switch (opcao) {
                case 1: inserirProduto(); break;
                case 2: listarProdutos(); break;
                case 3: buscarProdutoPorId(); break;
                case 4: buscarProdutoPorNome(); break;
                case 5: atualizarProduto(); break;
                case 6: deletarProduto(); break;
            }
        } while (opcao != 0);
    }

    private static void inserirProduto() throws SQLException {
        System.out.println("\n--- NOVO PRODUTO ---");
        String nome = lerString("Nome: ");
        BigDecimal preco = lerBigDecimal("Preço: R$ ");
        int estoque = lerInteiro("Estoque: ");

        listarFornecedores();
        int idFornecedor = lerInteiro("ID do Fornecedor: ");

        Produto produto = new Produto(nome, preco, estoque, idFornecedor);
        produtoDAO.insert(produto);
        System.out.println("✅ Produto cadastrado com sucesso! ID: " + produto.getId());
    }

    private static void listarProdutos() throws SQLException {
        List<Produto> produtos = produtoDAO.findAll();
        System.out.println("\n--- LISTA DE PRODUTOS ---");
        if (produtos.isEmpty()) {
            System.out.println("📭 Nenhum produto cadastrado.");
        } else {
            for (Produto p : produtos) {
                System.out.println(p);
            }
        }
    }

    private static void buscarProdutoPorId() throws SQLException {
        int id = lerInteiro("ID do produto: ");
        Produto p = produtoDAO.findById(id);
        if (p != null) {
            System.out.println(p);
        } else {
            System.out.println("❌ Produto não encontrado!");
        }
    }

    private static void buscarProdutoPorNome() throws SQLException {
        String nome = lerString("Nome para busca: ");
        List<Produto> produtos = produtoDAO.searchByNome(nome);
        System.out.println("\n--- RESULTADOS ---");
        if (produtos.isEmpty()) {
            System.out.println("📭 Nenhum produto encontrado.");
        } else {
            for (Produto p : produtos) {
                System.out.println(p);
            }
        }
    }

    private static void atualizarProduto() throws SQLException {
        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️  OPERADOR não tem permissão para ATUALIZAR!");
            System.out.println("   Faça login como ADMIN para esta operação.");
            return;
        }

        int id = lerInteiro("ID do produto a atualizar: ");
        Produto p = produtoDAO.findById(id);
        if (p != null) {
            System.out.println("Dados atuais: " + p);
            String novoNome = lerString("Novo nome (Enter para manter): ");
            if (!novoNome.isEmpty()) {
                p.setNome(novoNome);
            }
            String novoPreco = lerString("Novo preço (Enter para manter): ");
            if (!novoPreco.isEmpty()) {
                p.setPreco(new BigDecimal(novoPreco));
            }
            int novoEstoque = lerInteiro("Novo estoque (-1 para manter): ");
            if (novoEstoque != -1) {
                p.setEstoque(novoEstoque);
            }
            produtoDAO.update(p);
            System.out.println("✅ Produto atualizado com sucesso!");
        } else {
            System.out.println("❌ Produto não encontrado!");
        }
    }

    private static void deletarProduto() throws SQLException {
        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️  OPERADOR não tem permissão para DELETAR!");
            System.out.println("   Faça login como ADMIN para esta operação.");
            return;
        }

        int id = lerInteiro("ID do produto a deletar: ");
        produtoDAO.delete(id);
        System.out.println("✅ Produto deletado com sucesso!");
    }

    // ==================== MENU VENDAS ====================

    private static void menuVendas() throws SQLException {
        int opcao;
        do {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║            GERENCIAR VENDAS            ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. 💰 Registrar Nova Venda");
            System.out.println("2. 📋 Listar Todas Vendas");
            System.out.println("3. 🔍 Buscar Venda por ID");
            System.out.println("4. 🔍 Buscar Vendas por Cliente");
            System.out.println("5. ✏️ Atualizar Cliente da Venda");
            System.out.println("6. 🗑️ Deletar Venda");
            System.out.println("0. ↩️ Voltar");

            opcao = lerInteiro("Opção: ");

            switch (opcao) {
                case 1: registrarVenda(); break;
                case 2: listarVendas(); break;
                case 3: buscarVendaPorId(); break;
                case 4: buscarVendasPorCliente(); break;
                case 5: atualizarVenda(); break;
                case 6: deletarVenda(); break;
            }
        } while (opcao != 0);
    }

    private static void registrarVenda() throws SQLException {
        System.out.println("\n--- REGISTRAR VENDA ---");

        listarClientes();
        int idCliente = lerInteiro("ID do Cliente: ");

        Venda venda = new Venda(idCliente);
        vendaDAO.insert(venda);

        System.out.println("✅ Venda iniciada! ID da venda: " + venda.getId());

        boolean continuar = true;
        while (continuar) {
            listarProdutos();
            int idProduto = lerInteiro("ID do Produto (0 para finalizar): ");

            if (idProduto == 0) {
                continuar = false;
            } else {
                Produto produto = produtoDAO.findById(idProduto);
                if (produto != null) {
                    int quantidade = lerInteiro("Quantidade: ");
                    if (quantidade <= produto.getEstoque()) {
                        ItemVenda item = new ItemVenda(venda.getId(), idProduto, quantidade, produto.getPreco());
                        itemVendaDAO.insert(item);
                        System.out.println("✅ Item adicionado com sucesso!");
                    } else {
                        System.out.println("❌ Estoque insuficiente! Estoque atual: " + produto.getEstoque());
                    }
                } else {
                    System.out.println("❌ Produto não encontrado!");
                }
            }
        }

        System.out.println("✅ Venda finalizada com sucesso!");
    }

    private static void listarVendas() throws SQLException {
        List<Venda> vendas = vendaDAO.findAll();
        System.out.println("\n--- LISTA DE VENDAS ---");
        if (vendas.isEmpty()) {
            System.out.println("📭 Nenhuma venda registrada.");
        } else {
            for (Venda v : vendas) {
                System.out.println(v);
                List<ItemVenda> itens = itemVendaDAO.findByVenda(v.getId());
                for (ItemVenda item : itens) {
                    Produto p = produtoDAO.findById(item.getIdProduto());
                    System.out.println("  -> " + p.getNome() + " | Qtd: " + item.getQuantidade() +
                            " | Preço: R$" + item.getPrecoUnitario());
                }
                System.out.println();
            }
        }
    }

    private static void buscarVendaPorId() throws SQLException {
        int id = lerInteiro("ID da venda: ");
        Venda v = vendaDAO.findById(id);
        if (v != null) {
            System.out.println(v);
            List<ItemVenda> itens = itemVendaDAO.findByVenda(v.getId());
            for (ItemVenda item : itens) {
                Produto p = produtoDAO.findById(item.getIdProduto());
                System.out.println("  -> " + p.getNome() + " | Qtd: " + item.getQuantidade() +
                        " | Preço: R$" + item.getPrecoUnitario());
            }
        } else {
            System.out.println("❌ Venda não encontrada!");
        }
    }

    private static void buscarVendasPorCliente() throws SQLException {
        int idCliente = lerInteiro("ID do Cliente: ");
        List<Venda> vendas = vendaDAO.findByCliente(idCliente);
        System.out.println("\n--- VENDAS DO CLIENTE ---");
        if (vendas.isEmpty()) {
            System.out.println("📭 Nenhuma venda encontrada para este cliente.");
        } else {
            for (Venda v : vendas) {
                System.out.println(v);
            }
        }
    }

    private static void atualizarVenda() throws SQLException {
        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️ OPERADOR não pode ATUALIZAR vendas! Use ADMIN.");
            return;
        }

        System.out.println("\n--- ATUALIZAR VENDA ---");
        int id = lerInteiro("ID da venda: ");
        Venda v = vendaDAO.findById(id);

        if (v != null) {
            System.out.println("Dados atuais: " + v);
            System.out.println("Cliente atual: " + clienteDAO.findById(v.getIdCliente()).getNome());

            // Mostrar clientes disponíveis
            listarClientes();
            int novoIdCliente = lerInteiro("Novo ID do Cliente (0 para manter): ");

            if (novoIdCliente != 0) {
                Cliente novoCliente = clienteDAO.findById(novoIdCliente);
                if (novoCliente != null) {
                    v.setIdCliente(novoIdCliente);
                    vendaDAO.update(v);
                    System.out.println("✅ Venda atualizada com sucesso!");
                    System.out.println("   Cliente alterado para: " + novoCliente.getNome());
                } else {
                    System.out.println("❌ Cliente não encontrado! Atualização cancelada.");
                }
            } else {
                System.out.println("✅ Nenhuma alteração realizada.");
            }
        } else {
            System.out.println("❌ Venda não encontrada!");
        }
    }

    private static void deletarVenda() throws SQLException {
        if (tipoUsuario.equals("OPERADOR")) {
            System.out.println("\n⚠️ OPERADOR não pode DELETAR vendas! Use ADMIN.");
            return;
        }

        System.out.println("\n--- DELETAR VENDA ---");
        int id = lerInteiro("ID da venda a deletar: ");
        Venda v = vendaDAO.findById(id);

        if (v != null) {
            // Mostrar detalhes da venda antes de deletar
            System.out.println("\n⚠️ ATENÇÃO! Você está prestes a deletar a venda:");
            System.out.println(v);

            List<ItemVenda> itens = itemVendaDAO.findByVenda(id);
            System.out.println("\nItens da venda:");
            for (ItemVenda item : itens) {
                Produto p = produtoDAO.findById(item.getIdProduto());
                System.out.println("  → " + p.getNome() + " | Qtd: " + item.getQuantidade());
            }

            System.out.print("\nTem certeza? (S/N): ");
            String confirmacao = scanner.nextLine();

            if (confirmacao.equalsIgnoreCase("S")) {
                // Primeiro deleta os itens da venda
                itemVendaDAO.deleteByVenda(id);
                // Depois deleta a venda
                vendaDAO.delete(id);
                System.out.println("✅ Venda deletada com sucesso!");
                System.out.println("   Estoque NÃO foi restaurado (histórico mantido).");
            } else {
                System.out.println("❌ Operação cancelada.");
            }
        } else {
            System.out.println("❌ Venda não encontrada!");
        }
    }

    // ==================== CONSULTAS ESPECIAIS (JOINs) ====================

    private static void menuConsultasEspeciais() throws SQLException {
        int opcao;
        do {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          CONSULTAS ESPECIAIS           ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. 📊 Relatório de Vendas");
            System.out.println("2. 🏭 Produtos por Fornecedor");
            System.out.println("3. 👥 Total de Vendas por Cliente");
            System.out.println("0. ↩️ Voltar");

            opcao = lerInteiro("Opção: ");

            switch (opcao) {
                case 1: relatorioVendas(); break;
                case 2: produtosPorFornecedor(); break;
                case 3: totalVendasPorCliente(); break;
            }
        } while (opcao != 0);
    }

    private static void relatorioVendas() throws SQLException {
        String sql = "SELECT v.id AS numero_venda, c.nome AS cliente, p.nome AS produto, " +
                "i.quantidade, (i.quantidade * i.preco_unitario) AS total_item, v.data_venda " +
                "FROM Vendas v " +
                "INNER JOIN Clientes c ON v.id_cliente = c.id " +
                "INNER JOIN Itens_Venda i ON v.id = i.id_venda " +
                "INNER JOIN Produtos p ON i.id_produto = p.id " +
                "ORDER BY v.id, v.data_venda";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n=== RELATÓRIO DE VENDAS ===");
            System.out.println("┌───────┬────────────┬──────────────────┬────────────┬────────────┬─────────────────────┐");
            System.out.println("│ Venda │ Cliente    │ Produto          │ Quantidade │ Total Item │ Data Venda          │");
            System.out.println("├───────┼────────────┼──────────────────┼────────────┼────────────┼─────────────────────┤");

            int currentVenda = -1;
            BigDecimal totalVenda = BigDecimal.ZERO;

            while (rs.next()) {
                int vendaId = rs.getInt("numero_venda");
                if (currentVenda != vendaId && currentVenda != -1) {
                    System.out.printf("│       │            │                  │            │ Total R$%-8.2f │                     │\n", totalVenda);
                    System.out.println("├───────┼────────────┼──────────────────┼────────────┼────────────┼─────────────────────┤");
                    totalVenda = BigDecimal.ZERO;
                }

                System.out.printf("│ %-5d │ %-10s │ %-16s │ %-10d │ R$%-8.2f │ %-19s │\n",
                        vendaId,
                        rs.getString("cliente"),
                        rs.getString("produto"),
                        rs.getInt("quantidade"),
                        rs.getBigDecimal("total_item"),
                        rs.getTimestamp("data_venda"));

                currentVenda = vendaId;
                totalVenda = totalVenda.add(rs.getBigDecimal("total_item"));
            }

            if (currentVenda != -1) {
                System.out.printf("│       │            │                  │            │ Total R$%-8.2f │                     │\n", totalVenda);
            }
            System.out.println("└───────┴────────────┴──────────────────┴────────────┴────────────┴─────────────────────┘");
        }
    }

    private static void produtosPorFornecedor() throws SQLException {
        String sql = "SELECT f.nome_fantasia AS fornecedor, p.nome AS produto, " +
                "p.preco, p.estoque " +
                "FROM Produtos p " +
                "INNER JOIN Fornecedores f ON p.id_fornecedor = f.id " +
                "ORDER BY f.nome_fantasia, p.nome";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n=== PRODUTOS POR FORNECEDOR ===");

            String currentFornecedor = "";
            while (rs.next()) {
                String fornecedor = rs.getString("fornecedor");
                if (!currentFornecedor.equals(fornecedor)) {
                    System.out.println("\n┌─────────────────────────────────────────────────┐");
                    System.out.printf("│ 🏢 FORNECEDOR: %-30s │\n", fornecedor);
                    System.out.println("├─────────────────┬─────────────────┬─────────────┤");
                    System.out.println("│ PRODUTO         │ PREÇO           │ ESTOQUE     │");
                    System.out.println("├─────────────────┼─────────────────┼─────────────┤");
                    currentFornecedor = fornecedor;
                }
                System.out.printf("│ %-15s │ R$%-14.2f │ %-10d │\n",
                        rs.getString("produto"),
                        rs.getBigDecimal("preco"),
                        rs.getInt("estoque"));
            }
            System.out.println("└─────────────────┴─────────────────┴─────────────┘");
        }
    }

    private static void totalVendasPorCliente() throws SQLException {
        String sql = "SELECT c.nome AS cliente, COUNT(DISTINCT v.id) AS total_vendas, " +
                "SUM(i.quantidade * i.preco_unitario) AS total_gasto " +
                "FROM Clientes c " +
                "LEFT JOIN Vendas v ON c.id = v.id_cliente " +
                "LEFT JOIN Itens_Venda i ON v.id = i.id_venda " +
                "GROUP BY c.id, c.nome " +
                "ORDER BY total_gasto DESC";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n=== TOTAL DE VENDAS POR CLIENTE ===");
            System.out.println("┌─────────────────────┬─────────────────┬─────────────────┐");
            System.out.println("│ CLIENTE             │ TOTAL DE VENDAS │ TOTAL GASTO     │");
            System.out.println("├─────────────────────┼─────────────────┼─────────────────┤");

            while (rs.next()) {
                BigDecimal totalGasto = rs.getBigDecimal("total_gasto");
                if (totalGasto == null) {
                    totalGasto = BigDecimal.ZERO;
                }
                System.out.printf("│ %-19s │ %-15d │ R$%-14.2f │\n",
                        rs.getString("cliente"),
                        rs.getInt("total_vendas"),
                        totalGasto);
            }
            System.out.println("└─────────────────────┴─────────────────┴─────────────────┘");
        }
    }

    // ==================== MOSTRAR TODAS AS TABELAS ====================

    private static void mostrarTodasTabelas() throws SQLException {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       TODAS AS TABELAS DO SISTEMA      ║");
        System.out.println("╚════════════════════════════════════════╝");

        System.out.println("\n📋 FORNECEDORES:");
        listarFornecedores();

        System.out.println("\n📋 CLIENTES:");
        listarClientes();

        System.out.println("\n📋 PRODUTOS:");
        listarProdutos();

        System.out.println("\n📋 VENDAS:");
        listarVendas();

        System.out.println("\n✅ Consulta finalizada!");
    }
}
