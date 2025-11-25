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
        Usuario adminExistente = usuarioService.buscarPorUsername("admin");

        if (adminExistente == null) {
            Rol rolAdmin = rolService.buscarPorNombre("ROLE_ADMIN");
            if (rolAdmin == null) {
                rolAdmin = new Rol(null, "ROLE_ADMIN");
                rolAdmin = rolService.guardar(rolAdmin);
            }

            Usuario admin = Usuario.builder()
                .dui("00000000-0")
                .nombreCompleto("Administrador")
                .cargo("Admin")
                .telefono("12345678")
                .username("admin")
                .password("admin") // se encripta en el service
                .activo(true)
                .roles(List.of(rolAdmin))
                .build();

            usuarioService.guardar(admin);
            System.out.println("Usuario admin creado con contraseña 'admin'.");
        } else {
            System.out.println("Usuario admin ya existe en la base de datos.");
        }
    }

    
}
