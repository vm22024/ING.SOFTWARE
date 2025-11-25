package com.ues.edu.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ues.edu.modelo.Rol;

@Repository
public interface IRolRepo extends JpaRepository<Rol,Long>{
	
	@Query("Select r from Rol r where r.nombre=:nombre")
	Rol buscarPorNombre(@Param("nombre")String nombre);

}
