package com.ues.edu.controladores.excel;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ues.edu.servicios.impl.excel.ReporteCruceroProgramadoExcelImp;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/excel")
public class ReporteController {

	@Autowired
	private ReporteCruceroProgramadoExcelImp excelCruceroProgramado;
	
	@GetMapping("/cruceroProgramado")
	public void generarCruceroProgramadoExcel (HttpServletResponse response) throws IOException{
		excelCruceroProgramado.generarExcelCruceroProgramado(response);
	}
}
