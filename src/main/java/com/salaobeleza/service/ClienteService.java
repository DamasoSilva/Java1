package com.salaobeleza.service;

import com.salaobeleza.model.Cliente;
import com.salaobeleza.model.TipoUsuario;
import com.salaobeleza.model.Usuario;
import com.salaobeleza.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Cadastra um novo cliente
     */
    public Cliente cadastrar(Cliente cliente) {
        validarCliente(cliente);
        
        // Garante que o usuário seja do tipo CLIENTE
        cliente.getUsuario().setTipoUsuario(TipoUsuario.CLIENTE);
        
        // Salva o usuário primeiro
        Usuario usuarioSalvo = usuarioService.salvar(cliente.getUsuario());
        cliente.setUsuario(usuarioSalvo);
        
        // Define data de cadastro se não foi definida
        if (cliente.getDataCadastro() == null) {
            cliente.setDataCadastro(LocalDateTime.now());
        }
        
        return clienteRepository.save(cliente);
    }
    
    /**
     * Atualiza dados do cliente
     */
    public Cliente atualizar(Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(cliente.getId());
        if (clienteExistente.isPresent()) {
            Cliente clienteAtual = clienteExistente.get();
            
            // Atualiza dados do usuário
            Usuario usuario = clienteAtual.getUsuario();
            usuario.setNome(cliente.getUsuario().getNome());
            usuario.setSobrenome(cliente.getUsuario().getSobrenome());
            usuario.setTelefone(cliente.getUsuario().getTelefone());
            
            // Só atualiza senha se foi fornecida
            if (cliente.getUsuario().getSenha() != null && !cliente.getUsuario().getSenha().isEmpty()) {
                usuario.setSenha(cliente.getUsuario().getSenha());
            }
            
            usuarioService.atualizar(usuario);
            
            return clienteRepository.save(clienteAtual);
        }
        throw new RuntimeException("Cliente não encontrado com ID: " + cliente.getId());
    }
    
    /**
     * Busca cliente por ID
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    /**
     * Busca cliente por email
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByUsuarioEmail(email);
    }
    
    /**
     * Busca cliente por ID do usuário
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorUsuarioId(Long usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId);
    }
    
    /**
     * Lista todos os clientes
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAllByOrderByDataCadastroDesc();
    }
    
    /**
     * Busca clientes por nome
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    /**
     * Lista clientes cadastrados hoje
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarCadastradosHoje() {
        return clienteRepository.findCadastradosHoje();
    }
    
    /**
     * Lista clientes em um período
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return clienteRepository.findByDataCadastroBetween(dataInicio, dataFim);
    }
    
    /**
     * Lista clientes que têm agendamentos
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarComAgendamentos() {
        return clienteRepository.findClientesComAgendamentos();
    }
    
    /**
     * Conta total de clientes
     */
    @Transactional(readOnly = true)
    public long contarTotal() {
        return clienteRepository.countTotalClientes();
    }
    
    /**
     * Remove cliente
     */
    public void remover(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            clienteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cliente não encontrado com ID: " + id);
        }
    }
    
    /**
     * Valida dados do cliente
     */
    private void validarCliente(Cliente cliente) {
        if (cliente.getUsuario() == null) {
            throw new IllegalArgumentException("Dados do usuário são obrigatórios");
        }
        
        // Valida dados do usuário
        usuarioService.validarUsuario(cliente.getUsuario());
    }
    
    /**
     * Verifica se cliente existe por email
     */
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return clienteRepository.findByUsuarioEmail(email).isPresent();
    }
    
    /**
     * Autentica cliente
     */
    @Transactional(readOnly = true)
    public Optional<Cliente> autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioService.autenticar(email, senha);
        if (usuario.isPresent() && usuario.get().getTipoUsuario() == TipoUsuario.CLIENTE) {
            return buscarPorUsuarioId(usuario.get().getId());
        }
        return Optional.empty();
    }
    
    /**
     * Altera senha do cliente
     */
    public boolean alterarSenha(Long clienteId, String senhaAtual, String novaSenha) {
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        if (cliente.isPresent()) {
            return usuarioService.alterarSenha(cliente.get().getUsuario().getId(), senhaAtual, novaSenha);
        }
        return false;
    }
}

