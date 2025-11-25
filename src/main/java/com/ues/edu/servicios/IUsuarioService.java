package com.ues.edu.servicios;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ues.edu.modelo.Usuario;

public interface IUsuarioService extends ICRUD<Usuario,Long>,UserDetailsService{

	List<Usuario>buscarPorNombreOCargo(String filtro);
	Usuario buscarPorUsername(String username);
	Usuario login(String username,String password);
	long contarUsuarios();
	
}
