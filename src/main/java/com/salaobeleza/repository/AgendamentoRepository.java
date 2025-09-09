package com.salaobeleza.repository;

import com.salaobeleza.model.Agendamento;
import com.salaobeleza.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    /**
     * Busca agendamentos de um cliente
     */
    @Query("SELECT a FROM Agendamento a WHERE a.cliente.id = :clienteId ORDER BY a.dataAgendamento DESC, a.horaAgendamento DESC")
    List<Agendamento> findByClienteId(@Param("clienteId") Long clienteId);
    
    /**
     * Busca agendamentos de um profissional
     */
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId ORDER BY a.dataAgendamento DESC, a.horaAgendamento DESC")
    List<Agendamento> findByProfissionalId(@Param("profissionalId") Long profissionalId);
    
    /**
     * Busca agendamentos por status
     */
    List<Agendamento> findByStatusOrderByDataAgendamentoDescHoraAgendamentoDesc(StatusAgendamento status);
    
    /**
     * Busca agendamentos de um cliente por status
     */
    @Query("SELECT a FROM Agendamento a WHERE a.cliente.id = :clienteId AND a.status = :status ORDER BY a.dataAgendamento DESC, a.horaAgendamento DESC")
    List<Agendamento> findByClienteIdAndStatus(@Param("clienteId") Long clienteId, @Param("status") StatusAgendamento status);
    
    /**
     * Busca agendamentos de um profissional por status
     */
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.status = :status ORDER BY a.dataAgendamento DESC, a.horaAgendamento DESC")
    List<Agendamento> findByProfissionalIdAndStatus(@Param("profissionalId") Long profissionalId, @Param("status") StatusAgendamento status);
    
    /**
     * Busca agendamentos em uma data específica
     */
    @Query("SELECT a FROM Agendamento a WHERE a.dataAgendamento = :data ORDER BY a.horaAgendamento")
    List<Agendamento> findByDataAgendamento(@Param("data") LocalDate data);
    
    /**
     * Busca agendamentos de um profissional em uma data específica
     */
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataAgendamento = :data ORDER BY a.horaAgendamento")
    List<Agendamento> findByProfissionalIdAndDataAgendamento(@Param("profissionalId") Long profissionalId, @Param("data") LocalDate data);
    
    /**
     * Busca agendamentos em um período
     */
    @Query("SELECT a FROM Agendamento a WHERE a.dataAgendamento BETWEEN :dataInicio AND :dataFim ORDER BY a.dataAgendamento, a.horaAgendamento")
    List<Agendamento> findByDataAgendamentoBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
    
    /**
     * Busca agendamentos de um profissional em um período
     */
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataAgendamento BETWEEN :dataInicio AND :dataFim ORDER BY a.dataAgendamento, a.horaAgendamento")
    List<Agendamento> findByProfissionalIdAndDataAgendamentoBetween(@Param("profissionalId") Long profissionalId, 
                                                                   @Param("dataInicio") LocalDate dataInicio, 
                                                                   @Param("dataFim") LocalDate dataFim);
    
    /**
     * Verifica se existe agendamento conflitante
     */
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataAgendamento = :data AND a.horaAgendamento = :hora AND a.status != 'CANCELADO'")
    Optional<Agendamento> findConflitante(@Param("profissionalId") Long profissionalId, 
                                         @Param("data") LocalDate data, 
                                         @Param("hora") LocalTime hora);
    
    /**
     * Busca agendamentos criados em um período
     */
    @Query("SELECT a FROM Agendamento a WHERE a.dataCriacao BETWEEN :dataInicio AND :dataFim ORDER BY a.dataCriacao DESC")
    List<Agendamento> findByDataCriacaoBetween(@Param("dataInicio") LocalDateTime dataInicio, 
                                              @Param("dataFim") LocalDateTime dataFim);
    
    /**
     * Busca agendamentos de hoje
     */
    @Query("SELECT a FROM Agendamento a WHERE a.dataAgendamento = CURRENT_DATE ORDER BY a.horaAgendamento")
    List<Agendamento> findAgendamentosHoje();
    
    /**
     * Busca próximos agendamentos de um cliente
     */
    @Query("SELECT a FROM Agendamento a WHERE a.cliente.id = :clienteId AND a.dataAgendamento >= CURRENT_DATE AND a.status != 'CANCELADO' ORDER BY a.dataAgendamento, a.horaAgendamento")
    List<Agendamento> findProximosAgendamentosCliente(@Param("clienteId") Long clienteId);
    
    /**
     * Busca próximos agendamentos de um profissional
     */
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataAgendamento >= CURRENT_DATE AND a.status != 'CANCELADO' ORDER BY a.dataAgendamento, a.horaAgendamento")
    List<Agendamento> findProximosAgendamentosProfissional(@Param("profissionalId") Long profissionalId);
    
    /**
     * Conta agendamentos por status
     */
    long countByStatus(StatusAgendamento status);
    
    /**
     * Conta agendamentos de um profissional por status
     */
    @Query("SELECT COUNT(a) FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.status = :status")
    long countByProfissionalIdAndStatus(@Param("profissionalId") Long profissionalId, @Param("status") StatusAgendamento status);
    
    /**
     * Busca agendamentos de um serviço específico
     */
    @Query("SELECT a FROM Agendamento a WHERE a.servico.id = :servicoId ORDER BY a.dataAgendamento DESC, a.horaAgendamento DESC")
    List<Agendamento> findByServicoId(@Param("servicoId") Long servicoId);
    
    /**
     * Busca agendamentos pendentes (para notificações)
     */
    @Query("SELECT a FROM Agendamento a WHERE a.status = 'PENDENTE' AND a.dataAgendamento >= CURRENT_DATE ORDER BY a.dataAgendamento, a.horaAgendamento")
    List<Agendamento> findAgendamentosPendentes();
}

