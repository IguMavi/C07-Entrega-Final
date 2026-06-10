package com.supermercado.dao;

import com.supermercado.models.ItemVenda;
import com.supermercado.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDAO {

    public void insert(ItemVenda itemVenda) throws SQLException {
        String sql = "INSERT INTO Itens_Venda (id_venda, id_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, itemVenda.getIdVenda());
            stmt.setInt(2, itemVenda.getIdProduto());
            stmt.setInt(3, itemVenda.getQuantidade());
            stmt.setBigDecimal(4, itemVenda.getPrecoUnitario());
            stmt.executeUpdate();
        }
    }

    public List<ItemVenda> findByVenda(int idVenda) throws SQLException {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT * FROM Itens_Venda WHERE id_venda = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                itens.add(extractItemVenda(rs));
            }
        }
        return itens;
    }

    public void delete(int idVenda, int idProduto) throws SQLException {
        String sql = "DELETE FROM Itens_Venda WHERE id_venda = ? AND id_produto = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
        }
    }

    public void deleteByVenda(int idVenda) throws SQLException {
        String sql = "DELETE FROM Itens_Venda WHERE id_venda = ?";

        try (PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            stmt.executeUpdate();
        }
    }

    private ItemVenda extractItemVenda(ResultSet rs) throws SQLException {
        ItemVenda item = new ItemVenda();
        item.setIdVenda(rs.getInt("id_venda"));
        item.setIdProduto(rs.getInt("id_produto"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
        return item;
    }
}