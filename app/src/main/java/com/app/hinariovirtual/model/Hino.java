package com.app.hinariovirtual.model;

/**
 * Classe Model que representa um cadastro no sistema
 */
public class Hino implements Comparable<Hino> {
    private int id;
    private String codigo;
    private String titulo;
    private String texto;

    public Hino() {
    }

    public Hino(String codigo, String titulo, String texto) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.texto = texto;
    }

    public Hino(int id, String codigo, String titulo, String texto) {
        this.id = id;
        this.codigo = codigo;
        this.titulo = titulo;
        this.texto = texto;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public int compareTo(Hino outro) {
        // Ordenação por código
        return this.codigo.compareTo(outro.codigo);
    }

    @Override
    public String toString() {
        return codigo + " - " + titulo;
    }
}