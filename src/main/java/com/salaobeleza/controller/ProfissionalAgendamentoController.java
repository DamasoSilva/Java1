package com.salaobeleza.controller;

import com.salaobeleza.model.Agendamento;
import com.salaobeleza.model.Profissional;
import com.salaobeleza.model.StatusAgendamento;
import com.salaobeleza.model.Usuario;
import com.salaobeleza.service.AgendamentoService;
import com.salaobeleza.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profissional")
public class ProfissionalAgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private ProfissionalService profissionalService;

    @GetMapping("/meus-agendamentos")
    public String listarMeusAgendamentos(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || usuarioLogado.getTipoUsuario() != com.salaobeleza.model.TipoUsuario.PROFISSIONAL) {
            return "redirect:/login";
        }

        Optional<Profissional> profissionalOptional = profissionalService.buscarPorUsuarioId(usuarioLogado.getId());
        if (profissionalOptional.isEmpty()) {
            return "redirect:/logout";
        }
        Profissional profissional = profissionalOptional.get();

        List<Agendamento> agendamentos = agendamentoService.listarPorProfissional(profissional.getId());
        model.addAttribute("agendamentos", agendamentos);
        return "profissional/meus-agendamentos";
    }

    @PostMapping("/agendamento/confirmar/{id}")
    public String confirmarAgendamento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.confirmarAgendamento(id);
            redirectAttributes.addFlashAttribute("successMessage", "Agendamento confirmado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao confirmar agendamento: " + e.getMessage());
        }
        return "redirect:/profissional/meus-agendamentos";
    }

    @PostMapping("/agendamento/cancelar/{id}")
    public String cancelarAgendamento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.cancelarAgendamento(id);
            redirectAttributes.addFlashAttribute("successMessage", "Agendamento cancelado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cancelar agendamento: " + e.getMessage());
        }
        return "redirect:/profissional/meus-agendamentos";
    }

    @PostMapping("/agendamento/concluir/{id}")
    public String concluirAgendamento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.concluirAgendamento(id);
            redirectAttributes.addFlashAttribute("successMessage", "Agendamento concluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao concluir agendamento: " + e.getMessage());
        }
        return "redirect:/profissional/meus-agendamentos";
    }
}

