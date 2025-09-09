package com.salaobeleza.service;

import com.salaobeleza.model.Servico;
import com.salaobeleza.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServicoService {
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    /**
     * Salva um novo serviço
     */
    public Servico salvar(Servico servico) {
        validarServico(servico);
        return servicoRepository.save(servico);
    }
    
    /**
     * Atualiza um serviço existente
     */
    public Servico atualizar(Servico servico) {
        Optional<Servico> servicoExistente = servicoRepository.findById(servico.getId());
        if (servicoExistente.isPresent()) {
            validarServico(servico);
            
            Servico servicoAtual = servicoExistente.get();
            servicoAtual.setNome(servico.getNome());
            servicoAtual.setDescricao(servico.getDescricao());
            servicoAtual.setPreco(servico.getPreco());
            servicoAtual.setDuracaoMinutos(servico.getDuracaoMinutos());
            
            return servicoRepository.save(servicoAtual);
        }
        throw new RuntimeException("Serviço não encontrado com ID: " + servico.getId());
    }
    
    /**
     * Busca serviço por ID
     */
    @Transactional(readOnly = true)
    public Optional<Servico> buscarPorId(Long id) {
        return servicoRepository.findById(id);
    }
    
    /**
     * Busca serviço por nome
     */
    /**@Transactional(readOnly = true)
    *public Optional<Servico> buscarPorNome(String nome) {
    *    return servicoRepository.findByNome(nome);
    }
    */

    /**
     * Lista todos os serviços
     */
    @Transactional(readOnly = true)
    public List<Servico> listarTodos() {
        return servicoRepository.findAllByOrderByNomeAsc();
    }
    
    /**
     * Lista serviços ordenados por preço
     */
    @Transactional(readOnly = true)
    public List<Servico> listarPorPreco() {
        return servicoRepository.findAllByOrderByPrecoAsc();
    }
    
    /**
     * Lista serviços ordenados por duração
     */
    @Transactional(readOnly = true)
    public List<Servico> listarPorDuracao() {
        return servicoRepository.findAllByOrderByDuracaoMinutosAsc();
    }
    
    /**
     * Busca serviços por nome (case insensitive)
     */
    @Transactional(readOnly = true)
    public List<Servico> buscarPorNome(String nome) {
        return servicoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    /**
     * Busca serviços por faixa de preço
     */
    @Transactional(readOnly = true)
    public List<Servico> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        return servicoRepository.findByPrecoRange(precoMin, precoMax);
    }
    
    /**
     * Busca serviços com duração máxima
     */
    @Transactional(readOnly = true)
    public List<Servico> buscarPorDuracaoMaxima(Integer duracaoMaxima) {
        return servicoRepository.findByDuracaoMaxima(duracaoMaxima);
    }
    
    /**
     * Busca serviços com preço menor que o valor informado
     */
    @Transactional(readOnly = true)
    public List<Servico> buscarPorPrecoMenorQue(BigDecimal preco) {
        return servicoRepository.findByPrecoLessThanOrderByPrecoAsc(preco);
    }
    
    /**
     * Lista serviços que um profissional oferece
     */
    @Transactional(readOnly = true)
    public List<Servico> listarPorProfissional(Long profissionalId) {
        return servicoRepository.findByProfissionalId(profissionalId);
    }
    
    /**
     * Lista serviços mais populares
     */
    @Transactional(readOnly = true)
    public List<Servico> listarMaisPopulares() {
        return servicoRepository.findServicosMaisPopulares();
    }
    
    /**
     * Conta total de serviços
     */
    @Transactional(readOnly = true)
    public long contarTotal() {
        return servicoRepository.countTotalServicos();
    }
    
    /**
     * Remove serviço
     */
    public void remover(Long id) {
        Optional<Servico> servico = servicoRepository.findById(id);
        if (servico.isPresent()) {
            // Verifica se o serviço tem agendamentos
            if (!servico.get().getAgendamentos().isEmpty()) {
                throw new RuntimeException("Não é possível remover serviço que possui agendamentos");
            }
            servicoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Serviço não encontrado com ID: " + id);
        }
    }
    
    /**
     * Verifica se nome do serviço já existe
     */
    @Transactional(readOnly = true)
    public boolean nomeJaExiste(String nome) {
        return servicoRepository.existsByNome(nome);
    }
    
    /**
     * Verifica se nome já existe para outro serviço
     */
    @Transactional(readOnly = true)
    public boolean nomeJaExisteParaOutroServico(String nome, Long servicoId) {
        Optional<Servico> servico = servicoRepository.findByNome(nome);
        return servico.isPresent() && !servico.get().getId().equals(servicoId);
    }
    
    /**
     * Valida dados do serviço
     */
    private void validarServico(Servico servico) {
        if (servico.getNome() == null || servico.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do serviço é obrigatório");
        }
        
        if (servico.getPreco() == null || servico.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        
        if (servico.getDuracaoMinutos() == null || servico.getDuracaoMinutos() <= 0) {
            throw new IllegalArgumentException("Duração deve ser maior que zero");
        }
        
        // Verifica se nome já existe (para novos serviços)
        if (servico.getId() == null && nomeJaExiste(servico.getNome())) {
            throw new IllegalArgumentException("Já existe um serviço com este nome");
        }
        
        // Verifica se nome já existe para outro serviço (para atualizações)
        if (servico.getId() != null && nomeJaExisteParaOutroServico(servico.getNome(), servico.getId())) {
            throw new IllegalArgumentException("Já existe outro serviço com este nome");
        }
    }
    
    /**
     * Busca serviços por duração específica
     */
    @Transactional(readOnly = true)
    public List<Servico> buscarPorDuracao(Integer duracaoMinutos) {
        return servicoRepository.findByDuracaoMinutos(duracaoMinutos);
    }
    
    /**
     * Calcula preço total de uma lista de serviços
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularPrecoTotal(List<Long> servicoIds) {
        BigDecimal total = BigDecimal.ZERO;
        for (Long id : servicoIds) {
            Optional<Servico> servico = servicoRepository.findById(id);
            if (servico.isPresent()) {
                total = total.add(servico.get().getPreco());
            }
        }
        return total;
    }
    
    /**
     * Calcula duração total de uma lista de serviços
     */
    @Transactional(readOnly = true)
    public Integer calcularDuracaoTotal(List<Long> servicoIds) {
        Integer total = 0;
        for (Long id : servicoIds) {
            Optional<Servico> servico = servicoRepository.findById(id);
            if (servico.isPresent()) {
                total += servico.get().getDuracaoMinutos();
            }
        }
        return total;
    }
}

