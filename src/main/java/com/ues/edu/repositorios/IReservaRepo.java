package com.ues.edu.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.edu.modelo.Reserva;
import com.ues.edu.dto.ReservasDTO;

public interface IReservaRepo extends JpaRepository<Reserva, Integer>{

	  @Query(value = """
		        SELECT 
		            c.fecha_salida, 
		            b.nombre, 
		            p.nombre,
		            c.fecha_regreso, 
		            c.camarotes_disponibles,
		            COUNT(r.id_reserva)
		        FROM crucero_programado c
		        LEFT JOIN reserva r ON c.id_crucero = r.id_crucero
		        INNER JOIN barco b ON c.id_barco = b.id_barco
		        INNER JOIN puerto p ON p.id_puerto = c.id_puerto_origen
		        GROUP BY c.id_crucero, c.fecha_salida, b.nombre, p.nombre, c.fecha_regreso, c.camarotes_disponibles
		        """, nativeQuery = true)
		    List<Object[]> listadoReservarNative();
	
}
