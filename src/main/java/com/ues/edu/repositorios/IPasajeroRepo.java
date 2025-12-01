package com.ues.edu.repositorios;

import com.ues.edu.modelo.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ues.edu.interfaces.excel.IListaTotalPasajerosDTO;
import java.util.List;
import java.util.Optional;
@Repository
public interface IPasajeroRepo extends JpaRepository<Pasajero, Integer>{
    
    // Buscar pasajero por email (campo único)
    Optional<Pasajero> findByEmailIgnoreCase(String email);
    
    // Verificar si existe un pasajero con el email (para evitar duplicados)
    boolean existsByEmailIgnoreCase(String email);
    
    // Buscar pasajero por email excluyendo un ID específico (para edición)
    @Query("SELECT p FROM Pasajero p WHERE LOWER(p.email) = LOWER(:email) AND p.idPasajero != :id")
    Optional<Pasajero> findByEmailAndIdPasajeroNot(@Param("email") String email, @Param("id") Integer id);
    
    // Buscar pasajeros por nombre (búsqueda flexible)
    Optional<Pasajero> findByNombreIgnoreCase(String nombre);
    
    // Buscar pasajeros por apellido (búsqueda flexible)
    Optional<Pasajero> findByApellidoIgnoreCase(String apellido);
    
    // Buscar pasajero por nombre y apellido (búsqueda exacta)
    Optional<Pasajero> findByNombreIgnoreCaseAndApellidoIgnoreCase(String nombre, String apellido);
    
    // Verificar si existe un pasajero con nombre y apellido
    boolean existsByNombreIgnoreCaseAndApellidoIgnoreCase(String nombre, String apellido);
    
    // Buscar pasajero por nombre y apellido excluyendo un ID específico
    @Query("SELECT p FROM Pasajero p WHERE LOWER(p.nombre) = LOWER(:nombre) AND LOWER(p.apellido) = LOWER(:apellido) AND p.idPasajero != :id")
    Optional<Pasajero> findByNombreAndApellidoAndIdPasajeroNot(@Param("nombre") String nombre, @Param("apellido") String apellido, @Param("id") Integer id);
    
    
    @Query(value = """
    		SELECT 
    id_pasajero,
    nombre,
    apellido,
    email,
    telefono
FROM pasajero
ORDER BY apellido, nombre
    		""", nativeQuery = true)
    
    List<IListaTotalPasajerosDTO> consultaTotalPasajeros();
}