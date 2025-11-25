package com.ues.edu.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ues.edu.servicios.IUsuarioService;


import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ues.edu.modelo.Usuario;

@Controller
public class DashboardController {

    @Autowired
    private IUsuarioService usuarioService;



    
    @GetMapping("/dashboard")
    public String mostrarDashboard(@AuthenticationPrincipal Usuario usuario, Model model) {
        // Usuario logueado
        model.addAttribute("usuario", usuario);

        // Totales
        long totalUsuarios = usuarioService.contarUsuarios();

        model.addAttribute("totalUsuarios", totalUsuarios);

        return "dashboard/dashboard";
    }
    
}
