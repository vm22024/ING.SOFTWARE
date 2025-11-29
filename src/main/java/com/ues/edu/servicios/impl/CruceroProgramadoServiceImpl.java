package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ues.edu.modelo.CruceroProgramado;
import com.ues.edu.repositorios.ICruceroProgramadoRepo;
import com.ues.edu.servicios.ICruceroProgramadoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CruceroProgramadoServiceImpl implements ICruceroProgramadoService {

    private final ICruceroProgramadoRepo cruceroRepo;

    public CruceroProgramadoServiceImpl(ICruceroProgramadoRepo cruceroRepo) {
        this.cruceroRepo = cruceroRepo;
    }

    @Override
    public CruceroProgramado guardar(CruceroProgramado obj) {
        return cruceroRepo.save(obj);
    }

    @Override
    public List<CruceroProgramado> listar() {
        return cruceroRepo.findAllWithRelations(); // Usar el método con JOIN FETCH
    }

    @Override
    public CruceroProgramado leerPorId(Integer id) {
        return cruceroRepo.findByIdWithRelations(id)
                .orElseThrow(() -> new EntityNotFoundException("Crucero Programado no encontrado con ID: " + id));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (cruceroRepo.existsById(id)) {
            cruceroRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

	@Override
	public long contarCruceros() {
		// TODO Auto-generated method stub
		return cruceroRepo.count();
	}
}
