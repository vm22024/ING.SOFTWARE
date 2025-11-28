package com.ues.edu.repositorios;

import com.ues.edu.modelo.ModeloBarco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IModeloBarcoRepo extends JpaRepository<ModeloBarco, Integer>{
    
    // Buscar modelo por nombre 
    Optional<ModeloBarco> findByNombreIgnoreCase(String nombre);
    
    // Verificar si existe un modelo con el nombre 
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Buscar modelo por nombre excluyendo un ID específico (para edición)
    @Query("SELECT m FROM ModeloBarco m WHERE LOWER(m.nombre) = LOWER(:nombre) AND m.idModelo != :id")
    Optional<ModeloBarco> findByNombreAndIdModeloNot(@Param("nombre") String nombre, @Param("id") Integer id);
}