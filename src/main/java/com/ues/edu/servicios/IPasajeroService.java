package com.ues.edu.servicios;

import com.ues.edu.modelo.Pasajero;
import java.util.Optional;

public interface IPasajeroService extends ICRUD<Pasajero,Integer>{
    long contarPasajeros();
    Optional<Pasajero> buscarPorEmail(String email);
    boolean existePorEmail(String email);
    boolean existePorEmailExcluyendoId(String email, Integer id);
    Optional<Pasajero> buscarPorNombreCompleto(String nombre, String apellido);
    boolean existePorNombreCompleto(String nombre, String apellido);
    boolean existePorNombreCompletoExcluyendoId(String nombre, String apellido, Integer id);
}