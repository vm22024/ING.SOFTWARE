package com.ues.edu.servicios;

import com.ues.edu.modelo.Ciudad;
import java.util.Optional;

public interface ICiudadService extends ICRUD<Ciudad,Integer>{
    long contarCiudades();
    Optional<Ciudad> buscarPorNombre(String nombre);
    boolean existePorNombre(String nombre);
    boolean existePorNombreExcluyendoId(String nombre, Integer id);
}