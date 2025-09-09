package com.salaobeleza.controller;

import com.salaobeleza.model.Usuario;
import com.salaobeleza.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "home/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String senha, 
                               HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuario = usuarioService.autenticar(email, senha);

        if (usuario.isPresent()) {
            session.setAttribute("usuarioLogado", usuario.get());
            redirectAttributes.addFlashAttribute("successMessage", "Login realizado com sucesso!");
            
            switch (usuario.get().getTipoUsuario()) {
                case CLIENTE:
                    return "redirect:/cliente/dashboard";
                case PROFISSIONAL:
                    return "redirect:/profissional/dashboard";
                case ADMIN:
                    return "redirect:/admin/dashboard";
                default:
                    return "redirect:/";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email ou senha inválidos.");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Você foi desconectado.");
        return "redirect:/";
    }
}

