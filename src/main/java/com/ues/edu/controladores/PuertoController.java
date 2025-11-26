package com.ues.edu.controladores;

import com.ues.edu.modelo.Puerto;
import com.ues.edu.modelo.Ciudad;
import com.ues.edu.servicios.IPuertoService;
import com.ues.edu.servicios.ICiudadService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
@RequestMapping("/puertos")
public class PuertoController {

    @Autowired
    private IPuertoService puertoService;
    
    @Autowired
    private ICiudadService ciudadService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override public void setAsText(String text) {
                setValue(text == null ? null : text.trim());
            }
        });
    }

    // Listar
    @GetMapping
    public String listar(Model model, @RequestParam(value = "ok", required = false) String ok) {
        List<Puerto> puertos = puertoService.listar();
        model.addAttribute("puertos", puertos);
        if (ok != null) model.addAttribute("ok", ok);
        return "puertos/lista"; // ← CORREGIDO: era "puerto/lista"
    }

    // Nuevo
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        List<Ciudad> ciudades = ciudadService.listar();
        model.addAttribute("puerto", new Puerto());
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("esEdicion", false);
        return "puertos/form"; // ← CORREGIDO: era "puerto/form"
    }

    // Guardar nuevo - MÉTODO COMPLETAMENTE CORREGIDO
    @PostMapping
    public String guardar(@Valid @ModelAttribute("puerto") Puerto puerto,
                          BindingResult br,
                          Model model,
                          RedirectAttributes ra) {

        // Siempre cargar ciudades para el formulario
        List<Ciudad> ciudades = ciudadService.listar();
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("esEdicion", false);

        if (br.hasErrors()) {
            return "puertos/form"; // ← CORREGIDO: era "puerto/form"
        }

        try {
            // Validar que se seleccionó una ciudad
            if (puerto.getCiudad() == null || puerto.getCiudad().getIdCiudad() == null) {
                br.rejectValue("ciudad", "ciudad.requerida", "Debe seleccionar una ciudad.");
                return "puertos/form";
            }

            // Cargar la ciudad completa desde la base de datos
            Ciudad ciudad = ciudadService.leerPorId(puerto.getCiudad().getIdCiudad());
            puerto.setCiudad(ciudad);

            puertoService.guardar(puerto);
            ra.addFlashAttribute("ok", "Puerto guardado correctamente.");
            return "redirect:/puertos"; // ← CORREGIDO: era "redirect:/puerto"

        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Datos inválidos.";
            String lower = msg.toLowerCase();
            if (lower.contains("nombre")) {
                br.rejectValue("nombre", "nombre.duplicado", "Ya existe un puerto con ese nombre.");
            } else if (lower.contains("ciudad")) {
                br.rejectValue("ciudad", "ciudad.invalida", "La ciudad seleccionada no es válida.");
            } else {
                model.addAttribute("error", msg);
            }
            return "puertos/form"; // ← CORREGIDO: era "puerto/form"

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un puerto con ese nombre.");
            return "puertos/form"; // ← CORREGIDO: era "puerto/form"

        } catch (Exception ex) {
            model.addAttribute("error", "Ocurrió un error inesperado. Inténtalo nuevamente.");
            return "puertos/form"; // ← CORREGIDO: era "puerto/form"
        }
    }

    // Editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Puerto puerto = puertoService.leerPorId(id);
            List<Ciudad> ciudades = ciudadService.listar();
            model.addAttribute("puerto", puerto);
            model.addAttribute("ciudades", ciudades);
            model.addAttribute("esEdicion", true);
            return "puertos/form"; // ← Este ya estaba correcto
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/puertos";
        }
    }

    // Actualizar - MÉTODO CORREGIDO
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("puerto") Puerto puerto,
                             BindingResult br,
                             Model model,
                             RedirectAttributes ra) {
        try {
            Puerto existente = puertoService.leerPorId(id);
            puerto.setIdPuerto(id);

            // Siempre cargar ciudades para el formulario
            List<Ciudad> ciudades = ciudadService.listar();
            model.addAttribute("ciudades", ciudades);
            model.addAttribute("esEdicion", true);

            if (br.hasErrors()) {
                return "puertos/form";
            }

            // Validar y cargar la ciudad
            if (puerto.getCiudad() == null || puerto.getCiudad().getIdCiudad() == null) {
                br.rejectValue("ciudad", "ciudad.requerida", "Debe seleccionar una ciudad.");
                return "puertos/form";
            }

            Ciudad ciudad = ciudadService.leerPorId(puerto.getCiudad().getIdCiudad());
            puerto.setCiudad(ciudad);

            puertoService.guardar(puerto);
            ra.addFlashAttribute("ok", "Puerto actualizado correctamente.");
            return "redirect:/puertos";

        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Datos inválidos.";
            String lower = msg.toLowerCase();

            if (lower.contains("nombre")) {
                br.rejectValue("nombre", "nombre.duplicado", "Ya existe un puerto con ese nombre.");
            } else if (lower.contains("ciudad")) {
                br.rejectValue("ciudad", "ciudad.invalida", "La ciudad seleccionada no es válida.");
            } else {
                model.addAttribute("error", msg);
            }

            List<Ciudad> ciudades = ciudadService.listar();
            model.addAttribute("ciudades", ciudades);
            model.addAttribute("esEdicion", true);
            return "puertos/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un puerto con ese nombre.");
            List<Ciudad> ciudades = ciudadService.listar();
            model.addAttribute("ciudades", ciudades);
            model.addAttribute("esEdicion", true);
            return "puertos/form";

        } catch (Exception ex) {
            model.addAttribute("error", "Error al actualizar el puerto: " + ex.getMessage());
            List<Ciudad> ciudades = ciudadService.listar();
            model.addAttribute("ciudades", ciudades);
            model.addAttribute("esEdicion", true);
            return "puertos/form";
        }
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            puertoService.eliminar(id);
            ra.addFlashAttribute("ok", "Puerto eliminado correctamente");
            return "redirect:/puertos";

        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Error al eliminar el puerto: " + ex.getMessage());
            return "redirect:/puertos";
        }
    }
}