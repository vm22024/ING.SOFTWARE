package com.ues.edu.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ues.edu.modelo.Puerto;

public interface IPuertoRepo extends JpaRepository<Puerto, Integer>{
	 boolean existsByNombre(String nombre);
	 
	 boolean existsByNombreIgnoreCase(String nombre);
	    
	    // Consulta para verificar si existe un puerto con el mismo nombre pero ID diferente (para edición)
	    @Query("SELECT COUNT(p) > 0 FROM Puerto p WHERE LOWER(p.nombre) = LOWER(:nombre) AND p.idPuerto != :id")
	    boolean existsByNombreIgnoreCaseAndIdPuertoNot(@Param("nombre") String nombre, @Param("id") Integer id);
}
