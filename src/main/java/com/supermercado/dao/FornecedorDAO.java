package com.supermercado.dao;

import com.supermercado.models.Fornecedor;
import com.supermercado.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    public void insert(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO Fornecedores (nome_fantasia, cnpj) VALUES (?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, fornecedor.getNomeFantasia());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    fornecedor.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Fornecedor> findAll() throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM Fornecedores";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fornecedores.add(extractFornecedor(rs));
            }
        }
        return fornecedores;
    }

    public Fornecedor findById(int id) throws SQLException {
        String sql = "SELECT * FROM Fornecedores WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractFornecedor(rs);
            }
        }
        return null;
    }

    public List<Fornecedor> searchByNome(String nome) throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM Fornecedores WHERE nome_fantasia LIKE ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                fornecedores.add(extractFornecedor(rs));
            }
        }
        return fornecedores;
    }

    public void update(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE Fornecedores SET nome_fantasia = ?, cnpj = ? WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setString(1, fornecedor.getNomeFantasia());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setInt(3, fornecedor.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Fornecedores WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Fornecedor extractFornecedor(ResultSet rs) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(rs.getInt("id"));
        fornecedor.setNomeFantasia(rs.getString("nome_fantasia"));
        fornecedor.setCnpj(rs.getString("cnpj"));
        return fornecedor;
    }
}