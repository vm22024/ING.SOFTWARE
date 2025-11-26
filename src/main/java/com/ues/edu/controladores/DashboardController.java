package com.ues.edu.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ues.edu.servicios.IUsuarioService;
import com.ues.edu.servicios.IPuertoService;
// Importa también los otros servicios que necesites para los contadores
import com.ues.edu.servicios.ICiudadService;
import com.ues.edu.servicios.ICruceroProgramadoService;
import com.ues.edu.servicios.IModeloBarcoService;
import com.ues.edu.servicios.INavieraService;
import com.ues.edu.servicios.IBarcoService;
import com.ues.edu.servicios.IPasajeroService;
import com.ues.edu.servicios.IReservaService;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ues.edu.modelo.Usuario;

@Controller
public class DashboardController {

    @Autowired
    private IUsuarioService usuarioService;
    
    @Autowired
    private IPuertoService puertoService;
    
    @Autowired
    private ICiudadService ciudadService;
    
    @Autowired
    private IModeloBarcoService modeloBarcoService;
    
    @Autowired
    private INavieraService navieraService;
    
    @Autowired
    private IBarcoService barcoService;
    
    @Autowired
    private ICruceroProgramadoService cruceroService;
    
    @Autowired
    private IPasajeroService pasajeroService;
    
    @Autowired
    private IReservaService reservaService;

    @GetMapping("/dashboard")
    public String mostrarDashboard(@AuthenticationPrincipal Usuario usuario, Model model) {
        // Usuario logueado
        model.addAttribute("usuario", usuario);

        // Totales - Agregar todos los servicios que necesites
        long totalUsuarios = usuarioService.contarUsuarios();
        long totalPuertos = puertoService.contarPuertos();
        long totalCiudades = ciudadService.contarCiudades();
        //long totalModelosBarco = modeloBarcoService.contarModelosBarco();
        //long totalNavieras = navieraService.contarNavieras();
        long totalBarcos = barcoService.contarBarcos();
        //long totalCruceros = cruceroService.contarCruceros();
        //long totalPasajeros = pasajeroService.contarPasajeros();
        long totalReservas = reservaService.contarReservas();

        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("totalPuertos", totalPuertos);
        model.addAttribute("totalCiudades", totalCiudades);
        //model.addAttribute("totalModelosBarco", totalModelosBarco);
        //model.addAttribute("totalNavieras", totalNavieras);
        model.addAttribute("totalBarcos", totalBarcos);
        //model.addAttribute("totalCruceros", totalCruceros);
        //model.addAttribute("totalPasajeros", totalPasajeros);
        model.addAttribute("totalReservas", totalReservas);

        return "dashboard/dashboard";
    }
}