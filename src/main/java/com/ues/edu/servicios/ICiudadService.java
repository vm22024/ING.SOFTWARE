package com.ues.edu.servicios;

import com.ues.edu.modelo.Ciudad;

public interface ICiudadService extends ICRUD<Ciudad,Integer>{
	long contarCiudades();
}
