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
       
        if (obj.getIdCrucero() == null) {
            obj.setPasajerosRegistrados(0);
        }
        
       
        if (obj.getBarco() != null && obj.getBarco().getModelo() != null) {
            int capacidadTotal = obj.getBarco().getModelo().getCapacidadPasajeros();
            int camarotesTotales = capacidadTotal / 2; // 2 pasajeros por camarote
            int camarotesOcupados = obj.getPasajerosRegistrados() / 2;
            obj.setCamarotesDisponibles(camarotesTotales - camarotesOcupados);
        }
        
        return cruceroRepo.save(obj);
    }

    @Override
    public List<CruceroProgramado> listar() {
        return cruceroRepo.findAllWithRelations(); 
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
