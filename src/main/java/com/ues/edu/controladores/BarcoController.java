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
	public String listar(Model model) {
		List<Barco> barcos = barcoService.listar();

		model.addAttribute("titulo", "Barcos");
		model.addAttribute("barcos", barcos);

		return "barcos/lista";
	}

	// FORM NUEVO
	@GetMapping("/nuevo")
	public String nuevo(Model model) {
		Barco barco = new Barco();
		cargarCombos(model);
		model.addAttribute("barco", barco);
		model.addAttribute("modo", "nuevo");
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

		barco.setNaviera(navieraService.leerPorId(idNaviera));
		barco.setModelo(modeloBarcoService.leerPorId(idModelo));

		barcoService.guardar(barco);
		ra.addFlashAttribute("ok", "Barco registrado correctamente.");
		return "redirect:/barcos";
	}

	// FORM EDITAR
	@GetMapping("/{id}/editar")
	public String editar(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		Barco barco = barcoService.leerPorId(id);

		if (barco == null) {
			ra.addFlashAttribute("error", "No se encontró el barco con ID: " + id);
			return "redirect:/barcos";
		}

		cargarCombos(model);
		model.addAttribute("barco", barco);
		model.addAttribute("modo", "editar");
		return "barcos/form";
	}

	// ACTUALIZAR
	@PostMapping("/{id}/editar")
	public String actualizar(@PathVariable("id") Integer id, @Valid @ModelAttribute("barco") Barco barco,
			BindingResult result, @RequestParam("naviera") Integer idNaviera, @RequestParam("modelo") Integer idModelo,
			Model model, RedirectAttributes ra) {

		if (result.hasErrors()) {
			cargarCombos(model);
			model.addAttribute("modo", "editar");
			return "barcos/form";
		}

		barco.setIdBarco(id);
		barco.setNaviera(navieraService.leerPorId(idNaviera));
		barco.setModelo(modeloBarcoService.leerPorId(idModelo));

		barcoService.guardar(barco);
		ra.addFlashAttribute("ok", "Barco actualizado correctamente.");
		return "redirect:/barcos";
	}

	// ELIMINAR
	@PostMapping("/{id}/eliminar")
	public String eliminar(@PathVariable("id") Integer id, RedirectAttributes ra) {
		boolean eliminado = barcoService.eliminar(id);

		ra.addFlashAttribute(eliminado ? "ok" : "error",
				eliminado ? "Barco eliminado correctamente." : "No se pudo eliminar. Tiene dependencias.");

		return "redirect:/barcos";
	}

	// CARGAR COMBOS
	private void cargarCombos(Model model) {
		model.addAttribute("navieras", navieraService.listar());
		model.addAttribute("modelos", modeloBarcoService.listar());
	}
}