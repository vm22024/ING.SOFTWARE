package com.ues.edu.servicios.impl;

import java.util.List;
import java.util.Optional;

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
        // Validar si ya existe un modelo con el mismo nombre
        if (obj.getIdModelo() == null) {
            // Es un nuevo modelo - verificar si el nombre ya existe
            if (existePorNombre(obj.getNombre())) {
                throw new IllegalArgumentException("Ya existe un modelo de barco con el nombre: " + obj.getNombre());
            }
        } else {
            // Es una edición - verificar si el nombre ya existe excluyendo el modelo actual
            if (existePorNombreExcluyendoId(obj.getNombre(), obj.getIdModelo())) {
                throw new IllegalArgumentException("Ya existe un modelo de barco con el nombre: " + obj.getNombre());
            }
        }
        
        return modeloBarcoRepo.save(obj);
    }

    @Override
    public List<ModeloBarco> listar() {
        return modeloBarcoRepo.findAll();
    }

    @Override
    public ModeloBarco leerPorId(Integer id) {
        return modeloBarcoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Modelo de barco no encontrado con ID: " + id));
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
    
    @Override
    public long contarModelosBarco() {
        return modeloBarcoRepo.count();
    }

    @Override
    public Optional<ModeloBarco> buscarPorNombre(String nombre) {
        return modeloBarcoRepo.findByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return modeloBarcoRepo.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existePorNombreExcluyendoId(String nombre, Integer id) {
        return modeloBarcoRepo.findByNombreAndIdModeloNot(nombre, id).isPresent();
    }
}