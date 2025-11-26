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
    public String listar(Model model) {
        List<Reserva> reservas = reservaService.listar();
        model.addAttribute("reservas", reservas);
        model.addAttribute("titulo", "Reservas");
        return "reservas/lista";
    }

    // ==========================================================
    // FORM NUEVO
    // ==========================================================
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        Reserva reserva = new Reserva();
        cargarCombos(model);
        model.addAttribute("reserva", reserva);
        model.addAttribute("modo", "nuevo");
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

        reservaService.guardar(reserva);
        ra.addFlashAttribute("ok", "Reserva registrada correctamente.");
        return "redirect:/reservas";
    }

    // ==========================================================
    // FORM EDITAR
    // ==========================================================
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable("id") Integer id,
                         Model model,
                         RedirectAttributes ra) {

        Reserva reserva = reservaService.leerPorId(id);

        if (reserva == null) {
            ra.addFlashAttribute("error", "No se encontró la reserva con ID: " + id);
            return "redirect:/reservas";
        }

        cargarCombos(model);
        model.addAttribute("reserva", reserva);
        model.addAttribute("modo", "editar");

        return "reservas/form";
    }

    // ==========================================================
    // ACTUALIZAR
    // ==========================================================
    @PostMapping("/{id}/editar")
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

        reserva.setIdReserva(id);
        reservaService.guardar(reserva);

        ra.addFlashAttribute("ok", "Reserva actualizada correctamente.");
        return "redirect:/reservas";
    }

    // ==========================================================
    // ELIMINAR
    // ==========================================================
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable("id") Integer id,
                           RedirectAttributes ra) {

        boolean eliminado = reservaService.eliminar(id);

        ra.addFlashAttribute(eliminado ? "ok" : "error",
                eliminado ? "Reserva eliminada correctamente."
                          : "No se pudo eliminar la reserva. Puede tener dependencias.");

        return "redirect:/reservas";
    }

    // ==========================================================
    // CARGA DE COMBOS
    // ==========================================================
    private void cargarCombos(Model model) {
        model.addAttribute("cruceros", cruceroService.listar());
        model.addAttribute("pasajeros", pasajeroService.listar());
    }
}
