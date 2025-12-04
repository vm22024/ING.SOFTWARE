package com.ues.edu.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
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

    private Integer pasajerosRegistrados = 0;
    private Integer camarotesDisponibles;

    @PostLoad
    @PostPersist
    @PostUpdate
    public void calcularCamarotesDisponibles() {
        if (this.barco != null && this.barco.getModelo() != null) {
            int capacidadTotal = this.barco.getModelo().getCapacidadPasajeros();
            int camarotesTotales = capacidadTotal / 2; 
            this.camarotesDisponibles = camarotesTotales - (this.pasajerosRegistrados / 2);
        }
    }

    public void agregarPasajeros(int cantidad) {
        this.pasajerosRegistrados += cantidad;
        calcularCamarotesDisponibles();
    }

    public void quitarPasajeros(int cantidad) {
        this.pasajerosRegistrados = Math.max(0, this.pasajerosRegistrados - cantidad);
        calcularCamarotesDisponibles();
    }
}