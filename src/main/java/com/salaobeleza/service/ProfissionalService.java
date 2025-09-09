package com.salaobeleza.service;

import com.salaobeleza.model.Profissional;
import com.salaobeleza.model.Servico;
import com.salaobeleza.model.TipoUsuario;
import com.salaobeleza.model.Usuario;
import com.salaobeleza.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfissionalService {
    
    @Autowired
    private ProfissionalRepository profissionalRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ServicoService servicoService;
    
    /**
     * Cadastra um novo profissional
     */
    public Profissional cadastrar(Profissional profissional) {
        validarProfissional(profissional);
        
        // Garante que o usuário seja do tipo PROFISSIONAL
        profissional.getUsuario().setTipoUsuario(TipoUsuario.PROFISSIONAL);
        
        // Salva o usuário primeiro
        Usuario usuarioSalvo = usuarioService.salvar(profissional.getUsuario());
        profissional.setUsuario(usuarioSalvo);
        
        // Define data de contratação se não foi definida
        if (profissional.getDataContratacao() == null) {
            profissional.setDataContratacao(LocalDate.now());
        }
        
        return profissionalRepository.save(profissional);
    }
    
    /**
     * Atualiza dados do profissional
     */
    public Profissional atualizar(Profissional profissional) {
        Optional<Profissional> profissionalExistente = profissionalRepository.findById(profissional.getId());
        if (profissionalExistente.isPresent()) {
            Profissional profissionalAtual = profissionalExistente.get();
            
            // Atualiza dados do usuário
            Usuario usuario = profissionalAtual.getUsuario();
            usuario.setNome(profissional.getUsuario().getNome());
            usuario.setSobrenome(profissional.getUsuario().getSobrenome());
            usuario.setTelefone(profissional.getUsuario().getTelefone());
            
            // Só atualiza senha se foi fornecida
            if (profissional.getUsuario().getSenha() != null && !profissional.getUsuario().getSenha().isEmpty()) {
                usuario.setSenha(profissional.getUsuario().getSenha());
            }
            
            usuarioService.atualizar(usuario);
            
            profissionalAtual.setEspecialidade(profissional.getEspecialidade());
            profissionalAtual.setDataContratacao(profissional.getDataContratacao());
            
            return profissionalRepository.save(profissionalAtual);
        }
        throw new RuntimeException("Profissional não encontrado com ID: " + profissional.getId());
    }
    
    /**
     * Busca profissional por ID
     */
    @Transactional(readOnly = true)
    public Optional<Profissional> buscarPorId(Long id) {
        return profissionalRepository.findById(id);
    }
    
    /**
     * Busca profissional por email
     */
    @Transactional(readOnly = true)
    public Optional<Profissional> buscarPorEmail(String email) {
        return profissionalRepository.findByUsuarioEmail(email);
    }
    
    /**
     * Busca profissional por ID do usuário
     */
    @Transactional(readOnly = true)
    public Optional<Profissional> buscarPorUsuarioId(Long usuarioId) {
        return profissionalRepository.findByUsuarioId(usuarioId);
    }
    
    /**
     * Lista todos os profissionais
     */
    @Transactional(readOnly = true)
    public List<Profissional> listarTodos() {
        return profissionalRepository.findAllOrderByNome();
    }
    
    /**
     * Busca profissionais por especialidade
     */
    @Transactional(readOnly = true)
    public List<Profissional> buscarPorEspecialidade(String especialidade) {
        return profissionalRepository.findByEspecialidadeContainingIgnoreCase(especialidade);
    }
    
    /**
     * Busca profissionais por nome
     */
    @Transactional(readOnly = true)
    public List<Profissional> buscarPorNome(String nome) {
        return profissionalRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    /**
     * Lista profissionais contratados em um período
     */
    @Transactional(readOnly = true)
    public List<Profissional> listarPorPeriodoContratacao(LocalDate dataInicio, LocalDate dataFim) {
        return profissionalRepository.findByDataContratacaoBetween(dataInicio, dataFim);
    }
    
    /**
     * Lista profissionais que oferecem um serviço específico
     */
    @Transactional(readOnly = true)
    public List<Profissional> listarPorServico(Long servicoId) {
        return profissionalRepository.findByServicoId(servicoId);
    }
    
    /**
     * Lista profissionais que oferecem um serviço específico por nome
     */
    @Transactional(readOnly = true)
    public List<Profissional> listarPorServicoNome(String nomeServico) {
        return profissionalRepository.findByServicoNome(nomeServico);
    }
    
    /**
     * Lista profissionais disponíveis em uma data específica
     */
    @Transactional(readOnly = true)
    public List<Profissional> listarDisponiveis(LocalDate data) {
        return profissionalRepository.findDisponiveis(data);
    }
    
    /**
     * Lista profissionais com mais agendamentos
     */
    @Transactional(readOnly = true)
    public List<Profissional> listarMaisAtivos() {
        return profissionalRepository.findProfissionaisMaisAtivos();
    }
    
    /**
     * Lista profissionais que têm horários disponíveis
     */
    @Transactional(readOnly = true)
    public List<Profissional> listarComHorariosDisponiveis() {
        return profissionalRepository.findComHorariosDisponiveis();
    }
    
    /**
     * Conta total de profissionais
     */
    @Transactional(readOnly = true)
    public long contarTotal() {
        return profissionalRepository.countTotalProfissionais();
    }
    
    /**
     * Remove profissional
     */
    public void remover(Long id) {
        Optional<Profissional> profissional = profissionalRepository.findById(id);
        if (profissional.isPresent()) {
            // Verifica se o profissional tem agendamentos
            if (!profissional.get().getAgendamentos().isEmpty()) {
                throw new RuntimeException("Não é possível remover profissional que possui agendamentos");
            }
            profissionalRepository.deleteById(id);
        } else {
            throw new RuntimeException("Profissional não encontrado com ID: " + id);
        }
    }
    
    /**
     * Associa um serviço a um profissional
     */
    public Profissional adicionarServico(Long profissionalId, Long servicoId) {
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        Servico servico = servicoService.buscarPorId(servicoId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        
        if (!profissional.getServicos().contains(servico)) {
            profissional.getServicos().add(servico);
            servico.getProfissionais().add(profissional);
        }
        return profissionalRepository.save(profissional);
    }
    
    /**
     * Remove a associação de um serviço a um profissional
     */
    public Profissional removerServico(Long profissionalId, Long servicoId) {
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        Servico servico = servicoService.buscarPorId(servicoId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        
        if (profissional.getServicos().contains(servico)) {
            profissional.getServicos().remove(servico);
            servico.getProfissionais().remove(profissional);
        }
        return profissionalRepository.save(profissional);
    }
    
    /**
     * Valida dados do profissional
     */
    private void validarProfissional(Profissional profissional) {
        if (profissional.getUsuario() == null) {
            throw new IllegalArgumentException("Dados do usuário são obrigatórios");
        }
        
        // Valida dados do usuário
        usuarioService.validarUsuario(profissional.getUsuario());
    }
    
    /**
     * Autentica profissional
     */
    @Transactional(readOnly = true)
    public Optional<Profissional> autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioService.autenticar(email, senha);
        if (usuario.isPresent() && usuario.get().getTipoUsuario() == TipoUsuario.PROFISSIONAL) {
            return buscarPorUsuarioId(usuario.get().getId());
        }
        return Optional.empty();
    }
    
    /**
     * Altera senha do profissional
     */
    public boolean alterarSenha(Long profissionalId, String senhaAtual, String novaSenha) {
        Optional<Profissional> profissional = profissionalRepository.findById(profissionalId);
        if (profissional.isPresent()) {
            return usuarioService.alterarSenha(profissional.get().getUsuario().getId(), senhaAtual, novaSenha);
        }
        return false;
    }
}

