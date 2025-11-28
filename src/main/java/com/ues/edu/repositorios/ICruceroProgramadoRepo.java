package com.ues.edu.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.edu.modelo.CruceroProgramado;

public interface ICruceroProgramadoRepo extends JpaRepository<CruceroProgramado, Integer> {
    
    // Consulta para cargar todas las relaciones necesarias
    @Query("SELECT DISTINCT cp FROM CruceroProgramado cp " +
           "LEFT JOIN FETCH cp.barco b " +
           "LEFT JOIN FETCH b.modelo " +
           "LEFT JOIN FETCH b.naviera " +
           "LEFT JOIN FETCH cp.puertoOrigen po " +
           "LEFT JOIN FETCH po.ciudad " +
           "LEFT JOIN FETCH cp.puertoDestino pd " +
           "LEFT JOIN FETCH pd.ciudad")
    List<CruceroProgramado> findAllWithRelations();
    
    @Query("SELECT cp FROM CruceroProgramado cp " +
           "LEFT JOIN FETCH cp.barco b " +
           "LEFT JOIN FETCH b.modelo " +
           "LEFT JOIN FETCH b.naviera " +
           "LEFT JOIN FETCH cp.puertoOrigen po " +
           "LEFT JOIN FETCH po.ciudad " +
           "LEFT JOIN FETCH cp.puertoDestino pd " +
           "LEFT JOIN FETCH pd.ciudad " +
           "WHERE cp.idCrucero = :id")
    Optional<CruceroProgramado> findByIdWithRelations(Integer id);
}
