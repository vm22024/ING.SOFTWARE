package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ues.edu.modelo.ModeloBarco;
import com.ues.edu.repositorios.IModeloBarcoRepo;
import com.ues.edu.servicios.IModeloBarcoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ModeloBarcoServiceImpl implements IModeloBarcoService {

    private final IModeloBarcoRepo modeloBarcoRepo;

    public ModeloBarcoServiceImpl(IModeloBarcoRepo modeloBarcoRepo) {
        this.modeloBarcoRepo = modeloBarcoRepo;
    }

    @Override
    public ModeloBarco guardar(ModeloBarco obj) {
        return modeloBarcoRepo.save(obj);
    }

    @Override
    public List<ModeloBarco> listar() {
        return modeloBarcoRepo.findAll();
    }

    @Override
    public ModeloBarco leerPorId(Integer id) {
        return modeloBarcoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo de Barco no encontrado con ID: " + id));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (modeloBarcoRepo.existsById(id)) {
            modeloBarcoRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
