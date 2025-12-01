package com.ues.edu.interfaces.excel;

import java.time.LocalDate;

public interface IListaReservaCruceroDTO {
	Integer getIdReserva();
	 String getPasajero();
	 String getEmail();
	 String getTelefono();
	 Integer getIdCrucero();
	 String getBarco();
	 LocalDate getFechaReserva();
	 Integer getCantidadPersonas();
	 Integer getCantidadCamarotes();
}
