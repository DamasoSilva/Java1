package com.salaobeleza.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "profissionais")
public class Profissional {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Usuario usuario;
    
    private String especialidade;
    
    @Column(name = "data_contratacao")
    private LocalDate dataContratacao;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "profissional_servico",
        joinColumns = @JoinColumn(name = "profissional_id"),
        inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<Servico> servicos = new ArrayList<>();
    
    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HorarioDisponivel> horariosDisponiveis = new ArrayList<>();
    
    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Agendamento> agendamentos = new ArrayList<>();
    
    // Construtores
    public Profissional() {
        this.dataContratacao = LocalDate.now();
    }
    
    public Profissional(Usuario usuario, String especialidade) {
        this();
        this.usuario = usuario;
        this.especialidade = especialidade;
        this.usuario.setTipoUsuario(TipoUsuario.PROFISSIONAL);
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            usuario.setTipoUsuario(TipoUsuario.PROFISSIONAL);
        }
    }
    
    public String getEspecialidade() {
        return especialidade;
    }
    
    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
    
    public LocalDate getDataContratacao() {
        return dataContratacao;
    }
    
    public void setDataContratacao(LocalDate dataContratacao) {
        this.dataContratacao = dataContratacao;
    }
    
    public List<Servico> getServicos() {
        return servicos;
    }
    
    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
    
    public List<HorarioDisponivel> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }
    
    public void setHorariosDisponiveis(List<HorarioDisponivel> horariosDisponiveis) {
        this.horariosDisponiveis = horariosDisponiveis;
    }
    
    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }
    
    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }
    
    // Métodos de conveniência
    public String getNome() {
        return usuario != null ? usuario.getNome() : null;
    }
    
    public String getEmail() {
        return usuario != null ? usuario.getEmail() : null;
    }
    
    public String getTelefone() {
        return usuario != null ? usuario.getTelefone() : null;
    }
    
    public String getNomeCompleto() {
        return usuario != null ? usuario.getNomeCompleto() : null;
    }
    
    public void adicionarServico(Servico servico) {
        if (!servicos.contains(servico)) {
            servicos.add(servico);
            servico.getProfissionais().add(this);
        }
    }
    
    public void removerServico(Servico servico) {
        servicos.remove(servico);
        servico.getProfissionais().remove(this);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profissional that = (Profissional) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Profissional{" +
                "id=" + id +
                ", usuario=" + (usuario != null ? usuario.getNomeCompleto() : "null") +
                ", especialidade='" + especialidade + '\'' +
                ", dataContratacao=" + dataContratacao +
                '}';
    }
}

