package com.ues.edu.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.edu.modelo.Puerto;

public interface IPuertoRepo extends JpaRepository<Puerto, Integer>{
	 boolean existsByNombre(String nombre);
}
