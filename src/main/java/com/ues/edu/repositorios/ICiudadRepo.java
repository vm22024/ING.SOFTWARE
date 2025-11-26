package com.ues.edu.repositorios;

import com.ues.edu.modelo.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ICiudadRepo extends JpaRepository<Ciudad, Integer>{
    
    // Buscar ciudad por nombre 
    Optional<Ciudad> findByNombreIgnoreCase(String nombre);
    
    // Verificar si existe una ciudad con el nombre 
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Buscar ciudad por nombre excluyendo un ID específico (para edición)
    @Query("SELECT c FROM Ciudad c WHERE LOWER(c.nombre) = LOWER(:nombre) AND c.idCiudad != :id")
    Optional<Ciudad> findByNombreAndIdCiudadNot(@Param("nombre") String nombre, @Param("id") Integer id);
    

}