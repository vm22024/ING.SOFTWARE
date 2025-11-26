package com.ues.edu.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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
