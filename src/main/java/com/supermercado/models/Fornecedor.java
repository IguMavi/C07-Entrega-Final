package com.supermercado.models;

public class Fornecedor {
    private int id;
    private String nomeFantasia;
    private String cnpj;

    public Fornecedor() {}

    public Fornecedor(String nomeFantasia, String cnpj) {
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    public Fornecedor(int id, String nomeFantasia, String cnpj) {
        this.id = id;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    @Override
    public String toString() {
        return String.format("ID: %d | Nome: %s | CNPJ: %s", id, nomeFantasia, cnpj);
    }
}