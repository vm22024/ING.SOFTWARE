package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Rol;
import com.ues.edu.repositorios.IRolRepo;
import com.ues.edu.servicios.IRolService;

import jakarta.persistence.EntityNotFoundException;
@Service
public class RolServiceImpl implements IRolService {

    private final IRolRepo rolRepo;
    
    @Autowired
    public RolServiceImpl(IRolRepo rolRepo) {
        this.rolRepo = rolRepo;
    }
    
    @Override
    public Rol guardar(Rol obj) {
        return rolRepo.save(obj);
    }

    @Override
    public List<Rol> listar() {
        return rolRepo.findAll();
    }

    @Override
    public Rol leerPorId(Long id) {
        return rolRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + id));
    }

    @Override
    public boolean eliminar(Long id) {
        if (rolRepo.existsById(id)) {
            rolRepo.deleteById(id);
            return true; // ✅ IMPORTANTE: Cambiar a return true
        }
        return false;
    }

    @Override
    public Rol buscarPorNombre(String nombre) {
        return rolRepo.buscarPorNombre(nombre);
    }
}