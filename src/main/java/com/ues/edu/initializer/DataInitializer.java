package com.ues.edu.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ues.edu.modelo.Usuario;
import com.ues.edu.modelo.Rol;
import com.ues.edu.servicios.IUsuarioService;
import com.ues.edu.servicios.IRolService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final IUsuarioService usuarioService;
    private final IRolService rolService; 
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(IUsuarioService usuarioService,
                           IRolService rolService, 
                           PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        crearRolesSistema();
        crearUsuarioAdmin();
    }

    private void crearRolesSistema() {
        String[][] roles = {
            {"ROLE_ADMIN", "Administrador del sistema - Acceso total"},
            {"ROLE_USER", "Usuario estándar - Acceso básico"}
            
        };

        for (String[] rolInfo : roles) {
            String nombre = rolInfo[0];
            String descripcion = rolInfo[1];
            
            Rol rolExistente = rolService.buscarPorNombre(nombre);
            if (rolExistente == null) {
                Rol nuevoRol = new Rol();
                nuevoRol.setNombre(nombre);
                nuevoRol.setDescripcion(descripcion);
                rolService.guardar(nuevoRol);
                System.out.println("Rol creado: " + nombre);
            }
        }
    }

    private void crearUsuarioAdmin() {
        Usuario adminExistente = usuarioService.buscarPorUsername("admin");
        if (adminExistente == null) {
            Rol rolAdmin = rolService.buscarPorNombre("ROLE_ADMIN");
            
            Usuario admin = Usuario.builder()
                .dui("00000000-0")
                .nombreCompleto("Administrador del Sistema")
                .cargo("Administrador")
                .telefono("12345678")
                .username("admin")
                .password("admin")
                .activo(true)
                .esAdmin(true)
                .roles(List.of(rolAdmin))
                .build();

            usuarioService.guardar(admin);
            System.out.println("Usuario admin creado.");
        }
    }
}
