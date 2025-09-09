package com.salaobeleza.controller;

import com.salaobeleza.model.HorarioDisponivel;
import com.salaobeleza.model.Profissional;
import com.salaobeleza.model.Usuario;
import com.salaobeleza.service.HorarioDisponivelService;
import com.salaobeleza.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profissional")
public class ProfissionalHorarioController {

    @Autowired
    private HorarioDisponivelService horarioDisponivelService;

    @Autowired
    private ProfissionalService profissionalService;

    @GetMapping("/horarios")
    public String listarHorarios(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || usuarioLogado.getTipoUsuario() != com.salaobeleza.model.TipoUsuario.PROFISSIONAL) {
            return "redirect:/login";
        }

        Optional<Profissional> profissionalOptional = profissionalService.buscarPorUsuarioId(usuarioLogado.getId());
        if (profissionalOptional.isEmpty()) {
            return "redirect:/logout";
        }
        Profissional profissional = profissionalOptional.get();

        List<HorarioDisponivel> horarios = horarioDisponivelService.listarTodosPorProfissional(profissional.getId());
        model.addAttribute("horarios", horarios);
        model.addAttribute("profissional", profissional);
        model.addAttribute("novoHorario", new HorarioDisponivel());
        return "profissional/horarios";
    }

    @PostMapping("/horarios/adicionar")
    public String adicionarHorario(@ModelAttribute("novoHorario") HorarioDisponivel novoHorario,
                                   @RequestParam("dataStr") String dataStr,
                                   @RequestParam("horaInicioStr") String horaInicioStr,
                                   @RequestParam("horaFimStr") String horaFimStr,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || usuarioLogado.getTipoUsuario() != com.salaobeleza.model.TipoUsuario.PROFISSIONAL) {
            return "redirect:/login";
        }

        try {
            Profissional profissional = profissionalService.buscarPorUsuarioId(usuarioLogado.getId())
                    .orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

            novoHorario.setProfissional(profissional);
            novoHorario.setData(LocalDate.parse(dataStr));
            novoHorario.setHoraInicio(LocalTime.parse(horaInicioStr));
            novoHorario.setHoraFim(LocalTime.parse(horaFimStr));
            novoHorario.setDisponivel(true);

            horarioDisponivelService.salvar(novoHorario);
            redirectAttributes.addFlashAttribute("successMessage", "Horário adicionado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao adicionar horário: " + e.getMessage());
        }
        return "redirect:/profissional/horarios";
    }

    @PostMapping("/horarios/remover/{id}")
    public String removerHorario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            horarioDisponivelService.remover(id);
            redirectAttributes.addFlashAttribute("successMessage", "Horário removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover horário: " + e.getMessage());
        }
        return "redirect:/profissional/horarios";
    }

    @PostMapping("/horarios/marcar-indisponivel/{id}")
    public String marcarIndisponivel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            horarioDisponivelService.marcarComoIndisponivel(id);
            redirectAttributes.addFlashAttribute("successMessage", "Horário marcado como indisponível.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao marcar horário como indisponível: " + e.getMessage());
        }
        return "redirect:/profissional/horarios";
    }

    @PostMapping("/horarios/marcar-disponivel/{id}")
    public String marcarDisponivel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            horarioDisponivelService.marcarComoDisponivel(id);
            redirectAttributes.addFlashAttribute("successMessage", "Horário marcado como disponível.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao marcar horário como disponível: " + e.getMessage());
        }
        return "redirect:/profissional/horarios";
    }
}

