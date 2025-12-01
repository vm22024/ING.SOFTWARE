package com.ues.edu.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ues.edu.modelo.Ciudad;
import com.ues.edu.modelo.Naviera;

public interface INavieraRepo extends JpaRepository<Naviera, Integer>{

Optional<Naviera> findByNombreIgnoreCase(String nombre);
    
  
    boolean existsByNombreIgnoreCase(String nombre);
  
    @Query("SELECT n FROM Naviera n WHERE LOWER(n.nombre) = LOWER(:nombre) AND n.idNaviera != :id")
    Optional<Naviera> findByNombreAndIdNavieraNot(@Param("nombre") String nombre, @Param("id") Integer id);
  
    @Query("SELECT COUNT(n) > 0 FROM Naviera n WHERE LOWER(n.nombre) = LOWER(:nombre) AND n.idNaviera != :id")
    boolean existsByNombreAndIdNavieraNot(@Param("nombre") String nombre, @Param("id") Integer id);
	
}
