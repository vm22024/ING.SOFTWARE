package com.ues.edu.servicios.impl;

import java.util.List;
import java.util.Optional;

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
        // Validar si ya existe una ciudad con el mismo nombre
        if (obj.getIdCiudad() == null) {
            // Es una nueva ciudad - verificar si el nombre ya existe
            if (existePorNombre(obj.getNombre())) {
                throw new IllegalArgumentException("Ya existe una ciudad con el nombre: " + obj.getNombre());
            }
        } else {
            // Es una edición - verificar si el nombre ya existe excluyendo la ciudad actual
            if (existePorNombreExcluyendoId(obj.getNombre(), obj.getIdCiudad())) {
                throw new IllegalArgumentException("Ya existe una ciudad con el nombre: " + obj.getNombre());
            }
        }
        
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
        return ciudadRepo.count();
    }

    @Override
    public Optional<Ciudad> buscarPorNombre(String nombre) {
        return ciudadRepo.findByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existePorNombre(String nombre) {
        return ciudadRepo.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existePorNombreExcluyendoId(String nombre, Integer id) {
        return ciudadRepo.findByNombreAndIdCiudadNot(nombre, id).isPresent();
    }
}