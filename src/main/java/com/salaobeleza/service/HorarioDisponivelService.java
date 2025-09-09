package com.salaobeleza.service;

import com.salaobeleza.model.HorarioDisponivel;
import com.salaobeleza.model.Profissional;
import com.salaobeleza.repository.HorarioDisponivelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HorarioDisponivelService {
    
    @Autowired
    private HorarioDisponivelRepository horarioDisponivelRepository;
    
    @Autowired
    private ProfissionalService profissionalService;
    
    /**
     * Salva um novo horário disponível
     */
    public HorarioDisponivel salvar(HorarioDisponivel horario) {
        validarHorario(horario);
        return horarioDisponivelRepository.save(horario);
    }
    
    /**
     * Atualiza um horário disponível existente
     */
    public HorarioDisponivel atualizar(HorarioDisponivel horario) {
        Optional<HorarioDisponivel> horarioExistente = horarioDisponivelRepository.findById(horario.getId());
        if (horarioExistente.isPresent()) {
            validarHorario(horario);
            
            HorarioDisponivel horarioAtual = horarioExistente.get();
            horarioAtual.setProfissional(horario.getProfissional());
            horarioAtual.setData(horario.getData());
            horarioAtual.setHoraInicio(horario.getHoraInicio());
            horarioAtual.setHoraFim(horario.getHoraFim());
            horarioAtual.setDisponivel(horario.getDisponivel());
            
            return horarioDisponivelRepository.save(horarioAtual);
        }
        throw new RuntimeException("Horário disponível não encontrado com ID: " + horario.getId());
    }
    
    /**
     * Busca horário disponível por ID
     */
    @Transactional(readOnly = true)
    public Optional<HorarioDisponivel> buscarPorId(Long id) {
        return horarioDisponivelRepository.findById(id);
    }
    
    /**
     * Lista horários disponíveis de um profissional em uma data
     */
    @Transactional(readOnly = true)
    public List<HorarioDisponivel> listarPorProfissionalEData(Long profissionalId, LocalDate data) {
        return horarioDisponivelRepository.findByProfissionalIdAndDataAndDisponivelTrue(profissionalId, data);
    }
    
    /**
     * Lista todos os horários disponíveis de um profissional
     */
    @Transactional(readOnly = true)
    public List<HorarioDisponivel> listarTodosPorProfissional(Long profissionalId) {
        Profissional profissional = profissionalService.buscarPorId(profissionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        return profissional.getHorariosDisponiveis();
    }
    
    /**
     * Lista todos os horários disponíveis
     */
    @Transactional(readOnly = true)
    public List<HorarioDisponivel> listarTodos() {
        return horarioDisponivelRepository.findAll();
    }
    
    /**
     * Remove horário disponível
     */
    public void remover(Long id) {
        if (horarioDisponivelRepository.existsById(id)) {
            horarioDisponivelRepository.deleteById(id);
        } else {
            throw new RuntimeException("Horário disponível não encontrado com ID: " + id);
        }
    }
    
    /**
     * Valida dados do horário disponível
     */
    private void validarHorario(HorarioDisponivel horario) {
        if (horario.getProfissional() == null || horario.getProfissional().getId() == null) {
            throw new IllegalArgumentException("Profissional é obrigatório");
        }
        if (horario.getData() == null) {
            throw new IllegalArgumentException("Data é obrigatória");
        }
        if (horario.getHoraInicio() == null) {
            throw new IllegalArgumentException("Hora de início é obrigatória");
        }
        if (horario.getHoraFim() == null) {
            throw new IllegalArgumentException("Hora de fim é obrigatória");
        }
        if (horario.getHoraInicio().isAfter(horario.getHoraFim()) || horario.getHoraInicio().equals(horario.getHoraFim())) {
            throw new IllegalArgumentException("Hora de início deve ser anterior à hora de fim");
        }
        
        // Verifica conflitos de horário para o mesmo profissional e data
        List<HorarioDisponivel> conflitos = horarioDisponivelRepository.findHorariosConflitantes(
                horario.getProfissional().getId(), horario.getData(), horario.getHoraInicio(), horario.getHoraFim());
        
        if (!conflitos.isEmpty()) {
            for (HorarioDisponivel conflito : conflitos) {
                if (horario.getId() == null || !horario.getId().equals(conflito.getId())) {
                    throw new IllegalArgumentException("Já existe um horário para este profissional neste período: " + conflito.getHorarioFormatado());
                }
            }
        }
    }
    
    /**
     * Marca um horário como indisponível
     */
    public HorarioDisponivel marcarComoIndisponivel(Long id) {
        HorarioDisponivel horario = horarioDisponivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário disponível não encontrado"));
        horario.setDisponivel(false);
        return horarioDisponivelRepository.save(horario);
    }
    
    /**
     * Marca um horário como disponível
     */
    public HorarioDisponivel marcarComoDisponivel(Long id) {
        HorarioDisponivel horario = horarioDisponivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário disponível não encontrado"));
        horario.setDisponivel(true);
        return horarioDisponivelRepository.save(horario);
    }
    
    /**
     * Busca horários disponíveis em um período
     */
    @Transactional(readOnly = true)
    public List<HorarioDisponivel> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return horarioDisponivelRepository.findByDataBetweenAndDisponivelTrue(dataInicio, dataFim);
    }
    
    /**
     * Busca horários disponíveis de um profissional a partir de uma data
     */
    @Transactional(readOnly = true)
    public List<HorarioDisponivel> listarPorProfissionalAPartirDe(Long profissionalId, LocalDate dataInicio) {
        return horarioDisponivelRepository.findByProfissionalIdAndDataGreaterThanEqualAndDisponivelTrue(profissionalId, dataInicio);
    }
    
}

