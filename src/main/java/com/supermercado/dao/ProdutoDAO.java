package com.supermercado.dao;

import com.supermercado.models.Produto;
import com.supermercado.db.DatabaseConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void insert(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produtos (nome, preco, estoque, id_fornecedor) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.setInt(4, produto.getIdFornecedor());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Produto> findAll() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produtos.add(extractProduto(rs));
            }
        }
        return produtos;
    }

    public Produto findById(int id) throws SQLException {
        String sql = "SELECT * FROM Produtos WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractProduto(rs);
            }
        }
        return null;
    }

    public List<Produto> searchByNome(String nome) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produtos WHERE nome LIKE ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                produtos.add(extractProduto(rs));
            }
        }
        return produtos;
    }

    public void update(Produto produto) throws SQLException {
        String sql = "UPDATE Produtos SET nome = ?, preco = ?, estoque = ?, id_fornecedor = ? WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.setInt(4, produto.getIdFornecedor());
            stmt.setInt(5, produto.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Produtos WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Produto extractProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getBigDecimal("preco"));
        produto.setEstoque(rs.getInt("estoque"));
        produto.setIdFornecedor(rs.getInt("id_fornecedor"));
        return produto;
    }
}