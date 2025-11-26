package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Puerto;
import com.ues.edu.repositorios.IPuertoRepo;
import com.ues.edu.servicios.IPuertoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PuertoServiceImpl implements IPuertoService {

    private final IPuertoRepo puertoRepo;

    public PuertoServiceImpl(IPuertoRepo puertoRepo) {
        this.puertoRepo = puertoRepo;
    }

    @Override
    public Puerto guardar(Puerto obj) {
        return puertoRepo.save(obj);
    }

    @Override
    public List<Puerto> listar() {
        return puertoRepo.findAll();
    }

    @Override
    public Puerto leerPorId(Integer id) {
        return puertoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Puerto no encontrado con ID: " + id));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (puertoRepo.existsById(id)) {
            puertoRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

	@Override
	public long contarPuertos() {
		// TODO Auto-generated method stub
		return puertoRepo.count();
	}
}
