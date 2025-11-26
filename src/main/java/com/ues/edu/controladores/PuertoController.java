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
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : text.trim());
			}
		});
	}

	// Listar
	@GetMapping
	public String listar(Model model, @RequestParam(value = "ok", required = false) String ok) {
		List<Puerto> puertos = puertoService.listar();
		model.addAttribute("puertos", puertos);
		if (ok != null)
			model.addAttribute("ok", ok);
		return "puertos/lista";
	}

	// Nuevo
	@GetMapping("/nuevo")
	public String mostrarFormulario(Model model) {
		List<Ciudad> ciudades = ciudadService.listar();
		model.addAttribute("puerto", new Puerto());
		model.addAttribute("ciudades", ciudades);
		model.addAttribute("esEdicion", false);
		return "puertos/form";
	}

	@PostMapping
	public String guardar(@Valid @ModelAttribute("puerto") Puerto puerto, BindingResult br, Model model,
			RedirectAttributes ra) {

		List<Ciudad> ciudades = ciudadService.listar();
		model.addAttribute("ciudades", ciudades);
		model.addAttribute("esEdicion", false);

		if (br.hasErrors()) {
			return "puertos/form";
		}

		try {
			if (puerto.getCiudad() == null || puerto.getCiudad().getIdCiudad() == null) {
				br.rejectValue("ciudad", "ciudad.requerida", "Debe seleccionar una ciudad.");
				return "puertos/form";
			}

			Ciudad ciudad = ciudadService.leerPorId(puerto.getCiudad().getIdCiudad());
			puerto.setCiudad(ciudad);

			// VALIDACIÓN DE DUPLICADOS ANTES DE GUARDAR
			if (existePuertoConMismoNombre(puerto.getNombre(), null)) {
				br.rejectValue("nombre", "nombre.duplicado",
						"Ya existe un puerto con el nombre '" + puerto.getNombre() + "'.");
				return "puertos/form";
			}

			puertoService.guardar(puerto);
			ra.addFlashAttribute("ok", "Puerto '" + puerto.getNombre() + "' guardado correctamente.");
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
			return "puertos/form";

		} catch (DataIntegrityViolationException ex) {
			br.rejectValue("nombre", "nombre.duplicado", "Ya existe un puerto con ese nombre.");
			return "puertos/form";

		} catch (Exception ex) {
			model.addAttribute("error", "Ocurrió un error inesperado. Inténtalo nuevamente.");
			return "puertos/form";
		}
	}

	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Puerto puerto = puertoService.leerPorId(id);
			List<Ciudad> ciudades = ciudadService.listar();
			model.addAttribute("puerto", puerto);
			model.addAttribute("ciudades", ciudades);
			model.addAttribute("esEdicion", true);
			return "puertos/form";
		} catch (Exception ex) {
			ra.addFlashAttribute("error", ex.getMessage());
			return "redirect:/puertos";
		}
	}

	@PostMapping("/actualizar/{id}")
	public String actualizar(@PathVariable("id") Integer id, @Valid @ModelAttribute("puerto") Puerto puerto,
			BindingResult br, Model model, RedirectAttributes ra) {
		try {
			Puerto existente = puertoService.leerPorId(id);
			puerto.setIdPuerto(id);

			List<Ciudad> ciudades = ciudadService.listar();
			model.addAttribute("ciudades", ciudades);
			model.addAttribute("esEdicion", true);

			if (br.hasErrors()) {
				return "puertos/form";
			}

			if (puerto.getCiudad() == null || puerto.getCiudad().getIdCiudad() == null) {
				br.rejectValue("ciudad", "ciudad.requerida", "Debe seleccionar una ciudad.");
				return "puertos/form";
			}

			Ciudad ciudad = ciudadService.leerPorId(puerto.getCiudad().getIdCiudad());
			puerto.setCiudad(ciudad);

			if (existePuertoConMismoNombre(puerto.getNombre(), id)) {
				br.rejectValue("nombre", "nombre.duplicado",
						"Ya existe otro puerto con el nombre '" + puerto.getNombre() + "'.");
				return "puertos/form";
			}

			puertoService.guardar(puerto);
			ra.addFlashAttribute("ok", "Puerto '" + puerto.getNombre() + "' actualizado correctamente.");
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

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
		try {
			Puerto puerto = puertoService.leerPorId(id);
			puertoService.eliminar(id);
			ra.addFlashAttribute("ok", "Puerto '" + puerto.getNombre() + "' eliminado correctamente");
			return "redirect:/puertos";

		} catch (Exception ex) {
			ra.addFlashAttribute("error", "Error al eliminar el puerto: " + ex.getMessage());
			return "redirect:/puertos";
		}
	}

	// MÉTODO AUXILIAR PARA VALIDAR DUPLICADOS
	private boolean existePuertoConMismoNombre(String nombre, Integer idExcluir) {
		List<Puerto> puertos = puertoService.listar();
		for (Puerto p : puertos) {

			if (idExcluir != null && p.getIdPuerto().equals(idExcluir)) {
				continue;
			}
			if (p.getNombre().equalsIgnoreCase(nombre.trim())) {
				return true;
			}
		}
		return false;
	}
}