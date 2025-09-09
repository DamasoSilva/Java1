package com.salaobeleza.controller;

import com.salaobeleza.model.Cliente;
import com.salaobeleza.model.TipoUsuario;
import com.salaobeleza.model.Usuario;
import com.salaobeleza.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("cliente", new Cliente(new Usuario()));
        return "home/register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("cliente") Cliente cliente, 
                                      BindingResult result, 
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "home/register";
        }

        try {
            // Garante que o tipo de usuário seja CLIENTE
            cliente.getUsuario().setTipoUsuario(TipoUsuario.CLIENTE);
            clienteService.cadastrar(cliente);
            redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }
}

