package com.ues.edu.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.edu.modelo.Barco;

public interface IBarcoRepo extends JpaRepository<Barco, Integer>{
	
	 @Query("SELECT b FROM Barco b LEFT JOIN FETCH b.modelo LEFT JOIN FETCH b.naviera")
	    List<Barco> findAllWithRelations();

}
