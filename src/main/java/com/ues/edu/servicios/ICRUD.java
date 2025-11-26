package com.ues.edu.servicios;

import java.util.List;

public interface ICRUD <T, ID>{
	T guardar(T obj); // Sirve tanto para registrar como modificar

	List<T> listar();

	T leerPorId(ID id);

	boolean eliminar(ID id);
}
