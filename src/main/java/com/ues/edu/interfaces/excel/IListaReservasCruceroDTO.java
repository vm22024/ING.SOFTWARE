package com.ues.edu.interfaces.excel;

import java.time.LocalDate;

public interface IListaReservasCruceroDTO {

	 Integer getIdCrucero();
	 String getNombreBarco();
	 String getNaviera();
	 String getPuertoOrigen();
	 String getPuertoDestino();
	 LocalDate getFechaSalida();
	 LocalDate getFechaRegreso();
	 Integer getCamarotesDisponibles();
	 Integer getPasajerosRegistrados();
	 Integer getCapacidadPasajeros();
	 Double getOcupacionPorcentaje();
	
}
