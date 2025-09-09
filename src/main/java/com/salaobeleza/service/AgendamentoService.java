package com.salaobeleza.service;

import com.salaobeleza.model.*;
import com.salaobeleza.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AgendamentoService {
    
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ProfissionalService profissionalService;
    
    @Autowired
    private ServicoService servicoService;
    
    @Autowired
    private HorarioDisponivelService horarioDisponivelService;
    
    /**
     * Realiza um novo agendamento
     */
    public Agendamento agendar(Agendamento agendamento) {
        validarAgendamento(agendamento);
        
        // Busca o horário disponível correspondente e o marca como indisponível
        List<HorarioDisponivel> horariosDisponiveis = horarioDisponivelService.listarPorProfissionalEData(
                agendamento.getProfissional().getId(), 
                agendamento.getDataAgendamento()
        );
        
        Optional<HorarioDisponivel> horario = horariosDisponiveis.stream()
                .filter(h -> h.getHoraInicio().equals(agendamento.getHoraAgendamento()))
                .findFirst();
        
        if (horario.isPresent() && horario.get().isDisponivel()) {
            horarioDisponivelService.marcarComoIndisponivel(horario.get().getId());
        } else {
            throw new RuntimeException("Horário selecionado não está disponível ou não existe.");
        }
        
        // Define a duração do agendamento com base no serviço
        agendamento.setDuracaoMinutos(agendamento.getServico().getDuracaoMinutos());
        
        // Define a data de criação
        if (agendamento.getDataCriacao() == null) {
            agendamento.setDataCriacao(LocalDateTime.now());
        }
        
        return agendamentoRepository.save(agendamento);
    }
    
    /**
     * Atualiza um agendamento existente
     */
    public Agendamento atualizar(Agendamento agendamento) {
        Optional<Agendamento> agendamentoExistente = agendamentoRepository.findById(agendamento.getId());
        if (agendamentoExistente.isPresent()) {
            Agendamento agendamentoAtual = agendamentoExistente.get();
            
            // Se o horário ou profissional mudou, precisa liberar o antigo e ocupar o novo
            if (!agendamentoAtual.getProfissional().equals(agendamento.getProfissional()) ||
                !agendamentoAtual.getDataAgendamento().equals(agendamento.getDataAgendamento()) ||
                !agendamentoAtual.getHoraAgendamento().equals(agendamento.getHoraAgendamento())) {
                
                // Libera o horário antigo
                List<HorarioDisponivel> horariosAntigos = horarioDisponivelService.listarPorProfissionalEData(
                        agendamentoAtual.getProfissional().getId(), 
                        agendamentoAtual.getDataAgendamento()
                );
                Optional<HorarioDisponivel> horarioAntigo = horariosAntigos.stream()
                        .filter(h -> h.getHoraInicio().equals(agendamentoAtual.getHoraAgendamento()))
                        .findFirst();
                horarioAntigo.ifPresent(hd -> horarioDisponivelService.marcarComoDisponivel(hd.getId()));
                
                // Ocupa o novo horário
                List<HorarioDisponivel> horariosNovos = horarioDisponivelService.listarPorProfissionalEData(
                        agendamento.getProfissional().getId(), 
                        agendamento.getDataAgendamento()
                );
                Optional<HorarioDisponivel> horarioNovo = horariosNovos.stream()
                        .filter(h -> h.getHoraInicio().equals(agendamento.getHoraAgendamento()))
                        .findFirst();
                if (horarioNovo.isPresent() && horarioNovo.get().isDisponivel()) {
                    horarioDisponivelService.marcarComoIndisponivel(horarioNovo.get().getId());
                } else {
                    throw new RuntimeException("Novo horário selecionado não está disponível ou não existe.");
                }
            }
            
            agendamentoAtual.setCliente(agendamento.getCliente());
            agendamentoAtual.setProfissional(agendamento.getProfissional());
            agendamentoAtual.setServico(agendamento.getServico());
            agendamentoAtual.setDataAgendamento(agendamento.getDataAgendamento());
            agendamentoAtual.setHoraAgendamento(agendamento.getHoraAgendamento());
            agendamentoAtual.setDuracaoMinutos(agendamento.getServico().getDuracaoMinutos());
            agendamentoAtual.setStatus(agendamento.getStatus());
            agendamentoAtual.setObservacoes(agendamento.getObservacoes());
            
            return agendamentoRepository.save(agendamentoAtual);
        }
        throw new RuntimeException("Agendamento não encontrado com ID: " + agendamento.getId());
    }
    
    /**
     * Busca agendamento por ID
     */
    @Transactional(readOnly = true)
    public Optional<Agendamento> buscarPorId(Long id) {
        return agendamentoRepository.findById(id);
    }
    
    /**
     * Lista todos os agendamentos
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }
    
    /**
     * Lista agendamentos de um cliente
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);
    }
    
    /**
     * Lista agendamentos de um profissional
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorProfissional(Long profissionalId) {
        return agendamentoRepository.findByProfissionalId(profissionalId);
    }
    
    /**
     * Lista agendamentos por status
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorStatus(StatusAgendamento status) {
        return agendamentoRepository.findByStatusOrderByDataAgendamentoDescHoraAgendamentoDesc(status);
    }
    
    /**
     * Lista agendamentos em uma data específica
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorData(LocalDate data) {
        return agendamentoRepository.findByDataAgendamento(data);
    }
    
    /**
     * Lista agendamentos de um profissional em uma data específica
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorProfissionalEData(Long profissionalId, LocalDate data) {
        return agendamentoRepository.findByProfissionalIdAndDataAgendamento(profissionalId, data);
    }
    
    /**
     * Confirma um agendamento
     */
    public Agendamento confirmarAgendamento(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamento.confirmar();
        return agendamentoRepository.save(agendamento);
    }
    
    /**
     * Cancela um agendamento
     */
    public Agendamento cancelarAgendamento(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        
        // Libera o horário que estava ocupado
        List<HorarioDisponivel> horarios = horarioDisponivelService.listarPorProfissionalEData(
                agendamento.getProfissional().getId(), 
                agendamento.getDataAgendamento()
        );
        Optional<HorarioDisponivel> horario = horarios.stream()
                .filter(h -> h.getHoraInicio().equals(agendamento.getHoraAgendamento()))
                .findFirst();
        horario.ifPresent(hd -> horarioDisponivelService.marcarComoDisponivel(hd.getId()));
        
        agendamento.cancelar();
        return agendamentoRepository.save(agendamento);
    }
    
    /**
     * Conclui um agendamento
     */
    public Agendamento concluirAgendamento(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamento.concluir();
        return agendamentoRepository.save(agendamento);
    }
    
    /**
     * Remove agendamento
     */
    public void remover(Long id) {
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        if (agendamento.isPresent()) {
            // Libera o horário que estava ocupado, se o agendamento não foi cancelado
            if (!agendamento.get().isCancelado()) {
                List<HorarioDisponivel> horarios = horarioDisponivelService.listarPorProfissionalEData(
                        agendamento.get().getProfissional().getId(), 
                        agendamento.get().getDataAgendamento()
                );
                Optional<HorarioDisponivel> horario = horarios.stream()
                        .filter(h -> h.getHoraInicio().equals(agendamento.get().getHoraAgendamento()))
                        .findFirst();
                horario.ifPresent(hd -> horarioDisponivelService.marcarComoDisponivel(hd.getId()));
            }
            agendamentoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Agendamento não encontrado com ID: " + id);
        }
    }
    
    /**
     * Valida dados do agendamento
     */
    private void validarAgendamento(Agendamento agendamento) {
        if (agendamento.getCliente() == null || agendamento.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        if (agendamento.getProfissional() == null || agendamento.getProfissional().getId() == null) {
            throw new IllegalArgumentException("Profissional é obrigatório");
        }
        if (agendamento.getServico() == null || agendamento.getServico().getId() == null) {
            throw new IllegalArgumentException("Serviço é obrigatório");
        }
        if (agendamento.getDataAgendamento() == null) {
            throw new IllegalArgumentException("Data do agendamento é obrigatória");
        }
        if (agendamento.getHoraAgendamento() == null) {
            throw new IllegalArgumentException("Hora do agendamento é obrigatória");
        }
        
        // Verifica se cliente, profissional e serviço existem
        clienteService.buscarPorId(agendamento.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        profissionalService.buscarPorId(agendamento.getProfissional().getId())
                .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        servicoService.buscarPorId(agendamento.getServico().getId())
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));
        
        // Verifica se o horário está disponível para o profissional
        List<HorarioDisponivel> horariosDisponiveis = horarioDisponivelService.listarPorProfissionalEData(
                agendamento.getProfissional().getId(), 
                agendamento.getDataAgendamento()
        );
        Optional<HorarioDisponivel> horarioDisponivel = horariosDisponiveis.stream()
                .filter(h -> h.getHoraInicio().equals(agendamento.getHoraAgendamento()))
                .findFirst();
        
        if (horarioDisponivel.isEmpty() || !horarioDisponivel.get().isDisponivel()) {
            throw new IllegalArgumentException("Horário selecionado não está disponível para este profissional.");
        }
        
        // Verifica se já existe um agendamento para o mesmo profissional no mesmo horário
        Optional<Agendamento> conflito = agendamentoRepository.findConflitante(
                agendamento.getProfissional().getId(), 
                agendamento.getDataAgendamento(), 
                agendamento.getHoraAgendamento()
        );
        
        if (conflito.isPresent() && (agendamento.getId() == null || !agendamento.getId().equals(conflito.get().getId()))) {
            throw new IllegalArgumentException("Já existe um agendamento para este profissional neste horário.");
        }
        
        // Verifica se o serviço é oferecido pelo profissional
        boolean servicoOferecido = profissionalService.listarPorServico(agendamento.getServico().getId())
                .stream()
                .anyMatch(p -> p.getId().equals(agendamento.getProfissional().getId()));
        
        if (!servicoOferecido) {
            throw new IllegalArgumentException("O profissional selecionado não oferece este serviço.");
        }
    }
    
    /**
     * Busca próximos agendamentos de um cliente
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarProximosAgendamentosCliente(Long clienteId) {
        return agendamentoRepository.findProximosAgendamentosCliente(clienteId);
    }
    
    /**
     * Busca próximos agendamentos de um profissional
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarProximosAgendamentosProfissional(Long profissionalId) {
        return agendamentoRepository.findProximosAgendamentosProfissional(profissionalId);
    }
    
    /**
     * Conta agendamentos por status
     */
    @Transactional(readOnly = true)
    public long contarPorStatus(StatusAgendamento status) {
        return agendamentoRepository.countByStatus(status);
    }
    
    /**
     * Conta agendamentos de um profissional por status
     */
    @Transactional(readOnly = true)
    public long contarPorProfissionalEStatus(Long profissionalId, StatusAgendamento status) {
        return agendamentoRepository.countByProfissionalIdAndStatus(profissionalId, status);
    }
    
    /**
     * Busca agendamentos pendentes
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendamentosPendentes() {
        return agendamentoRepository.findAgendamentosPendentes();
    }
    
    /**
     * Busca agendamentos de hoje
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarAgendamentosHoje() {
        return agendamentoRepository.findAgendamentosHoje();
    }
    
    /**
     * Busca agendamentos em um período
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return agendamentoRepository.findByDataAgendamentoBetween(dataInicio, dataFim);
    }
    
    /**
     * Busca agendamentos de um profissional em um período
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorProfissionalEPeriodo(Long profissionalId, LocalDate dataInicio, LocalDate dataFim) {
        return agendamentoRepository.findByProfissionalIdAndDataAgendamentoBetween(profissionalId, dataInicio, dataFim);
    }
    
    /**
     * Busca agendamentos de um serviço específico
     */
    @Transactional(readOnly = true)
    public List<Agendamento> listarPorServico(Long servicoId) {
        return agendamentoRepository.findByServicoId(servicoId);
    }
}

