package com.ues.edu.controladores;

import com.ues.edu.modelo.Naviera;
import com.ues.edu.servicios.INavieraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/navieras")
public class NavieraController {

    @Autowired
    private INavieraService navieraService;

    // Listar navieras - muestra la vista HTML
    @GetMapping
    public String listar(Model model, 
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        List<Naviera> navieras = navieraService.listar();
        model.addAttribute("navieras", navieras);
        if (ok != null) model.addAttribute("ok", ok);
        if (error != null) model.addAttribute("error", error);
        return "navieras/lista"; // ← Devuelve la vista HTML
    }

    // Mostrar formulario para nueva naviera
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("naviera", new Naviera());
        return "navieras/form"; // ← Devuelve el formulario HTML
    }

    // Guardar nueva naviera
    @PostMapping
    public String guardar(@ModelAttribute Naviera naviera,
                         RedirectAttributes ra) {
        try {
            navieraService.guardar(naviera);
            ra.addFlashAttribute("ok", "Naviera creada correctamente.");
            return "redirect:/navieras";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Error al crear la naviera: " + ex.getMessage());
            return "redirect:/navieras/nuevo";
        }
    }

    // Mostrar formulario para editar naviera
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            Naviera naviera = navieraService.leerPorId(id);
            model.addAttribute("naviera", naviera);
            return "navieras/form"; // ← Mismo formulario para editar
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Naviera no encontrada: " + ex.getMessage());
            return "redirect:/navieras";
        }
    }

    // Actualizar naviera existente
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                            @ModelAttribute Naviera naviera,
                            RedirectAttributes ra) {
        try {
            naviera.setIdNaviera(id);
            navieraService.guardar(naviera);
            ra.addFlashAttribute("ok", "Naviera actualizada correctamente.");
            return "redirect:/navieras";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Error al actualizar la naviera: " + ex.getMessage());
            return "redirect:/navieras/editar/" + id;
        }
    }

    // Eliminar naviera
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            navieraService.eliminar(id);
            ra.addFlashAttribute("ok", "Naviera eliminada correctamente.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Error al eliminar la naviera: " + ex.getMessage());
        }
        return "redirect:/navieras";
    }
}