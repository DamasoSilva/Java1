package com.salaobeleza.repository;

import com.salaobeleza.model.HorarioDisponivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {
    
    /**
     * Busca horários disponíveis de um profissional em uma data específica
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.data = :data AND h.disponivel = true ORDER BY h.horaInicio")
    List<HorarioDisponivel> findByProfissionalIdAndDataAndDisponivelTrue(@Param("profissionalId") Long profissionalId, 
                                                                         @Param("data") LocalDate data);
    
    /**
     * Busca horários de um profissional em uma data específica (disponíveis ou não)
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.data = :data ORDER BY h.horaInicio")
    List<HorarioDisponivel> findByProfissionalIdAndData(@Param("profissionalId") Long profissionalId, 
                                                        @Param("data") LocalDate data);
    
    /**
     * Busca horário específico de um profissional
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.data = :data AND h.horaInicio = :horaInicio")
    Optional<HorarioDisponivel> findByProfissionalIdAndDataAndHoraInicio(@Param("profissionalId") Long profissionalId, 
                                                                         @Param("data") LocalDate data, 
                                                                         @Param("horaInicio") LocalTime horaInicio);
    
    /**
     * Busca horários disponíveis de um profissional em um período
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.data BETWEEN :dataInicio AND :dataFim AND h.disponivel = true ORDER BY h.data, h.horaInicio")
    List<HorarioDisponivel> findByProfissionalIdAndDataBetweenAndDisponivelTrue(@Param("profissionalId") Long profissionalId, 
                                                                               @Param("dataInicio") LocalDate dataInicio, 
                                                                               @Param("dataFim") LocalDate dataFim);
    
    /**
     * Busca todos os horários disponíveis em uma data específica
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.data = :data AND h.disponivel = true ORDER BY h.profissional.usuario.nome, h.horaInicio")
    List<HorarioDisponivel> findByDataAndDisponivelTrue(@Param("data") LocalDate data);
    
    /**
     * Busca horários disponíveis em um período
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.data BETWEEN :dataInicio AND :dataFim AND h.disponivel = true ORDER BY h.data, h.profissional.usuario.nome, h.horaInicio")
    List<HorarioDisponivel> findByDataBetweenAndDisponivelTrue(@Param("dataInicio") LocalDate dataInicio, 
                                                              @Param("dataFim") LocalDate dataFim);
    
    /**
     * Busca horários de um profissional a partir de uma data
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.data >= :dataInicio ORDER BY h.data, h.horaInicio")
    List<HorarioDisponivel> findByProfissionalIdAndDataGreaterThanEqual(@Param("profissionalId") Long profissionalId, 
                                                                        @Param("dataInicio") LocalDate dataInicio);
    
    /**
     * Busca horários disponíveis de um profissional a partir de uma data
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.data >= :dataInicio AND h.disponivel = true ORDER BY h.data, h.horaInicio")
    List<HorarioDisponivel> findByProfissionalIdAndDataGreaterThanEqualAndDisponivelTrue(@Param("profissionalId") Long profissionalId, 
                                                                                        @Param("dataInicio") LocalDate dataInicio);
    
    /**
     * Conta horários disponíveis de um profissional em uma data
     */
    @Query("SELECT COUNT(h) FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.data = :data AND h.disponivel = true")
    long countByProfissionalIdAndDataAndDisponivelTrue(@Param("profissionalId") Long profissionalId, 
                                                       @Param("data") LocalDate data);
    
    /**
     * Remove horários antigos (anteriores à data atual)
     */
    @Query("DELETE FROM HorarioDisponivel h WHERE h.data < :dataAtual")
    void deleteByDataBefore(@Param("dataAtual") LocalDate dataAtual);
    
    /**
     * Busca horários conflitantes (mesmo profissional, data e horário)
     */
    @Query("SELECT h FROM HorarioDisponivel h WHERE h.profissional.id = :profissionalId AND h.data = :data AND ((h.horaInicio <= :horaInicio AND h.horaFim > :horaInicio) OR (h.horaInicio < :horaFim AND h.horaFim >= :horaFim) OR (h.horaInicio >= :horaInicio AND h.horaFim <= :horaFim))")
    List<HorarioDisponivel> findHorariosConflitantes(@Param("profissionalId") Long profissionalId, 
                                                     @Param("data") LocalDate data, 
                                                     @Param("horaInicio") LocalTime horaInicio, 
                                                     @Param("horaFim") LocalTime horaFim);
}

