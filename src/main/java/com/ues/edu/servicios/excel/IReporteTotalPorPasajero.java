package com.ues.edu.servicios.excel;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public interface IReporteTotalPorPasajero {
void GenerarTotalporPasajeros(HttpServletResponse response) throws IOException;
}
