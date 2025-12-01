package com.ues.edu.controladores.excel;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ues.edu.servicios.impl.excel.ReporteCruceroProgramadoExcelImp;
import com.ues.edu.servicios.impl.excel.ReporteReservaCruceroExcel;
import com.ues.edu.servicios.impl.excel.ReporteTotalBarcosExcel;
import com.ues.edu.servicios.impl.excel.ReporteTotalPorPasajeroExcel;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

	@Autowired
	private ReporteCruceroProgramadoExcelImp excelCruceroProgramado;
	@Autowired
	private ReporteReservaCruceroExcel reservaCrucero;
	@Autowired
	private ReporteTotalPorPasajeroExcel totalPasajeros;
	@Autowired
	private ReporteTotalBarcosExcel totalBarcos;
	@GetMapping("/excel")
	public ModelAndView mostrarPaginaExportacion() {
		ModelAndView modelAndView = new ModelAndView("excel/exportarExcel");
		return modelAndView;
	}
	
	@GetMapping("/excel/cruceroProgramado")
	public void generarCruceroProgramadoExcel(HttpServletResponse response) throws IOException {
		excelCruceroProgramado.generarExcelCruceroProgramado(response);
	}
	
	@GetMapping("/excel/reservas")
	public void generarReservasExcel(HttpServletResponse response) throws IOException {
		reservaCrucero.generarReservaCrucero(response);
	}
	
	@GetMapping("/excel/pasajeros")
	public void generarPasajerosExcel(HttpServletResponse response) throws IOException {
		totalPasajeros.GenerarTotalporPasajeros(response);
	}
	
	@GetMapping("/excel/barcos")
	public void generarBarcosExcel(HttpServletResponse response) throws IOException {
		totalBarcos.generarInventarioBarco(response);
	}
	

}