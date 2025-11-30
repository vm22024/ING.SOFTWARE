package com.ues.edu.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ues.edu.interfaces.excel.IListaInventarioBarcosDTO;
import com.ues.edu.modelo.Barco;

@Repository
public interface IBarcoRepo extends JpaRepository<Barco, Integer>{
	
	 @Query("SELECT b FROM Barco b LEFT JOIN FETCH b.modelo LEFT JOIN FETCH b.naviera")
	    List<Barco> findAllWithRelations();

	 @Query(value = """
	 		SELECT 
    b.id_barco,
    b.nombre as nombre_barco,
    b.anio_construccion,
    n.nombre as naviera,
    mb.nombre as modelo,
    mb.capacidad_pasajeros,
    mb.total_camarotes
FROM barco b
JOIN naviera n ON b.id_naviera = n.id_naviera
JOIN modelo_barco mb ON b.id_modelo = mb.id_modelo
	 		""", nativeQuery = true)
	 
	 List<IListaInventarioBarcosDTO> consultaTotalBarcos();
}
