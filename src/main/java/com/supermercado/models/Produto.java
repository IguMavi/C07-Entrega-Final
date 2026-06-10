package com.supermercado.models;

import java.math.BigDecimal;

public class Produto {
    private int id;
    private String nome;
    private BigDecimal preco;
    private int estoque;
    private int idFornecedor;

    public Produto() {}

    public Produto(String nome, BigDecimal preco, int estoque, int idFornecedor) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.idFornecedor = idFornecedor;
    }

    public Produto(int id, String nome, BigDecimal preco, int estoque, int idFornecedor) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.idFornecedor = idFornecedor;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    public int getIdFornecedor() { return idFornecedor; }
    public void setIdFornecedor(int idFornecedor) { this.idFornecedor = idFornecedor; }

    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s | Preço: R$%.2f | Estoque: %d | Fornecedor ID: %d",
                id, nome, preco, estoque, idFornecedor);
    }
}