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
public class Barco {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBarco;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "idNaviera", nullable = false)
    private Naviera naviera;

    @ManyToOne
    @JoinColumn(name = "idModelo", nullable = false)
    private ModeloBarco modelo;

    private Integer anioConstruccion;
}
