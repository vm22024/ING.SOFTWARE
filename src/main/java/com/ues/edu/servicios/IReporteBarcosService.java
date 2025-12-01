package com.ues.edu.servicios;

import java.util.List;

import com.ues.edu.dto.BarcosDTO;

public interface IReporteBarcosService {
	List<BarcosDTO> obtenerDatosParaReporte();

	byte[] generarReporteBarcosPDF();

}
