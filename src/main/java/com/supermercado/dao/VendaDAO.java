package com.supermercado.dao;

import com.supermercado.models.Venda;
import com.supermercado.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public void insert(Venda venda) throws SQLException {
        String sql = "INSERT INTO Vendas (data_venda, id_cliente) VALUES (?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, venda.getDataVenda());
            stmt.setInt(2, venda.getIdCliente());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venda.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Venda> findAll() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM Vendas";

        try (Statement stmt = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vendas.add(extractVenda(rs));
            }
        }
        return vendas;
    }

    public Venda findById(int id) throws SQLException {
        String sql = "SELECT * FROM Vendas WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractVenda(rs);
            }
        }
        return null;
    }

    public List<Venda> findByCliente(int idCliente) throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM Vendas WHERE id_cliente = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vendas.add(extractVenda(rs));
            }
        }
        return vendas;
    }

    public void update(Venda venda) throws SQLException {
        String sql = "UPDATE Vendas SET data_venda = ?, id_cliente = ? WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setTimestamp(1, venda.getDataVenda());
            stmt.setInt(2, venda.getIdCliente());
            stmt.setInt(3, venda.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Vendas WHERE id = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Venda extractVenda(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setId(rs.getInt("id"));
        venda.setDataVenda(rs.getTimestamp("data_venda"));
        venda.setIdCliente(rs.getInt("id_cliente"));
        return venda;
    }
}