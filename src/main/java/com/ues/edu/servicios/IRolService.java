package com.ues.edu.servicios;

import java.util.List;

import com.ues.edu.modelo.Rol;

public interface IRolService extends ICRUD<Rol, Long> {
   
    Rol buscarPorNombre(String nombre);
}
