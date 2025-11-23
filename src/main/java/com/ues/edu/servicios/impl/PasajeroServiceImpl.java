package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Pasajero;
import com.ues.edu.repositorios.IPasajeroRepo;
import com.ues.edu.servicios.IPasajeroService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PasajeroServiceImpl implements IPasajeroService {

    private final IPasajeroRepo pasajeroRepo;

    public PasajeroServiceImpl(IPasajeroRepo pasajeroRepo) {
        this.pasajeroRepo = pasajeroRepo;
    }

    @Override
    public Pasajero guardar(Pasajero obj) {
        return pasajeroRepo.save(obj);
    }

    @Override
    public List<Pasajero> listar() {
        return pasajeroRepo.findAll();
    }

    @Override
    public Pasajero leerPorId(Integer id) {
        return pasajeroRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pasajero no encontrado con ID: " + id));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (pasajeroRepo.existsById(id)) {
            pasajeroRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
