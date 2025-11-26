package com.ues.edu.modelo;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @ManyToOne
    @JoinColumn(name = "idCrucero", nullable = false)
    private CruceroProgramado crucero;

    @ManyToOne
    @JoinColumn(name = "idPasajero", nullable = false)
    private Pasajero pasajero;

    private Integer cantidadPersonas;
    private Integer cantidadCamarotes;

    private LocalDate fechaReserva;

}
