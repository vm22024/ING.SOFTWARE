package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Barco;
import com.ues.edu.repositorios.IBarcoRepo;
import com.ues.edu.servicios.IBarcoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BarcoServiceImpl implements IBarcoService {

    private final IBarcoRepo barcoRepo;

    public BarcoServiceImpl(IBarcoRepo barcoRepo) {
        super();
        this.barcoRepo = barcoRepo;
    }

    @Override
    public Barco guardar(Barco obj) {
        return barcoRepo.save(obj);
    }

    @Override
    public List<Barco> listar() {
        // Cambia esto para cargar las relaciones
        return barcoRepo.findAllWithRelations();
    }

    @Override
    public Barco leerPorId(Integer id) {
        return barcoRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Barco no encontrado con ID: " + id));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (barcoRepo.existsById(id)) {
            barcoRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public long contarBarcos() {
        return barcoRepo.count();
    }
}