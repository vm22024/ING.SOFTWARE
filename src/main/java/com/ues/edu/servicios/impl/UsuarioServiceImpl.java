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


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.Rol;
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

    // 🔽🔽🔽 CAMBIA ESTE MÉTODO COMPLETAMENTE 🔽🔽🔽
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // === CÓDIGO DE DEBUG ===
        System.out.println("=== DEBUG USERDETAILS ===");
        System.out.println("Usuario: " + usuario.getUsername());
        System.out.println("Activo: " + usuario.isActivo());
        System.out.println("Tiene roles: " + (usuario.getRoles() != null));
        if (usuario.getRoles() != null) {
            System.out.println("Roles: " + usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toList()));
        } else {
            System.out.println("Roles: NULL - ESTE ES EL PROBLEMA!");
        }
        
        List<GrantedAuthority> authorities = usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
            .collect(Collectors.toList());

        System.out.println("Authorities: " + authorities);
        System.out.println("=========================");

        return new org.springframework.security.core.userdetails.User(
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.isActivo(), // enabled
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            authorities
        );
    }
    // 🔼🔼🔼 HASTA AQUÍ EL MÉTODO MODIFICADO 🔼🔼🔼

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

            // 🔽🔽🔽 NUEVO: ASIGNAR ROL AUTOMÁTICAMENTE SEGÚN EL CARGO 🔽🔽🔽
            if (obj.getRoles() == null || obj.getRoles().isEmpty()) {
                Rol rol = asignarRolSegunCargo(obj.getCargo());
                if (rol != null) {
                    obj.setRoles(List.of(rol));
                }
            }

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

    private String encodeIfNeeded(String rawOrEncoded) {
        if (rawOrEncoded == null) return null;
        String val = rawOrEncoded.trim();
        if (val.startsWith("$2a$") || val.startsWith("$2b$") || val.startsWith("$2y$")) return val; // ya es bcrypt
        return passwordEncoder.encode(val);
    }
    
    private Rol asignarRolSegunCargo(String cargo) {
     
        if (cargo == null) return null;
        
        String nombreRol;
        if (cargo.equalsIgnoreCase("Administrador") || cargo.equalsIgnoreCase("Administrador del Sistema")) {
            nombreRol = "ROLE_ADMIN";
        } else {
            nombreRol = "ROLE_USER";
        }
      
        Rol rol = new Rol();
        rol.setNombre(nombreRol);
      
        return rol;
    }
}