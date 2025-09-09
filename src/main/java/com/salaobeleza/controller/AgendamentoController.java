package com.salaobeleza.controller;

import com.salaobeleza.model.*;
import com.salaobeleza.service.*;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cliente")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProfissionalService profissionalService;
    @Autowired
    private ServicoService servicoService;
    @Autowired
    private HorarioDisponivelService horarioDisponivelService;

    @GetMapping("/agendar")
    public String showAgendamentoForm(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || usuarioLogado.getTipoUsuario() != TipoUsuario.CLIENTE) {
            return "redirect:/login";
        }

        model.addAttribute("agendamento", new Agendamento());
        model.addAttribute("servicos", servicoService.listarTodos());
        model.addAttribute("profissionais", profissionalService.listarTodos());
        return "cliente/agendar";
    }

    @PostMapping("/agendar")
    public String processAgendamento(@ModelAttribute("agendamento") Agendamento agendamento, 
                                     @RequestParam("servicoId") Long servicoId,
                                     @RequestParam("profissionalId") Long profissionalId,
                                     @RequestParam("dataAgendamentoStr") String dataAgendamentoStr,
                                     @RequestParam("horaAgendamentoStr") String horaAgendamentoStr,
                                     HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || usuarioLogado.getTipoUsuario() != TipoUsuario.CLIENTE) {
            return "redirect:/login";
        }

        try {
            Cliente cliente = clienteService.buscarPorUsuarioId(usuarioLogado.getId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
            Profissional profissional = profissionalService.buscarPorId(profissionalId)
                    .orElseThrow(() -> new RuntimeException("Profissional não encontrado."));
            Servico servico = servicoService.buscarPorId(servicoId)
                    .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));

            LocalDate dataAgendamento = LocalDate.parse(dataAgendamentoStr);
            LocalTime horaAgendamento = LocalTime.parse(horaAgendamentoStr);

            agendamento.setCliente(cliente);
            agendamento.setProfissional(profissional);
            agendamento.setServico(servico);
            agendamento.setDataAgendamento(dataAgendamento);
            agendamento.setHoraAgendamento(horaAgendamento);

            agendamentoService.agendar(agendamento);
            redirectAttributes.addFlashAttribute("successMessage", "Agendamento realizado com sucesso!");
            return "redirect:/cliente/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao agendar: " + e.getMessage());
            return "redirect:/cliente/agendar";
        }
    }

    @GetMapping("/meus-agendamentos")
    public String meusAgendamentos(Model model, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado == null || usuarioLogado.getTipoUsuario() != TipoUsuario.CLIENTE) {
            return "redirect:/login";
        }

        Cliente cliente = clienteService.buscarPorUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        List<Agendamento> agendamentos = agendamentoService.listarPorCliente(cliente.getId());
        model.addAttribute("agendamentos", agendamentos);
        return "cliente/meus-agendamentos";
    }

    @PostMapping("/agendamento/cancelar/{id}")
    public String cancelarAgendamento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.cancelarAgendamento(id);
            redirectAttributes.addFlashAttribute("successMessage", "Agendamento cancelado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cancelar agendamento: " + e.getMessage());
        }
        return "redirect:/cliente/meus-agendamentos";
    }

    // AJAX endpoint para buscar profissionais por serviço
    @GetMapping("/api/profissionais-por-servico")
    @ResponseBody
    public List<Profissional> getProfissionaisPorServico(@RequestParam("servicoId") Long servicoId) {
        return profissionalService.listarPorServico(servicoId);
    }

    // AJAX endpoint para buscar horários disponíveis de um profissional em uma data
    @GetMapping("/api/horarios-disponiveis")
    @ResponseBody
    public List<LocalTime> getHorariosDisponiveis(@RequestParam("profissionalId") Long profissionalId,
                                                  @RequestParam("data") String dataStr) {
        LocalDate data = LocalDate.parse(dataStr);
        return horarioDisponivelService.listarPorProfissionalEData(profissionalId, data)
                .stream()
                .filter(HorarioDisponivel::isDisponivel)
                .map(HorarioDisponivel::getHoraInicio)
                .collect(Collectors.toList());
    }
}

