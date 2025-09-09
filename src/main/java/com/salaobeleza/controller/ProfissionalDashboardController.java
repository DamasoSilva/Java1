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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profissional")
public class ProfissionalDashboardController {

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || usuarioLogado.getTipoUsuario() != com.salaobeleza.model.TipoUsuario.PROFISSIONAL) {
            return "redirect:/login";
        }

        Optional<Profissional> profissionalOptional = profissionalService.buscarPorUsuarioId(usuarioLogado.getId());
        if (profissionalOptional.isEmpty()) {
            // Tratar erro: profissional não encontrado para o usuário logado
            return "redirect:/logout";
        }
        Profissional profissional = profissionalOptional.get();
        model.addAttribute("profissional", profissional);

        List<Agendamento> agendamentosHoje = agendamentoService.listarPorProfissionalEData(profissional.getId(), java.time.LocalDate.now());
        model.addAttribute("agendamentosHoje", agendamentosHoje);

        long totalAgendamentosPendentes = agendamentoService.contarPorProfissionalEStatus(profissional.getId(), StatusAgendamento.PENDENTE);
        model.addAttribute("totalAgendamentosPendentes", totalAgendamentosPendentes);

        long totalAgendamentosConfirmados = agendamentoService.contarPorProfissionalEStatus(profissional.getId(), StatusAgendamento.CONFIRMADO);
        model.addAttribute("totalAgendamentosConfirmados", totalAgendamentosConfirmados);

        return "profissional/dashboard";
    }
}

