package com.ues.edu.controladores;

import com.ues.edu.modelo.Reserva;
import com.ues.edu.servicios.ICruceroProgramadoService;
import com.ues.edu.servicios.IPasajeroService;
import com.ues.edu.servicios.IReservaService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    @Autowired
    private ICruceroProgramadoService cruceroService;

    @Autowired
    private IPasajeroService pasajeroService;

    // ==========================================================
    // LISTA
    // ==========================================================
    @GetMapping
    public String listar(Model model,
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        List<Reserva> reservas = reservaService.listar();
        model.addAttribute("reservas", reservas);
        model.addAttribute("titulo", "Reservas");
        
        // Pasar parámetros a la vista para SweetAlert
        if (ok != null) {
            model.addAttribute("ok", ok);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        
        return "reservas/lista";
    }

    // ==========================================================
    // FORM NUEVO
    // ==========================================================
    @GetMapping("/nuevo")
    public String nuevo(Model model,
                       @RequestParam(value = "error", required = false) String error) {
        Reserva reserva = new Reserva();
        cargarCombos(model);
        model.addAttribute("reserva", reserva);
        model.addAttribute("modo", "nuevo");
        
        // Pasar parámetros a la vista para SweetAlert
        if (error != null) {
            model.addAttribute("error", error);
        }
        
        return "reservas/form";
    }

    // ==========================================================
    // GUARDAR NUEVO
    // ==========================================================
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("reserva") Reserva reserva,
                          BindingResult result,
                          Model model,
                          RedirectAttributes ra) {

        if (result.hasErrors()) {
            cargarCombos(model);
            model.addAttribute("modo", "nuevo");
            return "reservas/form";
        }

        try {
            reservaService.guardar(reserva);
            return "redirect:/reservas?ok=Reserva registrada correctamente";
        } catch (Exception e) {
            return "redirect:/reservas/nuevo?error=Error al registrar la reserva: " + e.getMessage();
        }
    }

    // ==========================================================
    // FORM EDITAR
    // ==========================================================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id,
                         Model model,
                         @RequestParam(value = "error", required = false) String error) {

        Reserva reserva = reservaService.leerPorId(id);

        if (reserva == null) {
            return "redirect:/reservas?error=No se encontró la reserva con ID: " + id;
        }

        cargarCombos(model);
        model.addAttribute("reserva", reserva);
        model.addAttribute("modo", "editar");
        
        // Pasar parámetros a la vista para SweetAlert
        if (error != null) {
            model.addAttribute("error", error);
        }

        return "reservas/form";
    }

    // ==========================================================
    // ACTUALIZAR
    // ==========================================================
    @PostMapping("/editar/{id}")
    public String actualizar(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("reserva") Reserva reserva,
                             BindingResult result,
                             Model model,
                             RedirectAttributes ra) {

        if (result.hasErrors()) {
            cargarCombos(model);
            model.addAttribute("modo", "editar");
            return "reservas/form";
        }

        try {
            reserva.setIdReserva(id);
            reservaService.guardar(reserva);
            return "redirect:/reservas?ok=Reserva actualizada correctamente";
        } catch (Exception e) {
            return "redirect:/reservas/editar/" + id + "?error=Error al actualizar la reserva: " + e.getMessage();
        }
    }

    // ==========================================================
    // ELIMINAR
    // ==========================================================
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id,
                           RedirectAttributes ra) {

        try {
            boolean eliminado = reservaService.eliminar(id);

            if (eliminado) {
                return "redirect:/reservas?ok=Reserva eliminada correctamente";
            } else {
                return "redirect:/reservas?error=No se pudo eliminar la reserva. Puede tener dependencias";
            }
        } catch (Exception e) {
            return "redirect:/reservas?error=Error al eliminar la reserva: " + e.getMessage();
        }
    }

    // ==========================================================
    // CARGA DE COMBOS
    // ==========================================================
    private void cargarCombos(Model model) {
        model.addAttribute("cruceros", cruceroService.listar());
        model.addAttribute("pasajeros", pasajeroService.listar());
    }
}