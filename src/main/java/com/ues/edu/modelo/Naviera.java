package com.ues.edu.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Naviera {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idNaviera;

	@Column(nullable = false)
	private String nombre;

	private String pais;
}
