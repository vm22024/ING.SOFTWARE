package com.ues.edu.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.servicios.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final IUsuarioService usuarioService;

    @Autowired
    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
