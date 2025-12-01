package com.ues.edu.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ues.edu.modelo.Usuario;



public interface IUsuarioRepo extends JpaRepository<Usuario, Long> {

    // 🔽🔽🔽 CAMBIO CRÍTICO: Agregar LEFT JOIN FETCH para cargar los roles
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<Usuario> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM Usuario u WHERE u.nombreCompleto LIKE %:filtro% OR u.cargo LIKE %:filtro%")
    List<Usuario> buscarPorNombreOCargo(@Param("filtro") String filtro);
    
    boolean existsByUsername(String username);
    boolean existsByDui(String dui);
     
    boolean existsByDuiAndIdNot(String dui, Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);
}