package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.repositorios.IUsuarioRepo;
import com.ues.edu.servicios.IUsuarioService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepo usuarioRepo;

    public UsuarioServiceImpl(IUsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public Usuario guardar(Usuario obj) {
        return usuarioRepo.save(obj);
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepo.findAll();
    }

    @Override
    public Usuario leerPorId(Integer id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public boolean eliminar(Integer id) {
        if (usuarioRepo.existsById(id)) {
            usuarioRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
