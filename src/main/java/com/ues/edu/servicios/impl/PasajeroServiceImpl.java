package com.ues.edu.servicios.impl;

import java.util.List;
import java.util.Optional;

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
        // Validar si ya existe un pasajero con el mismo email
        if (obj.getEmail() != null && !obj.getEmail().trim().isEmpty()) {
            if (obj.getIdPasajero() == null) {
                // Es un nuevo pasajero - verificar si el email ya existe
                if (existePorEmail(obj.getEmail())) {
                    throw new IllegalArgumentException("Ya existe un pasajero con el email: " + obj.getEmail());
                }
            } else {
                // Es una edición - verificar si el email ya existe excluyendo el pasajero actual
                if (existePorEmailExcluyendoId(obj.getEmail(), obj.getIdPasajero())) {
                    throw new IllegalArgumentException("Ya existe un pasajero con el email: " + obj.getEmail());
                }
            }
        }
        
        // Validar nombre y apellido (opcional, según requerimientos)
        if (obj.getIdPasajero() == null) {
            // Es un nuevo pasajero - verificar si ya existe con mismo nombre y apellido
            if (existePorNombreCompleto(obj.getNombre(), obj.getApellido())) {
                throw new IllegalArgumentException("Ya existe un pasajero con el nombre: " + obj.getNombre() + " " + obj.getApellido());
            }
        } else {
            // Es una edición - verificar si ya existe con mismo nombre y apellido excluyendo el actual
            if (existePorNombreCompletoExcluyendoId(obj.getNombre(), obj.getApellido(), obj.getIdPasajero())) {
                throw new IllegalArgumentException("Ya existe un pasajero con el nombre: " + obj.getNombre() + " " + obj.getApellido());
            }
        }
        
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

    @Override
    public long contarPasajeros() {
        return pasajeroRepo.count();
    }

    @Override
    public Optional<Pasajero> buscarPorEmail(String email) {
        return pasajeroRepo.findByEmailIgnoreCase(email);
    }

    @Override
    public boolean existePorEmail(String email) {
        return pasajeroRepo.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existePorEmailExcluyendoId(String email, Integer id) {
        return pasajeroRepo.findByEmailAndIdPasajeroNot(email, id).isPresent();
    }

    @Override
    public Optional<Pasajero> buscarPorNombreCompleto(String nombre, String apellido) {
        return pasajeroRepo.findByNombreIgnoreCaseAndApellidoIgnoreCase(nombre, apellido);
    }

    @Override
    public boolean existePorNombreCompleto(String nombre, String apellido) {
        return pasajeroRepo.existsByNombreIgnoreCaseAndApellidoIgnoreCase(nombre, apellido);
    }

    @Override
    public boolean existePorNombreCompletoExcluyendoId(String nombre, String apellido, Integer id) {
        return pasajeroRepo.findByNombreAndApellidoAndIdPasajeroNot(nombre, apellido, id).isPresent();
    }
}