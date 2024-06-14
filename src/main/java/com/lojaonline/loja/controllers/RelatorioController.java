package com.lojaonline.loja.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

@GetMapping("/vendasSemestral")
public ModelAndView exibirRelatorio (){
    // Carregar a página do relatório
    ModelAndView view = new ModelAndView("/relatorios/relatorioVendas");

    return view;
}

}
