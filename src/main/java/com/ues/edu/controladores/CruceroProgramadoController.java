package com.ues.edu.controladores;

import com.ues.edu.modelo.CruceroProgramado;
import com.ues.edu.modelo.Barco;
import com.ues.edu.modelo.Puerto;
import com.ues.edu.servicios.ICruceroProgramadoService;
import com.ues.edu.servicios.IBarcoService;
import com.ues.edu.servicios.IPuertoService;
import com.ues.edu.servicios.impl.PuertoServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cruceros")
public class CruceroProgramadoController {

    @Autowired
    private ICruceroProgramadoService cruceroService;

    @Autowired
    private IBarcoService barcoService;

    @Autowired
    private IPuertoService puertoService;
    
    @GetMapping
    public String listar(Model model,
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        List<CruceroProgramado> cruceros = cruceroService.listar();
        model.addAttribute("cruceros", cruceros);
        if (ok != null) model.addAttribute("ok", ok);
        if (error != null) model.addAttribute("error", error);
        return "cruceros/lista";
    }
    
    @PostMapping
    public String guardar(@ModelAttribute CruceroProgramado crucero,
                         RedirectAttributes ra) {
        try {
            // Validar que puerto origen y destino sean diferentes
            if (crucero.getPuertoOrigen().getIdPuerto().equals(crucero.getPuertoDestino().getIdPuerto())) {
                ra.addFlashAttribute("error", "El puerto de origen y destino deben ser diferentes.");
                return "redirect:/cruceros/nuevo";
            }
            
            cruceroService.guardar(crucero);
            ra.addFlashAttribute("ok", "Crucero programado creado correctamente.");
            return "redirect:/cruceros";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Error al crear el crucero: " + ex.getMessage());
            return "redirect:/cruceros/nuevo";
        }
    }

    // Mostrar formulario para nuevo crucero
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        List<Barco> barcos = barcoService.listar();
        // ✅ USAR EL NUEVO MÉTODO para puertos con ciudad
        List<Puerto> puertos = ((PuertoServiceImpl) puertoService).listarConCiudad();
        
        model.addAttribute("crucero", new CruceroProgramado());
        model.addAttribute("barcos", barcos);
        model.addAttribute("puertos", puertos);
        return "cruceros/form";
    }

    // Mostrar formulario para editar crucero
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            CruceroProgramado crucero = cruceroService.leerPorId(id);
            List<Barco> barcos = barcoService.listar();
            // ✅ USAR EL NUEVO MÉTODO para puertos con ciudad
            List<Puerto> puertos = ((PuertoServiceImpl) puertoService).listarConCiudad();
            
            model.addAttribute("crucero", crucero);
            model.addAttribute("barcos", barcos);
            model.addAttribute("puertos", puertos);
            return "cruceros/form";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Crucero no encontrado: " + ex.getMessage());
            return "redirect:/cruceros";
        }
    }

    
    
    
    
    // Actualizar crucero existente
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                            @ModelAttribute CruceroProgramado crucero,
                            RedirectAttributes ra) {
        try {
            // Validar que puerto origen y destino sean diferentes
            if (crucero.getPuertoOrigen().getIdPuerto().equals(crucero.getPuertoDestino().getIdPuerto())) {
                ra.addFlashAttribute("error", "El puerto de origen y destino deben ser diferentes.");
                return "redirect:/cruceros/editar/" + id;
            }
            
            crucero.setIdCrucero(id);
            cruceroService.guardar(crucero);
            ra.addFlashAttribute("ok", "Crucero programado actualizado correctamente.");
            return "redirect:/cruceros";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Error al actualizar el crucero: " + ex.getMessage());
            return "redirect:/cruceros/editar/" + id;
        }
    }

    // Eliminar crucero
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            cruceroService.eliminar(id);
            ra.addFlashAttribute("ok", "Crucero programado eliminado correctamente.");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Error al eliminar el crucero: " + ex.getMessage());
        }
        return "redirect:/cruceros";
    }
}
