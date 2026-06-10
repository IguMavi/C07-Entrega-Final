package com.supermercado.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Venda {
    private int id;
    private Timestamp dataVenda;
    private int idCliente;

    public Venda() {
        this.dataVenda = Timestamp.valueOf(LocalDateTime.now());
    }

    public Venda(int idCliente) {
        this.idCliente = idCliente;
        this.dataVenda = Timestamp.valueOf(LocalDateTime.now());
    }

    public Venda(int id, Timestamp dataVenda, int idCliente) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.idCliente = idCliente;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Timestamp getDataVenda() { return dataVenda; }
    public void setDataVenda(Timestamp dataVenda) { this.dataVenda = dataVenda; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    @Override
    public String toString() {
        return String.format("ID: %d | Data: %s | Cliente ID: %d", id, dataVenda, idCliente);
    }
}