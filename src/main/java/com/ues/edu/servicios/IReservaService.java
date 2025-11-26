package com.ues.edu.servicios;

import com.ues.edu.modelo.Reserva;

public interface IReservaService extends ICRUD<Reserva,Integer>{

	long contarReservas();

}
