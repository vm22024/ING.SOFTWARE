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
public class Puerto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPuerto;

	@Column(nullable = false)
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "idCiudad", nullable = false)
	private Ciudad ciudad;
}
