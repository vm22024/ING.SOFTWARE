package com.ues.edu.servicios.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.repositorios.IUsuarioRepo;
import com.ues.edu.servicios.IUsuarioService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepo usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(IUsuarioRepo usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario guardar(Usuario obj) {
        boolean hayUsuarios = usuarioRepo.count() > 0;

        // ===== CREACIÓN =====
        if (obj.getId() == null) {

            if (usuarioRepo.existsByDui(obj.getDui()))
                throw new IllegalArgumentException("El número de DUI ya está registrado.");

            if ("admin".equalsIgnoreCase(obj.getUsername()) && hayUsuarios)
                throw new IllegalArgumentException("No se puede crear otro usuario con nombre de usuario 'admin'.");

            if ("admin".equalsIgnoreCase(obj.getPassword()) && hayUsuarios)
                throw new IllegalArgumentException("La contraseña no puede ser 'admin'. Elige una más segura.");

            if (usuarioRepo.existsByUsername(obj.getUsername()))
                throw new IllegalArgumentException("Ya existe un usuario con ese nombre de usuario.");

            if (obj.getPassword() == null || obj.getPassword().isBlank())
                throw new IllegalArgumentException("La contraseña es obligatoria.");

            obj.setPassword(passwordEncoder.encode(obj.getPassword()));
            return usuarioRepo.save(obj);
        }

        // ===== ACTUALIZACIÓN =====
        Usuario actual = usuarioRepo.findById(obj.getId())
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + obj.getId()));

        // Si es admin protegido: congelar username y password (no lanzar excepción)
        boolean adminProtegido = actual.isEsAdmin() || "admin".equalsIgnoreCase(actual.getUsername());
        if (adminProtegido) {
            obj.setUsername(actual.getUsername());
        }

        // DUI/username duplicados en otro registro
        if (obj.getDui() != null && usuarioRepo.existsByDuiAndIdNot(obj.getDui(), obj.getId()))
            throw new IllegalArgumentException("El número de DUI ya está registrado.");

        if (obj.getUsername() != null && usuarioRepo.existsByUsernameAndIdNot(obj.getUsername(), obj.getId()))
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre de usuario.");

        // Password: conservar si viene vacía; encriptar si viene en claro
        if (obj.getPassword() == null || obj.getPassword().isBlank()) {
            obj.setPassword(actual.getPassword());
        } else {
            obj.setPassword(encodeIfNeeded(obj.getPassword()));
        }

        // Preservar bandera admin (por seguridad)
        obj.setEsAdmin(actual.isEsAdmin());

        return usuarioRepo.save(obj);
    }

    @Override
    public boolean eliminar(Long id) {
        Usuario usuario = leerPorId(id);
        if (usuario.isEsAdmin() || "admin".equalsIgnoreCase(usuario.getUsername()))
            throw new RuntimeException("No se puede eliminar el usuario admin");
        usuarioRepo.delete(usuario);
        return true;
    }

    @Override
    public List<Usuario> listar() { return usuarioRepo.findAll(); }

    @Override
    public Usuario leerPorId(Long id) {
        return usuarioRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Override
    public List<Usuario> buscarPorNombreOCargo(String filtro) {
        return usuarioRepo.buscarPorNombreOCargo(filtro);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        Optional<Usuario> usuario = usuarioRepo.findByUsername(username);
        return usuario.orElse(null);
    }

    @Override
    public Usuario login(String username, String password) {
        Usuario usuario = buscarPorUsername(username);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    @Override
    public long contarUsuarios() { return usuarioRepo.count(); }

    @Override
    public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = buscarPorUsername(username);
        if (usuario == null) throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        return usuario;
    }

    private String encodeIfNeeded(String rawOrEncoded) {
        if (rawOrEncoded == null) return null;
        String val = rawOrEncoded.trim();
        if (val.startsWith("$2a$") || val.startsWith("$2b$") || val.startsWith("$2y$")) return val; // ya es bcrypt
        return passwordEncoder.encode(val);
    }
}