package com.salaobeleza.repository;

import com.salaobeleza.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca cliente por email do usuário
     */
    @Query("SELECT c FROM Cliente c WHERE c.usuario.email = :email")
    Optional<Cliente> findByUsuarioEmail(@Param("email") String email);
    
    /**
     * Busca cliente por ID do usuário
     */
    Optional<Cliente> findByUsuarioId(Long usuarioId);
    
    /**
     * Busca clientes por nome (case insensitive)
     */
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.usuario.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca clientes cadastrados em um período
     */
    @Query("SELECT c FROM Cliente c WHERE c.dataCadastro BETWEEN :dataInicio AND :dataFim ORDER BY c.dataCadastro DESC")
    List<Cliente> findByDataCadastroBetween(@Param("dataInicio") LocalDateTime dataInicio, 
                                           @Param("dataFim") LocalDateTime dataFim);
    
    /**
     * Busca clientes cadastrados hoje
     */
    @Query("SELECT c FROM Cliente c WHERE DATE(c.dataCadastro) = CURRENT_DATE ORDER BY c.dataCadastro DESC")
    List<Cliente> findCadastradosHoje();
    
    /**
     * Busca clientes com agendamentos
     */
    @Query("SELECT DISTINCT c FROM Cliente c JOIN c.agendamentos a WHERE a.id IS NOT NULL")
    List<Cliente> findClientesComAgendamentos();
    
    /**
     * Conta total de clientes
     */
    @Query("SELECT COUNT(c) FROM Cliente c")
    long countTotalClientes();
    
    /**
     * Busca clientes ordenados por data de cadastro (mais recentes primeiro)
     */
    List<Cliente> findAllByOrderByDataCadastroDesc();
}

