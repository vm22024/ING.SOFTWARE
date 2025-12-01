package com.ues.edu.controladores;

import com.ues.edu.modelo.Barco;
import com.ues.edu.modelo.ModeloBarco;
import com.ues.edu.modelo.Naviera;
import com.ues.edu.servicios.IBarcoService;
import com.ues.edu.servicios.IModeloBarcoService;
import com.ues.edu.servicios.INavieraService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/barcos")
public class BarcoController {

	private final IBarcoService barcoService;
	private final INavieraService navieraService;
	private final IModeloBarcoService modeloBarcoService;

	@Autowired
	public BarcoController(IBarcoService barcoService, INavieraService navieraService,
			IModeloBarcoService modeloBarcoService) {
		this.barcoService = barcoService;
		this.navieraService = navieraService;
		this.modeloBarcoService = modeloBarcoService;
	}

	// LISTAR SIN BÚSQUEDA
	@GetMapping
	public String listar(Model model, 
	                    @RequestParam(value = "ok", required = false) String ok,
	                    @RequestParam(value = "error", required = false) String error) {
		List<Barco> barcos = barcoService.listar();

		model.addAttribute("titulo", "Barcos");
		model.addAttribute("barcos", barcos);
		
		// Pasar parámetros a la vista para SweetAlert
		if (ok != null) {
			model.addAttribute("ok", ok);
		}
		if (error != null) {
			model.addAttribute("error", error);
		}

		return "barcos/lista";
	}

	// FORM NUEVO
	@GetMapping("/nuevo")
	public String nuevo(Model model,
	                   @RequestParam(value = "error", required = false) String error) {
		Barco barco = new Barco();
		cargarCombos(model);
		model.addAttribute("barco", barco);
		model.addAttribute("modo", "nuevo");
		
		// Pasar parámetros a la vista para SweetAlert
		if (error != null) {
			model.addAttribute("error", error);
		}
		
		return "barcos/form";
	}

	// GUARDAR NUEVO
	@PostMapping("/guardar")
	public String guardar(@Valid @ModelAttribute("barco") Barco barco, BindingResult result,
			@RequestParam("naviera") Integer idNaviera, @RequestParam("modelo") Integer idModelo, Model model,
			RedirectAttributes ra) {

		if (result.hasErrors()) {
			cargarCombos(model);
			model.addAttribute("modo", "nuevo");
			return "barcos/form";
		}

		try {
			barco.setNaviera(navieraService.leerPorId(idNaviera));
			barco.setModelo(modeloBarcoService.leerPorId(idModelo));

			barcoService.guardar(barco);
			return "redirect:/barcos?ok=Barco registrado correctamente";
		} catch (Exception e) {
			return "redirect:/barcos/nuevo?error=Error al registrar el barco: " + e.getMessage();
		}
	}

	// FORM EDITAR - CORREGIDO (sin /editar en el path)
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable("id") Integer id, Model model,
	                    @RequestParam(value = "error", required = false) String error) {
		Barco barco = barcoService.leerPorId(id);

		if (barco == null) {
			return "redirect:/barcos?error=No se encontró el barco con ID: " + id;
		}

		cargarCombos(model);
		model.addAttribute("barco", barco);
		model.addAttribute("modo", "editar");
		
		// Pasar parámetros a la vista para SweetAlert
		if (error != null) {
			model.addAttribute("error", error);
		}
		
		return "barcos/form";
	}

	// ACTUALIZAR - CORREGIDO (sin /editar en el path)
	@PostMapping("/editar/{id}")
	public String actualizar(@PathVariable("id") Integer id, @Valid @ModelAttribute("barco") Barco barco,
			BindingResult result, @RequestParam("naviera") Integer idNaviera, @RequestParam("modelo") Integer idModelo,
			Model model, RedirectAttributes ra) {

		if (result.hasErrors()) {
			cargarCombos(model);
			model.addAttribute("modo", "editar");
			return "barcos/form";
		}

		try {
			barco.setIdBarco(id);
			barco.setNaviera(navieraService.leerPorId(idNaviera));
			barco.setModelo(modeloBarcoService.leerPorId(idModelo));

			barcoService.guardar(barco);
			return "redirect:/barcos?ok=Barco actualizado correctamente";
		} catch (Exception e) {
			return "redirect:/barcos/editar/" + id + "?error=Error al actualizar el barco: " + e.getMessage();
		}
	}

	// ELIMINAR - MANTENER COMO POST PARA EVITAR PROBLEMAS
	@PostMapping("/eliminar/{id}")
	public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
		try {
			boolean eliminado = barcoService.eliminar(id);
			
			if (eliminado) {
				return "redirect:/barcos?ok=Barco eliminado correctamente";
			} else {
				return "redirect:/barcos?error=No se pudo eliminar el barco. Tiene dependencias";
			}
		} catch (Exception e) {
			return "redirect:/barcos?error=Error al eliminar el barco: " + e.getMessage();
		}
	}

	// CARGAR COMBOS
	private void cargarCombos(Model model) {
		model.addAttribute("navieras", navieraService.listar());
		model.addAttribute("modelos", modeloBarcoService.listar());
	}
}