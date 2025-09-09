package com.salaobeleza.model;

public enum TipoUsuario {
    CLIENTE("Cliente"),
    PROFISSIONAL("Profissional"),
    ADMIN("Administrador");
    
    private final String descricao;
    
    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
}

