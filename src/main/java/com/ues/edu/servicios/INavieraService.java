package com.ues.edu.servicios;

import java.util.Optional;


import com.ues.edu.modelo.Naviera;

public interface INavieraService extends ICRUD<Naviera,Integer>{
	long contarNavieras();
	Optional<Naviera> buscarPorNombre(String nombre);
    boolean existePorNombre(String nombre);
    boolean existePorNombreExcluyendoId(String nombre, Integer id);
}
