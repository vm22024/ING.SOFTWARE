package com.ues.edu.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Pasajero {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPasajero;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private String apellido;

	@Column(unique = true)
	private String email;

	private String telefono;
}
