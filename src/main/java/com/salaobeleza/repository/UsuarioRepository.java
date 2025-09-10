package com.salaobeleza.repository;

import com.salaobeleza.model.Usuario;
import com.salaobeleza.model.TipoUsuario;
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
     * Verifica se já existe usuário com o email informado
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuários por nome (case insensitive)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Busca usuários por nome completo
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(CONCAT(u.nome, ' ', u.sobrenome)) LIKE LOWER(CONCAT('%', :nomeCompleto, '%'))")
    List<Usuario> findByNomeCompleto(@Param("nomeCompleto") String nomeCompleto);

    /**
     * Busca usuários por tipo (ADMIN, CLIENTE, PROFISSIONAL)
     */
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);

    /**
     * Conta usuários por tipo
     */
    long countByTipoUsuario(TipoUsuario tipoUsuario);

    /**
     * Conta total de usuários cadastrados
     */
    @Query("SELECT COUNT(u) FROM Usuario u")
    long countTotalUsuarios();

    /**
     * Busca usuários ordenados alfabeticamente
     */
    List<Usuario> findAllByOrderByNomeAsc();
}