package com.salaobeleza.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "horarios_disponiveis", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"profissional_id", "data", "hora_inicio"}))
public class HorarioDisponivel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;
    
    @NotNull(message = "Data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;
    
    @NotNull(message = "Hora de início é obrigatória")
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    
    @NotNull(message = "Hora de fim é obrigatória")
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;
    
    @Column(nullable = false)
    private Boolean disponivel = true;
    
    // Construtores
    public HorarioDisponivel() {}
    
    public HorarioDisponivel(Profissional profissional, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        this.profissional = profissional;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.disponivel = true;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Profissional getProfissional() {
        return profissional;
    }
    
    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }
    
    public LocalDate getData() {
        return data;
    }
    
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public LocalTime getHoraFim() {
        return horaFim;
    }
    
    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }
    
    public Boolean getDisponivel() {
        return disponivel;
    }
    
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    // Métodos de conveniência
    public String getDataFormatada() {
        return data != null ? data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }
    
    public String getHoraInicioFormatada() {
        return horaInicio != null ? horaInicio.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }
    
    public String getHoraFimFormatada() {
        return horaFim != null ? horaFim.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }
    
    public String getHorarioFormatado() {
        return getHoraInicioFormatada() + " - " + getHoraFimFormatada();
    }
    
    public boolean isDisponivel() {
        return disponivel != null && disponivel;
    }
    
    public void marcarComoIndisponivel() {
        this.disponivel = false;
    }
    
    public void marcarComoDisponivel() {
        this.disponivel = true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HorarioDisponivel that = (HorarioDisponivel) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(profissional, that.profissional) &&
               Objects.equals(data, that.data) &&
               Objects.equals(horaInicio, that.horaInicio);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, profissional, data, horaInicio);
    }
    
    @Override
    public String toString() {
        return "HorarioDisponivel{" +
                "id=" + id +
                ", profissional=" + (profissional != null ? profissional.getNomeCompleto() : "null") +
                ", data=" + data +
                ", horaInicio=" + horaInicio +
                ", horaFim=" + horaFim +
                ", disponivel=" + disponivel +
                '}';
    }
}

