package com.ues.edu.controladores;

import com.ues.edu.modelo.ModeloBarco;
import com.ues.edu.servicios.IModeloBarcoService;

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
@RequestMapping("/modelos-barco")
public class ModeloBarcoController {

    @Autowired
    private IModeloBarcoService modeloBarcoService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override 
            public void setAsText(String text) {
                setValue(text == null ? null : text.trim());
            }
        });
    }

    // Listar todos los modelos
    @GetMapping
    public String listar(Model model, 
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        List<ModeloBarco> modelosBarco = modeloBarcoService.listar();
        model.addAttribute("modelosBarco", modelosBarco);
        if (ok != null) model.addAttribute("ok", ok);
        if (error != null) model.addAttribute("error", error);
        return "modelobarco/lista";
    }

    // Mostrar formulario para nuevo modelo
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model,
                                   @RequestParam(value = "ok", required = false) String ok,
                                   @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("modeloBarco", new ModeloBarco());
        model.addAttribute("esEdicion", false);
        if (ok != null) model.addAttribute("ok", ok);
        if (error != null) model.addAttribute("error", error);
        return "modelobarco/form";
    }

    // Guardar nuevo modelo
    @PostMapping
    public String guardar(@Valid @ModelAttribute("modeloBarco") ModeloBarco modeloBarco,
                          BindingResult br,
                          Model model,
                          RedirectAttributes ra) {

        if (modeloBarco.getIdModelo() == null && modeloBarcoService.existePorNombre(modeloBarco.getNombre())) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un modelo de barco con ese nombre.");
        }

        if (br.hasErrors()) {
            model.addAttribute("esEdicion", false);
            return "modelobarco/form";
        }

        try {
            modeloBarcoService.guardar(modeloBarco);
            return "redirect:/modelos-barco?ok=Modelo+de+barco+guardado+correctamente";

        } catch (IllegalArgumentException ex) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un modelo de barco con ese nombre.");
            model.addAttribute("esEdicion", false);
            return "modelobarco/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un modelo de barco con ese nombre.");
            model.addAttribute("esEdicion", false);
            return "modelobarco/form";

        } catch (Exception ex) {
            model.addAttribute("error", "Ocurrió un error inesperado. Inténtalo nuevamente.");
            model.addAttribute("esEdicion", false);
            return "modelobarco/form";
        }
    }

    // Mostrar formulario para editar modelo
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, 
                        Model model, 
                        RedirectAttributes ra,
                        @RequestParam(value = "ok", required = false) String ok,
                        @RequestParam(value = "error", required = false) String error) {
        try {
            ModeloBarco modeloBarco = modeloBarcoService.leerPorId(id);
            model.addAttribute("modeloBarco", modeloBarco);
            model.addAttribute("esEdicion", true);
            if (ok != null) model.addAttribute("ok", ok);
            if (error != null) model.addAttribute("error", error);
            return "modelobarco/form";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Modelo de barco no encontrado: " + ex.getMessage());
            return "redirect:/modelos-barco?error=Modelo+de+barco+no+encontrado";
        }
    }

    // Actualizar modelo existente
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable("id") Integer id,
                             @Valid @ModelAttribute("modeloBarco") ModeloBarco modeloBarco,
                             BindingResult br,
                             Model model,
                             RedirectAttributes ra) {

        if (modeloBarcoService.existePorNombreExcluyendoId(modeloBarco.getNombre(), id)) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un modelo de barco con ese nombre.");
        }

        if (br.hasErrors()) {
            model.addAttribute("esEdicion", true);
            return "modelobarco/form";
        }

        try {
            modeloBarco.setIdModelo(id);
            modeloBarcoService.guardar(modeloBarco);
            return "redirect:/modelos-barco?ok=Modelo+de+barco+actualizado+correctamente";

        } catch (IllegalArgumentException ex) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un modelo de barco con ese nombre.");
            model.addAttribute("esEdicion", true);
            return "modelobarco/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("nombre", "nombre.duplicado", "Ya existe un modelo de barco con ese nombre.");
            model.addAttribute("esEdicion", true);
            return "modelobarco/form";

        } catch (Exception ex) {
            model.addAttribute("error", "Error al actualizar el modelo de barco: " + ex.getMessage());
            model.addAttribute("esEdicion", true);
            return "modelobarco/form";
        }
    }

    // Eliminar modelo
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            modeloBarcoService.eliminar(id);
            return "redirect:/modelos-barco?ok=Modelo+de+barco+eliminado+correctamente";

        } catch (Exception ex) {
            return "redirect:/modelos-barco?error=Error+al+eliminar+el+modelo+de+barco";
        }
    }
}