package com.salaobeleza.repository;

import com.salaobeleza.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
    /**
     * Busca serviço por nome
     */
    Optional<Servico> findByNome(String nome);
    
    /**
     * Verifica se existe serviço com o nome informado
     */
    boolean existsByNome(String nome);
    
    /**
     * Busca serviços por nome (case insensitive)
     */
    @Query("SELECT s FROM Servico s WHERE LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Servico> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca serviços por faixa de preço
     */
    @Query("SELECT s FROM Servico s WHERE s.preco BETWEEN :precoMin AND :precoMax ORDER BY s.preco")
    List<Servico> findByPrecoRange(@Param("precoMin") BigDecimal precoMin, 
                                  @Param("precoMax") BigDecimal precoMax);
    
    /**
     * Busca serviços por duração máxima
     */
    @Query("SELECT s FROM Servico s WHERE s.duracaoMinutos <= :duracaoMaxima ORDER BY s.duracaoMinutos")
    List<Servico> findByDuracaoMaxima(@Param("duracaoMaxima") Integer duracaoMaxima);
    
    /**
     * Busca serviços ordenados por preço (menor para maior)
     */
    List<Servico> findAllByOrderByPrecoAsc();
    
    /**
     * Busca serviços ordenados por duração (menor para maior)
     */
    List<Servico> findAllByOrderByDuracaoMinutosAsc();
    
    /**
     * Busca serviços ordenados por nome
     */
    List<Servico> findAllByOrderByNomeAsc();
    
    /**
     * Busca serviços que um profissional específico oferece
     */
    @Query("SELECT s FROM Servico s JOIN s.profissionais p WHERE p.id = :profissionalId ORDER BY s.nome")
    List<Servico> findByProfissionalId(@Param("profissionalId") Long profissionalId);
    
    /**
     * Busca serviços mais populares (com mais agendamentos)
     */
    @Query("SELECT s FROM Servico s LEFT JOIN s.agendamentos a GROUP BY s ORDER BY COUNT(a) DESC")
    List<Servico> findServicosMaisPopulares();
    
    /**
     * Conta total de serviços
     */
    @Query("SELECT COUNT(s) FROM Servico s")
    long countTotalServicos();
    
    /**
     * Busca serviços com preço menor que o valor informado
     */
    List<Servico> findByPrecoLessThanOrderByPrecoAsc(BigDecimal preco);
    
    /**
     * Busca serviços com duração específica
     */
    List<Servico> findByDuracaoMinutos(Integer duracaoMinutos);
}