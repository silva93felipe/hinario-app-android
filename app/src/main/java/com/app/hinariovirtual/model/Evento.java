package com.app.hinariovirtual.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Model que representa um evento no sistema
 */
public class Evento {
    private int id;
    private String descricao;
    private List<Hino> hinos;

    public Evento() {
        this.hinos = new ArrayList<>();
    }

    public Evento(String descricao) {
        this.descricao = descricao;
        this.hinos = new ArrayList<>();
    }

    public Evento(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.hinos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Hino> getHinos() {
        return hinos;
    }

    public void setHinos(List<Hino> hinos) {
        this.hinos = hinos;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
