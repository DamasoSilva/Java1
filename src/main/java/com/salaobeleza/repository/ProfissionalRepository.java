package com.salaobeleza.repository;

import com.salaobeleza.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    
    /**
     * Busca profissional por email do usuário
     */
    @Query("SELECT p FROM Profissional p WHERE p.usuario.email = :email")
    Optional<Profissional> findByUsuarioEmail(@Param("email") String email);
    
    /**
     * Busca profissional por ID do usuário
     */
    Optional<Profissional> findByUsuarioId(Long usuarioId);
    
    /**
     * Busca profissionais por especialidade (case insensitive)
     */
    @Query("SELECT p FROM Profissional p WHERE LOWER(p.especialidade) LIKE LOWER(CONCAT('%', :especialidade, '%'))")
    List<Profissional> findByEspecialidadeContainingIgnoreCase(@Param("especialidade") String especialidade);
    
    /**
     * Busca profissionais por nome (case insensitive)
     */
    @Query("SELECT p FROM Profissional p WHERE LOWER(p.usuario.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Profissional> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca profissionais contratados em um período
     */
    @Query("SELECT p FROM Profissional p WHERE p.dataContratacao BETWEEN :dataInicio AND :dataFim ORDER BY p.dataContratacao DESC")
    List<Profissional> findByDataContratacaoBetween(@Param("dataInicio") LocalDate dataInicio, 
                                                   @Param("dataFim") LocalDate dataFim);
    
    /**
     * Lista profissionais que oferecem um serviço específico
     */
    @Query("SELECT p FROM Profissional p JOIN p.servicos s WHERE s.id = :servicoId ORDER BY p.usuario.nome")
    List<Profissional> findByServicoId(@Param("servicoId") Long servicoId);
    
    /**
     * Lista profissionais que oferecem um serviço específico por nome
     */
    @Query("SELECT p FROM Profissional p JOIN p.servicos s WHERE LOWER(s.nome) = LOWER(:nomeServico) ORDER BY p.usuario.nome")
    List<Profissional> findByServicoNome(@Param("nomeServico") String nomeServico);
    
    /**
     * Lista profissionais disponíveis em uma data específica
     */
    @Query("SELECT DISTINCT p FROM Profissional p JOIN p.horariosDisponiveis h WHERE h.data = :data AND h.disponivel = true ORDER BY p.usuario.nome")
    List<Profissional> findDisponiveis(@Param("data") LocalDate data);
    
    /**
     * Lista profissionais com mais agendamentos
     */
    @Query("SELECT p FROM Profissional p LEFT JOIN p.agendamentos a GROUP BY p ORDER BY COUNT(a) DESC")
    List<Profissional> findProfissionaisMaisAtivos();
    
    /**
     * Lista profissionais que têm horários disponíveis
     */
    @Query("SELECT DISTINCT p FROM Profissional p JOIN p.horariosDisponiveis h WHERE h.disponivel = true ORDER BY p.usuario.nome")
    List<Profissional> findComHorariosDisponiveis();
    
    /**
     * Conta total de profissionais
     */
    @Query("SELECT COUNT(p) FROM Profissional p")
    long countTotalProfissionais();
    
    /**
     * Lista todos os profissionais ordenados por nome
     */
    @Query("SELECT p FROM Profissional p ORDER BY p.usuario.nome")
    List<Profissional> findAllOrderByNome();
}