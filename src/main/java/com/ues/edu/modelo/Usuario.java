package com.ues.edu.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idUsuario;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, length = 255)
	private String password; // encriptado (BCrypt)

	private boolean enabled = true;

	@ManyToOne
	@JoinColumn(name = "idRol", nullable = false)
	private Rol rol;
}
