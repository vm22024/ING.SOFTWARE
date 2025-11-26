package com.ues.edu.modelo;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El DUI es obligatorio")
    @Size(min = 9, max = 10, message = "El DUI debe tener 9 dígitos (con guion)")
    @Column(nullable = false, unique = true, length = 10)
    private String dui;

    @NotBlank(message = "El nombre completo del usuario es obligatorio")
    @Size(min = 5, max = 150)
    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @NotBlank(message = "El cargo es obligatorio")
    @Column(nullable = false, length = 50)
    private String cargo;

    @Pattern(regexp = "^[0-9]{8,15}$", message = "El teléfono debe contener entre 8 y 15 dígitos")
    @Column(length = 15)
    private String telefono;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean activo = true;

    // Admin especial (congelar username/password y no eliminar)
    @Column(name = "es_admin", nullable = false)
    private boolean esAdmin = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private List<Rol> roles;

    // UserDetails
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return roles; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return activo; }
}
