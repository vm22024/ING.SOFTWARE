package com.ues.edu.servicios.excel;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface IReporteReservaCrucero {
	void generarReservaCrucero(HttpServletResponse response) throws IOException;
}
