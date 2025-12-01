package com.ues.edu.servicios;

import java.util.List;

import com.ues.edu.dto.ReservasDTO;

public interface IReporteReservasService {
	List<ReservasDTO> obtenerDatosParaReporte();
    byte[] generarReporteReservasPDF();
}
