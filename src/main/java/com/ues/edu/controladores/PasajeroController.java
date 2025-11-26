package com.ues.edu.controladores;

import com.ues.edu.modelo.Pasajero;
import com.ues.edu.servicios.IPasajeroService;

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
@RequestMapping("/pasajeros")
public class PasajeroController {

    @Autowired
    private IPasajeroService pasajeroService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override 
            public void setAsText(String text) {
                setValue(text == null ? null : text.trim());
            }
        });
    }

    // Listar todos los pasajeros
    @GetMapping
    public String listar(Model model, 
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        List<Pasajero> pasajeros = pasajeroService.listar();
        model.addAttribute("pasajeros", pasajeros);
        if (ok != null) model.addAttribute("ok", ok);
        if (error != null) model.addAttribute("error", error);
        return "pasajero/lista";
    }

    // Mostrar formulario para nuevo pasajero
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model,
                                   @RequestParam(value = "ok", required = false) String ok,
                                   @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("pasajero", new Pasajero());
        model.addAttribute("esEdicion", false);
        if (ok != null) model.addAttribute("ok", ok);
        if (error != null) model.addAttribute("error", error);
        return "pasajero/form";
    }

    // Guardar nuevo pasajero
    @PostMapping
    public String guardar(@Valid @ModelAttribute("pasajero") Pasajero pasajero,
                          BindingResult br,
                          Model model,
                          RedirectAttributes ra) {

        // Validar email único para nuevo pasajero
        if (pasajero.getEmail() != null && !pasajero.getEmail().trim().isEmpty() && 
            pasajero.getIdPasajero() == null && 
            pasajeroService.existePorEmail(pasajero.getEmail())) {
            br.rejectValue("email", "email.duplicado", "Ya existe un pasajero con ese email.");
        }

        // Validar nombre y apellido único para nuevo pasajero
        if (pasajero.getIdPasajero() == null && 
            pasajeroService.existePorNombreCompleto(pasajero.getNombre(), pasajero.getApellido())) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un pasajero con ese nombre y apellido.");
        }

        if (br.hasErrors()) {
            model.addAttribute("esEdicion", false);
            return "pasajero/form";
        }

        try {
            pasajeroService.guardar(pasajero);
            return "redirect:/pasajeros?ok=Pasajero+guardado+correctamente";

        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().contains("email")) {
                br.rejectValue("email", "email.duplicado", ex.getMessage());
            } else if (ex.getMessage().contains("nombre")) {
                br.rejectValue("nombre", "nombre.duplicado", ex.getMessage());
            } else {
                br.reject("globalError", ex.getMessage());
            }
            model.addAttribute("esEdicion", false);
            return "pasajero/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("email", "email.duplicado", "Ya existe un pasajero con ese email.");
            model.addAttribute("esEdicion", false);
            return "pasajero/form";

        } catch (Exception ex) {
            model.addAttribute("error", "Ocurrió un error inesperado. Inténtalo nuevamente.");
            model.addAttribute("esEdicion", false);
            return "pasajero/form";
        }
    }

    // Mostrar formulario para editar pasajero
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, 
                        Model model, 
                        RedirectAttributes ra,
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        try {
            Pasajero pasajero = pasajeroService.leerPorId(id);
            model.addAttribute("pasajero", pasajero);
            model.addAttribute("esEdicion", true);
            if (ok != null) model.addAttribute("ok", ok);
            if (error != null) model.addAttribute("error", error);
            return "pasajero/form";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Pasajero no encontrado: " + ex.getMessage());
            return "redirect:/pasajeros?error=Pasajero+no+encontrado";
        }
    }

    // Actualizar pasajero existente
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("pasajero") Pasajero pasajero,
                             BindingResult br,
                             Model model,
                             RedirectAttributes ra) {

        // Validar email único excluyendo el ID actual
        if (pasajero.getEmail() != null && !pasajero.getEmail().trim().isEmpty() && 
            pasajeroService.existePorEmailExcluyendoId(pasajero.getEmail(), id)) {
            br.rejectValue("email", "email.duplicado", "Ya existe un pasajero con ese email.");
        }

        // Validar nombre y apellido único excluyendo el ID actual
        if (pasajeroService.existePorNombreCompletoExcluyendoId(pasajero.getNombre(), pasajero.getApellido(), id)) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un pasajero con ese nombre y apellido.");
        }

        if (br.hasErrors()) {
            model.addAttribute("esEdicion", true);
            return "pasajero/form";
        }

        try {
            pasajero.setIdPasajero(id);
            pasajeroService.guardar(pasajero);
            return "redirect:/pasajeros?ok=Pasajero+actualizado+correctamente";

        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().contains("email")) {
                br.rejectValue("email", "email.duplicado", ex.getMessage());
            } else if (ex.getMessage().contains("nombre")) {
                br.rejectValue("nombre", "nombre.duplicado", ex.getMessage());
            } else {
                br.reject("globalError", ex.getMessage());
            }
            model.addAttribute("esEdicion", true);
            return "pasajero/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("email", "email.duplicado", "Ya existe un pasajero con ese email.");
            model.addAttribute("esEdicion", true);
            return "pasajero/form";

        } catch (Exception ex) {
            model.addAttribute("error", "Error al actualizar el pasajero: " + ex.getMessage());
            model.addAttribute("esEdicion", true);
            return "pasajero/form";
        }
    }

    // Eliminar pasajero
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            pasajeroService.eliminar(id);
            return "redirect:/pasajeros?ok=Pasajero+eliminado+correctamente";

        } catch (Exception ex) {
            return "redirect:/pasajeros?error=Error+al+eliminar+el+pasajero";
        }
    }
}