package com.ues.edu.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ues.edu.dto.BarcosDTO;
import com.ues.edu.modelo.Barco;

public interface IBarcoRepo extends JpaRepository<Barco, Integer>{
	 @Query(value = """
		        SELECT 
		            b.nombre as barco, 
		            b.anio_construccion as construccion, 
		            m.nombre as modelo, 
		            n.nombre as naviera, 
		            n.pais
		        FROM barco b
		        INNER JOIN modelo_barco m ON b.id_modelo = m.id_modelo
		        INNER JOIN naviera n ON b.id_naviera = n.id_naviera
		        """, nativeQuery = true)
		    List<Object[]> listadoBarcosNative();
}
