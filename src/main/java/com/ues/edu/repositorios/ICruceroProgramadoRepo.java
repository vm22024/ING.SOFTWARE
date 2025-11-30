package com.ues.edu.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ues.edu.interfaces.excel.IListaReservasCruceroDTO;
import com.ues.edu.modelo.CruceroProgramado;

@Repository
public interface ICruceroProgramadoRepo extends JpaRepository<CruceroProgramado, Integer> {

	@Query(value = """
						SELECT
    cp.id_crucero,
    b.nombre as nombre_barco,
    n.nombre as naviera,
    po.nombre as puerto_origen,
    pd.nombre as puerto_destino,
    cp.fecha_salida,
    cp.fecha_regreso,
    cp.camarotes_disponibles,
    cp.pasajeros_registrados,
    mb.capacidad_pasajeros,
    CASE 
        WHEN mb.capacidad_pasajeros > 0 
        THEN ROUND((cp.pasajeros_registrados * 100.0 / mb.capacidad_pasajeros), 2)
        ELSE 0 
    END as ocupacion_porcentaje
FROM crucero_programado cp
JOIN barco b ON cp.id_barco = b.id_barco
JOIN modelo_barco mb ON b.id_modelo = mb.id_modelo
JOIN naviera n ON b.id_naviera = n.id_naviera
JOIN puerto po ON cp.id_puerto_origen = po.id_puerto
JOIN puerto pd ON cp.id_puerto_destino = pd.id_puerto
						""", nativeQuery = true)

	List<IListaReservasCruceroDTO> consultaCruceroProgramado();
}
