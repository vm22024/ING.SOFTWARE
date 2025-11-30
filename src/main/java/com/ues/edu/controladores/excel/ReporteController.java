package com.ues.edu.controladores.excel;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ues.edu.servicios.impl.excel.ReporteCruceroProgramadoExcelImp;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

	@Autowired
	private ReporteCruceroProgramadoExcelImp excelCruceroProgramado;
	
	// Método para mostrar la página HTML de exportación
	@GetMapping("/excel")
	public ModelAndView mostrarPaginaExportacion() {
		ModelAndView modelAndView = new ModelAndView("excel/exportarExcel");
		// Aquí puedes agregar datos al modelo si necesitas mostrar estadísticas
		// modelAndView.addObject("totalUsuarios", usuarioService.contarUsuarios());
		// modelAndView.addObject("totalCruceros", cruceroService.contarCruceros());
		// etc...
		return modelAndView;
	}
	
	// Método existente para generar el Excel (manteniendo la funcionalidad)
	@GetMapping("/excel/cruceroProgramado")
	public void generarCruceroProgramadoExcel(HttpServletResponse response) throws IOException {
		excelCruceroProgramado.generarExcelCruceroProgramado(response);
	}
	
	// Métodos adicionales para las otras exportaciones
	@GetMapping("/excel/usuarios")
	public void generarUsuariosExcel(HttpServletResponse response) throws IOException {
		// Implementar lógica para exportar usuarios
		// usuarioExcelService.generarExcelUsuarios(response);
	}
	
	@GetMapping("/excel/reservas")
	public void generarReservasExcel(HttpServletResponse response) throws IOException {
		// Implementar lógica para exportar reservas
		// reservaExcelService.generarExcelReservas(response);
	}
	
	@GetMapping("/excel/pasajeros")
	public void generarPasajerosExcel(HttpServletResponse response) throws IOException {
		// Implementar lógica para exportar pasajeros
		// pasajeroExcelService.generarExcelPasajeros(response);
	}
	
	@GetMapping("/excel/barcos")
	public void generarBarcosExcel(HttpServletResponse response) throws IOException {
		// Implementar lógica para exportar barcos
		// barcoExcelService.generarExcelBarcos(response);
	}
	
	@GetMapping("/excel/navieras")
	public void generarNavierasExcel(HttpServletResponse response) throws IOException {
		// Implementar lógica para exportar navieras
		// navieraExcelService.generarExcelNavieras(response);
	}
	
	@GetMapping("/excel/puertos")
	public void generarPuertosExcel(HttpServletResponse response) throws IOException {
		// Implementar lógica para exportar puertos
		// puertoExcelService.generarExcelPuertos(response);
	}
	
	@GetMapping("/excel/completo")
	public void generarExportacionCompleta(HttpServletResponse response) throws IOException {
		// Implementar lógica para exportación completa
		// exportacionCompletaService.generarExcelCompleto(response);
	}
}