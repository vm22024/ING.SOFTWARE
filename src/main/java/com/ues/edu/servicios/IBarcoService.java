package com.ues.edu.servicios;

import com.ues.edu.modelo.Barco;

public interface IBarcoService extends ICRUD<Barco,Integer> {

	long contarBarcos();

}
