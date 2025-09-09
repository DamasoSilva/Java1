package com.salaobeleza.controller;

import com.salaobeleza.model.Profissional;
import com.salaobeleza.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profissionais")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @GetMapping
    public String listarProfissionais(Model model) {
        List<Profissional> profissionais = profissionalService.listarTodos();
        model.addAttribute("profissionais", profissionais);
        return "profissional/lista";
    }
}

