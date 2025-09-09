package com.salaobeleza.controller;

import com.salaobeleza.model.Servico;
import com.salaobeleza.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public String listarServicos(Model model) {
        List<Servico> servicos = servicoService.listarTodos();
        model.addAttribute("servicos", servicos);
        return "servico/lista";
    }
}

