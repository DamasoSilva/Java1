package com.salaobeleza.controller;

import com.salaobeleza.model.Agendamento;
import com.salaobeleza.model.Cliente;
import com.salaobeleza.model.Usuario;
import com.salaobeleza.service.AgendamentoService;
import com.salaobeleza.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteDashboardController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        Optional<Cliente> clienteOptional = clienteService.buscarPorUsuarioId(usuarioLogado.getId());
        if (clienteOptional.isEmpty()) {
            // Tratar erro: cliente não encontrado para o usuário logado
            return "redirect:/logout";
        }
        Cliente cliente = clienteOptional.get();
        model.addAttribute("cliente", cliente);

        List<Agendamento> proximosAgendamentos = agendamentoService.listarProximosAgendamentosCliente(cliente.getId());
        model.addAttribute("proximosAgendamentos", proximosAgendamentos);

        return "cliente/dashboard";
    }
}

