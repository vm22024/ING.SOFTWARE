package com.ues.edu.repositorios;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.edu.modelo.Usuario;

public interface IUsuarioRepo extends JpaRepository<Usuario, Integer>{

}
