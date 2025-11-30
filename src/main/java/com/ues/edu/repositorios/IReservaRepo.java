package com.ues.edu.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ues.edu.interfaces.excel.IListaReservaCruceroDTO;
import com.ues.edu.modelo.Reserva;

@Repository
public interface IReservaRepo extends JpaRepository<Reserva, Integer>{

	@Query(value ="""
			SELECT 
    r.id_reserva,
    p.nombre || ' ' || p.apellido as pasajero,
    p.email,
    p.telefono,
    cp.id_crucero,
    b.nombre as barco,
    r.fecha_reserva,
    r.cantidad_personas,
    r.cantidad_camarotes
FROM reserva r
JOIN pasajero p ON r.id_pasajero = p.id_pasajero
JOIN crucero_programado cp ON r.id_crucero = cp.id_crucero
JOIN barco b ON cp.id_barco = b.id_barco
			""", nativeQuery = true)
	
	List<IListaReservaCruceroDTO> consultaCrucero();

	
}
