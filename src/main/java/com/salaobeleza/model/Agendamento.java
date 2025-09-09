package com.salaobeleza.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "agendamentos",
       uniqueConstraints = @UniqueConstraint(columnNames = {"profissional_id", "data_agendamento", "hora_agendamento"}))
public class Agendamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;
    
    @NotNull(message = "Data do agendamento é obrigatória")
    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;
    
    @NotNull(message = "Hora do agendamento é obrigatória")
    @Column(name = "hora_agendamento", nullable = false)
    private LocalTime horaAgendamento;
    
    @NotNull(message = "Duração é obrigatória")
    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    // Construtores
    public Agendamento() {
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusAgendamento.PENDENTE;
    }
    
    public Agendamento(Cliente cliente, Profissional profissional, Servico servico, 
                      LocalDate dataAgendamento, LocalTime horaAgendamento) {
        this();
        this.cliente = cliente;
        this.profissional = profissional;
        this.servico = servico;
        this.dataAgendamento = dataAgendamento;
        this.horaAgendamento = horaAgendamento;
        this.duracaoMinutos = servico.getDuracaoMinutos();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Profissional getProfissional() {
        return profissional;
    }
    
    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }
    
    public Servico getServico() {
        return servico;
    }
    
    public void setServico(Servico servico) {
        this.servico = servico;
        if (servico != null) {
            this.duracaoMinutos = servico.getDuracaoMinutos();
        }
    }
    
    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }
    
    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }
    
    public LocalTime getHoraAgendamento() {
        return horaAgendamento;
    }
    
    public void setHoraAgendamento(LocalTime horaAgendamento) {
        this.horaAgendamento = horaAgendamento;
    }
    
    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }
    
    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }
    
    public StatusAgendamento getStatus() {
        return status;
    }
    
    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    // Métodos de conveniência
    public String getDataAgendamentoFormatada() {
        return dataAgendamento != null ? dataAgendamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
    
    public String getHoraAgendamentoFormatada() {
        return horaAgendamento != null ? horaAgendamento.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }
    
    public LocalTime getHoraFim() {
        if (horaAgendamento != null && duracaoMinutos != null) {
            return horaAgendamento.plusMinutes(duracaoMinutos);
        }
        return null;
    }
    
    public String getHoraFimFormatada() {
        LocalTime horaFim = getHoraFim();
        return horaFim != null ? horaFim.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }
    
    public String getHorarioCompleto() {
        return getHoraAgendamentoFormatada() + " - " + getHoraFimFormatada();
    }
    
    public void confirmar() {
        this.status = StatusAgendamento.CONFIRMADO;
    }
    
    public void cancelar() {
        this.status = StatusAgendamento.CANCELADO;
    }
    
    public void concluir() {
        this.status = StatusAgendamento.CONCLUIDO;
    }
    
    public boolean isPendente() {
        return status == StatusAgendamento.PENDENTE;
    }
    
    public boolean isConfirmado() {
        return status == StatusAgendamento.CONFIRMADO;
    }
    
    public boolean isCancelado() {
        return status == StatusAgendamento.CANCELADO;
    }
    
    public boolean isConcluido() {
        return status == StatusAgendamento.CONCLUIDO;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", cliente=" + (cliente != null ? cliente.getNomeCompleto() : "null") +
                ", profissional=" + (profissional != null ? profissional.getNomeCompleto() : "null") +
                ", servico=" + (servico != null ? servico.getNome() : "null") +
                ", dataAgendamento=" + dataAgendamento +
                ", horaAgendamento=" + horaAgendamento +
                ", status=" + status +
                '}';
    }
}

