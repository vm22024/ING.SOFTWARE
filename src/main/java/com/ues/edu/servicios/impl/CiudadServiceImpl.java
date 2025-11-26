package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Ciudad;
import com.ues.edu.repositorios.ICiudadRepo;
import com.ues.edu.servicios.ICiudadService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CiudadServiceImpl implements ICiudadService {

	private final ICiudadRepo ciudadRepo;

	public CiudadServiceImpl(ICiudadRepo ciudadRepo) {
		this.ciudadRepo = ciudadRepo;
	}

	@Override
	public Ciudad guardar(Ciudad obj) {
		return ciudadRepo.save(obj);
	}

	@Override
	public List<Ciudad> listar() {
		return ciudadRepo.findAll();
	}

	@Override
	public Ciudad leerPorId(Integer id) {
		return ciudadRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Ciudad no encontrada con ID: " + id));
	}

	@Override
	public boolean eliminar(Integer id) {
		if (ciudadRepo.existsById(id)) {
			ciudadRepo.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public long contarCiudades() {
		// TODO Auto-generated method stub
		return ciudadRepo.count();
	}
}
