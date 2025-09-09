package com.salaobeleza.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "servicos")
public class Servico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome do serviço é obrigatório")
    @Column(unique = true, nullable = false)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;
    
    @NotNull(message = "Duração é obrigatória")
    @Min(value = 1, message = "Duração deve ser pelo menos 1 minuto")
    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;
    
    @ManyToMany(mappedBy = "servicos", fetch = FetchType.LAZY)
    private List<Profissional> profissionais = new ArrayList<>();
    
    @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Agendamento> agendamentos = new ArrayList<>();
    
    // Construtores
    public Servico() {}
    
    public Servico(String nome, String descricao, BigDecimal preco, Integer duracaoMinutos) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    
    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }
    
    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }
    
    public List<Profissional> getProfissionais() {
        return profissionais;
    }
    
    public void setProfissionais(List<Profissional> profissionais) {
        this.profissionais = profissionais;
    }
    
    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }
    
    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }
    
    // Métodos de conveniência
    public String getDuracaoFormatada() {
        if (duracaoMinutos == null) return "0 min";
        
        int horas = duracaoMinutos / 60;
        int minutos = duracaoMinutos % 60;
        
        if (horas > 0 && minutos > 0) {
            return horas + "h " + minutos + "min";
        } else if (horas > 0) {
            return horas + "h";
        } else {
            return minutos + "min";
        }
    }
    
    public String getPrecoFormatado() {
        return preco != null ? "R$ " + preco.toString() : "R$ 0,00";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servico servico = (Servico) o;
        return Objects.equals(id, servico.id) && Objects.equals(nome, servico.nome);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }
    
    @Override
    public String toString() {
        return "Servico{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", duracaoMinutos=" + duracaoMinutos +
                '}';
    }
}

