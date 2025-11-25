package com.ues.edu.controladores;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.servicios.IUsuarioService;

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
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

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
        List<Usuario> usuarios = usuarioService.listar();
        model.addAttribute("usuarios", usuarios);
        if (ok != null) model.addAttribute("ok", ok);
        return "usuarios/lista";
    }

    // Nuevo
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("esEdicion", false);
        model.addAttribute("adminProtegido", false);
        return "usuarios/form";
    }

    // Guardar nuevo
    @PostMapping
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult br,
                          Model model,
                          RedirectAttributes ra) {

        if (br.hasErrors()) {
            model.addAttribute("esEdicion", false);
            model.addAttribute("adminProtegido", false);
            return "usuarios/form";
        }

        try {
            usuarioService.guardar(usuario);
            ra.addFlashAttribute("ok", "Usuario guardado correctamente.");
            return "redirect:/usuarios";

        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Datos inválidos.";
            String lower = msg.toLowerCase();
            if (lower.contains("dui")) {
                br.rejectValue("dui", "dui.duplicado", "El número de DUI ya está registrado.");
            } else if (lower.contains("usuario") || lower.contains("username")) {
                br.rejectValue("username", "username.duplicado", "Ya existe un usuario con ese nombre de usuario.");
            } else if (lower.contains("contraseña") || lower.contains("password")) {
                br.rejectValue("password", "password.invalida", msg);
            } else {
                model.addAttribute("error", msg);
            }
            model.addAttribute("esEdicion", false);
            model.addAttribute("adminProtegido", false);
            return "usuarios/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("dui", "dui.duplicado", "El número de DUI ya está registrado.");
            model.addAttribute("esEdicion", false);
            model.addAttribute("adminProtegido", false);
            return "usuarios/form";

        } catch (Exception ex) {
            model.addAttribute("error", "Ocurrió un error inesperado. Inténtalo nuevamente.");
            model.addAttribute("esEdicion", false);
            model.addAttribute("adminProtegido", false);
            return "usuarios/form";
        }
    }

    // Editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try {
            Usuario usuario = usuarioService.leerPorId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("esEdicion", true);
            boolean adminProtegido = usuario.isEsAdmin() || "admin".equalsIgnoreCase(usuario.getUsername());
            model.addAttribute("adminProtegido", adminProtegido);
            return "usuarios/form";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/usuarios";
        }
    }

    // Actualizar
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("usuario") Usuario usuario,
                             BindingResult br,
                             Model model,
                             RedirectAttributes ra) {
        try {
            Usuario existente = usuarioService.leerPorId(id);

            usuario.setId(id);
            usuario.setEsAdmin(existente.isEsAdmin());

            boolean adminProtegido = existente.isEsAdmin() || "admin".equalsIgnoreCase(existente.getUsername());

            if (br.hasErrors()) {
                model.addAttribute("esEdicion", true);
                model.addAttribute("adminProtegido", adminProtegido);
                return "usuarios/form";
            }

            if (adminProtegido) {
                usuario.setUsername(existente.getUsername());
            }
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                usuario.setPassword(existente.getPassword());
            }

            usuarioService.guardar(usuario);
            ra.addFlashAttribute("ok", "Usuario actualizado correctamente.");
            return "redirect:/usuarios";

        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() != null ? ex.getMessage() : "Datos inválidos.";
            String lower = msg.toLowerCase();

            if (lower.contains("dui")) {
                br.rejectValue("dui", "dui.duplicado", "El número de DUI ya está registrado.");
            } else if (lower.contains("usuario") || lower.contains("username")) {
                br.rejectValue("username", "username.duplicado", "Ya existe un usuario con ese nombre de usuario.");
            } else if (lower.contains("contraseña") || lower.contains("password")) {
                br.rejectValue("password", "password.invalida", msg);
            } else {
                model.addAttribute("error", msg);
            }

            model.addAttribute("esEdicion", true);
            try {
                Usuario exi = usuarioService.leerPorId(id);
                boolean adminProtegido = exi.isEsAdmin() || "admin".equalsIgnoreCase(exi.getUsername());
                model.addAttribute("adminProtegido", adminProtegido);
            } catch (Exception ignore) {
                model.addAttribute("adminProtegido", false);
            }
            return "usuarios/form";

        } catch (DataIntegrityViolationException ex) {
            br.rejectValue("dui", "dui.duplicado", "El número de DUI ya está registrado.");
            model.addAttribute("esEdicion", true);
            model.addAttribute("adminProtegido", false);
            return "usuarios/form";

        } catch (Exception ex) {
            model.addAttribute("error", "Error al actualizar el usuario: " + ex.getMessage());
            model.addAttribute("esEdicion", true);
            model.addAttribute("adminProtegido", false);
            return "usuarios/form";
        }
    }

    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            Usuario usuario = usuarioService.leerPorId(id);
            if (usuario.isEsAdmin() || "admin".equalsIgnoreCase(usuario.getUsername())) {
                ra.addFlashAttribute("error", "No se puede eliminar el usuario admin");
                return "redirect:/usuarios";
            }
            usuarioService.eliminar(id);
            ra.addFlashAttribute("ok", "Usuario eliminado correctamente");
            return "redirect:/usuarios";

        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Error al eliminar el usuario: " + ex.getMessage());
            return "redirect:/usuarios";
        }
    }
}