package com.ues.edu.servicios.excel;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface IReporteInventarioBarcoExcel {
	void generarInventarioBarco(HttpServletResponse response) throws IOException;
}
