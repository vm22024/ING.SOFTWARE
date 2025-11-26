package com.ues.edu.servicios;

import com.ues.edu.modelo.ModeloBarco;
import java.util.Optional;

public interface IModeloBarcoService extends ICRUD<ModeloBarco,Integer>{
    long contarModelosBarco();
    Optional<ModeloBarco> buscarPorNombre(String nombre);
    boolean existePorNombre(String nombre);
    boolean existePorNombreExcluyendoId(String nombre, Integer id);
}