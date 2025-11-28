package com.ues.edu.servicios;

import com.ues.edu.modelo.Puerto;

public interface IPuertoService extends ICRUD<Puerto, Integer> {

	long contarPuertos();

	boolean existePorNombre(String nombre); // ← NUEVO MÉTODO

	boolean existePorNombreYIdDiferente(String nombre, Integer id); // ← NUEVO MÉTODO
}
