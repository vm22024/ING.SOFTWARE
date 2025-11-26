package com.ues.edu.controladores;

import com.ues.edu.modelo.Ciudad;
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
@RequestMapping("/ciudades")
public class CiudadController {

    @Autowired
    private ICiudadService ciudadService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override 
            public void setAsText(String text) {
                setValue(text == null ? null : text.trim());
            }
        });
    }

    // Listar todas las ciudades
    @GetMapping
    public String listar(Model model, 
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        List<Ciudad> ciudades = ciudadService.listar();
        model.addAttribute("ciudades", ciudades);
        if (ok != null) model.addAttribute("ok", ok);
        if (error != null) model.addAttribute("error", error);
        return "ciudad/lista";
    }

    // Mostrar formulario para nueva ciudad
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model,
                                   @RequestParam(value = "ok", required = false) String ok,
                                   @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("ciudad", new Ciudad());
        model.addAttribute("esEdicion", false);
        if (ok != null) model.addAttribute("ok", ok);
        if (error != null) model.addAttribute("error", error);
        return "ciudad/form";
    }

    // Guardar nueva ciudad
    @PostMapping
    public String guardar(@Valid @ModelAttribute("ciudad") Ciudad ciudad,
                          BindingResult br,
                          Model model,
                          RedirectAttributes ra) {

        if (br.hasErrors()) {
            model.addAttribute("esEdicion", false);
            return "ciudad/form";
        }

        try {
            ciudadService.guardar(ciudad);
            ra.addFlashAttribute("ok", "Ciudad guardada correctamente.");
            return "redirect:/ciudades?ok=Ciudad+guardada+correctamente";

        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Datos inválidos.";
            String lower = msg.toLowerCase();
            
            if (lower.contains("nombre")) {
                br.rejectValue("nombre", "nombre.duplicado", "Ya existe una ciudad con ese nombre.");
            } else if (lower.contains("país") || lower.contains("pais")) {
                br.rejectValue("pais", "pais.invalido", "El país ingresado no es válido.");
            } else {
                model.addAttribute("error", msg);
            }
            
            model.addAttribute("esEdicion", false);
            return "ciudad/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe una ciudad con ese nombre.");
            model.addAttribute("esEdicion", false);
            return "ciudad/form";

        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Ocurrió un error inesperado. Inténtalo nuevamente.");
            return "redirect:/ciudades/nuevo?error=Ocurrió+un+error+inesperado";
        }
    }

    // Mostrar formulario para editar ciudad
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, 
                        Model model, 
                        RedirectAttributes ra,
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        try {
            Ciudad ciudad = ciudadService.leerPorId(id);
            model.addAttribute("ciudad", ciudad);
            model.addAttribute("esEdicion", true);
            if (ok != null) model.addAttribute("ok", ok);
            if (error != null) model.addAttribute("error", error);
            return "ciudad/form";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Ciudad no encontrada: " + ex.getMessage());
            return "redirect:/ciudades?error=Ciudad+no+encontrada";
        }
    }

    // Actualizar ciudad existente
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("ciudad") Ciudad ciudad,
                             BindingResult br,
                             Model model,
                             RedirectAttributes ra) {
        try {
            Ciudad existente = ciudadService.leerPorId(id);
            ciudad.setIdCiudad(id);

            if (br.hasErrors()) {
                model.addAttribute("esEdicion", true);
                return "ciudad/form";
            }

            ciudadService.guardar(ciudad);
            return "redirect:/ciudades?ok=Ciudad+actualizada+correctamente";

        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Datos inválidos.";
            String lower = msg.toLowerCase();

            if (lower.contains("nombre")) {
                br.rejectValue("nombre", "nombre.duplicado", "Ya existe una ciudad con ese nombre.");
            } else if (lower.contains("país") || lower.contains("pais")) {
                br.rejectValue("pais", "pais.invalido", "El país ingresado no es válido.");
            } else {
                model.addAttribute("error", msg);
            }

            model.addAttribute("esEdicion", true);
            return "ciudad/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe una ciudad con ese nombre.");
            model.addAttribute("esEdicion", true);
            return "ciudad/form";

        } catch (Exception ex) {
            return "redirect:/ciudades/editar/" + id + "?error=Error+al+actualizar+la+ciudad";
        }
    }

    // Eliminar ciudad
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            ciudadService.eliminar(id);
            return "redirect:/ciudades?ok=Ciudad+eliminada+correctamente";

        } catch (Exception ex) {
            return "redirect:/ciudades?error=Error+al+eliminar+la+ciudad";
        }
    }
}