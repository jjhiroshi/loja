package com.lojaonline.loja.controllers;

import java.util.Map;


import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/chatComAssistente")
public class ChatAiController {
    
    private final OpenAiChatModel chatModel;

    @Autowired
    public ChatAiController(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }
    

    @GetMapping("/iniciarChat")
    public ModelAndView iniciarChat (@RequestParam(value = "message", defaultValue = "Quais os 10 melhores jogos de tabuleiro de 2023?")String message){
        ModelAndView view = new ModelAndView("/inteligencia/conversando");

        String mensagem = chatModel.call(message);

        view.addObject("mensagem", message);
        return view;
    }


    /*
    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Quais os 10 melhores jogos de tabuleiro de 2023?") String message) {
        return Map.of("generation", chatModel.call(message));
    }
    
    @GetMapping("/ai/generateStream")
	public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }
     */
}
