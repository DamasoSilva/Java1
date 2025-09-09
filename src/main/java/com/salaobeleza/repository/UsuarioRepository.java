package com.salaobeleza.repository;

import com.salaobeleza.model.TipoUsuario;
import com.salaobeleza.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica se existe usuário com o email informado
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuários por tipo
     */
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    
    /**
     * Busca usuários por nome (case insensitive)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca usuários por nome completo (nome + sobrenome)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(CONCAT(u.nome, ' ', COALESCE(u.sobrenome, ''))) LIKE LOWER(CONCAT('%', :nomeCompleto, '%'))")
    List<Usuario> findByNomeCompletoContainingIgnoreCase(@Param("nomeCompleto") String nomeCompleto);
    
    /**
     * Busca usuários por telefone
     */
    Optional<Usuario> findByTelefone(String telefone);
    
    /**
     * Conta usuários por tipo
     */
    long countByTipoUsuario(TipoUsuario tipoUsuario);
}

