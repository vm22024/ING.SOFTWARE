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
public class CruceroProgramado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCrucero;

	@ManyToOne
	@JoinColumn(name = "idBarco", nullable = false)
	private Barco barco;

	@ManyToOne
	@JoinColumn(name = "idPuertoOrigen", nullable = false)
	private Puerto puertoOrigen;

	@ManyToOne
	@JoinColumn(name = "idPuertoDestino", nullable = false)
	private Puerto puertoDestino;

	private java.sql.Date fechaSalida;
	private java.sql.Date fechaRegreso;

	private Integer pasajerosRegistrados;
	private Integer camarotesDisponibles;
}
