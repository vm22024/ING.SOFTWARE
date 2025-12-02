package com.ues.edu.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ues.edu.interfaces.excel.IListaReservaCruceroDTO;
import com.ues.edu.modelo.Reserva;

@Repository
public interface IReservaRepo extends JpaRepository<Reserva, Integer> {

	@Query(value = """
						SELECT
			    r.id_reserva AS idReserva,
			    p.nombre || ' ' || p.apellido as pasajero,
			    p.email AS email,
			    p.telefono AS telefono,
			    cp.id_crucero AS idCrucero,
			    b.nombre AS barco,
			    r.fecha_reserva AS fechaReserva,
			    r.cantidad_personas AS cantidadPersonas,
			    r.cantidad_camarotes AS cantidadCamarotes
			FROM reserva r
			JOIN pasajero p ON r.id_pasajero = p.id_pasajero
			JOIN crucero_programado cp ON r.id_crucero = cp.id_crucero
			JOIN barco b ON cp.id_barco = b.id_barco
						""", nativeQuery = true)

	List<IListaReservaCruceroDTO> consultaCrucero();

	@Query(value = """
								SELECT
			    c.fecha_salida AS fechaSalida,
			    b.nombre AS barco,
			    p.nombre AS puertoSalida,
			    c.fecha_regreso AS fechaRegreso,
				 (mb.total_camarotes - CEILING(SUM(r.cantidad_personas) / 2)) AS camarotesDisponibles,
				COUNT(r.id_reserva)  AS totalReservas


			FROM crucero_programado c
			LEFT JOIN reserva r
			    ON c.id_crucero = r.id_crucero
			INNER JOIN barco b
			    ON c.id_barco = b.id_barco
			INNER JOIN modelo_barco mb
			    ON b.id_modelo = mb.id_modelo
			INNER JOIN puerto p
			    ON p.id_puerto = c.id_puerto_origen

			GROUP BY
			    c.fecha_salida,
			    b.nombre,
			    p.nombre,
			    c.fecha_regreso,
			    mb.total_camarotes

			HAVING
			    SUM(r.cantidad_personas) > 0;


									""", nativeQuery = true)
	List<Object[]> listadoReservarNative();
}
