package com.ues.edu.servicios.excel;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface IReporteCruceroProgramadoExcel {
	void generarExcelCruceroProgramado(HttpServletResponse response) throws IOException;
}
