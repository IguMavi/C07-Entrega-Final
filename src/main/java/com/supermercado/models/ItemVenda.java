package com.supermercado.models;

import java.math.BigDecimal;

public class ItemVenda {
    private int idVenda;
    private int idProduto;
    private int quantidade;
    private BigDecimal precoUnitario;

    public ItemVenda() {}

    public ItemVenda(int idVenda, int idProduto, int quantidade, BigDecimal precoUnitario) {
        this.idVenda = idVenda;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
    public int getIdVenda() { return idVenda; }
    public void setIdVenda(int idVenda) { this.idVenda = idVenda; }

    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    @Override
    public String toString() {
        return String.format("Venda ID: %d | Produto ID: %d | Qtd: %d | Preço Unit: R$%.2f",
                idVenda, idProduto, quantidade, precoUnitario);
    }
}