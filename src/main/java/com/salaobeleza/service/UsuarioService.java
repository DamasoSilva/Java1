package com.salaobeleza.service;

import com.salaobeleza.model.TipoUsuario;
import com.salaobeleza.model.Usuario;
import com.salaobeleza.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Salva um novo usuário
     */
    public Usuario salvar(Usuario usuario) {
        // Criptografa a senha antes de salvar
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }
    
    /**
     * Atualiza um usuário existente
     */
    public Usuario atualizar(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
        if (usuarioExistente.isPresent()) {
            Usuario user = usuarioExistente.get();
            user.setNome(usuario.getNome());
            user.setSobrenome(usuario.getSobrenome());
            user.setTelefone(usuario.getTelefone());
            user.setTipoUsuario(usuario.getTipoUsuario());
            
            // Só atualiza a senha se uma nova foi fornecida
            if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                user.setSenha(passwordEncoder.encode(usuario.getSenha()));
            }
            
            return usuarioRepository.save(user);
        }
        throw new RuntimeException("Usuário não encontrado com ID: " + usuario.getId());
    }
    
    /**
     * Busca usuário por ID
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    /**
     * Busca usuário por email
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    /**
     * Lista todos os usuários
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    /**
     * Lista usuários por tipo
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarPorTipo(TipoUsuario tipo) {
        return usuarioRepository.findByTipoUsuario(tipo);
    }
    
    /**
     * Busca usuários por nome
     */
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    /**
     * Verifica se email já existe
     */
    @Transactional(readOnly = true)
    public boolean emailJaExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    /**
     * Verifica se email já existe para outro usuário
     */
    @Transactional(readOnly = true)
    public boolean emailJaExisteParaOutroUsuario(String email, Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.isPresent() && !usuario.get().getId().equals(usuarioId);
    }
    
    /**
     * Autentica usuário
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && passwordEncoder.matches(senha, usuario.get().getSenha())) {
            return usuario;
        }
        return Optional.empty();
    }
    
    /**
     * Altera senha do usuário
     */
    public boolean alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            if (passwordEncoder.matches(senhaAtual, user.getSenha())) {
                user.setSenha(passwordEncoder.encode(novaSenha));
                usuarioRepository.save(user);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Remove usuário
     */
    public void remover(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
    }
    
    /**
     * Conta usuários por tipo
     */
    @Transactional(readOnly = true)
    public long contarPorTipo(TipoUsuario tipo) {
        return usuarioRepository.countByTipoUsuario(tipo);
    }
    
    /**
     * Valida dados do usuário
     */
    public void validarUsuario(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }
        
        if (usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
        
        if (usuario.getTipoUsuario() == null) {
            throw new IllegalArgumentException("Tipo de usuário é obrigatório");
        }
        
        // Verifica se email já existe (para novos usuários)
        if (usuario.getId() == null && emailJaExiste(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        
        // Verifica se email já existe para outro usuário (para atualizações)
        if (usuario.getId() != null && emailJaExisteParaOutroUsuario(usuario.getEmail(), usuario.getId())) {
            throw new IllegalArgumentException("Email já está em uso por outro usuário");
        }
    }
}

