package com.ues.edu.dto;

import java.sql.Date;
import java.time.LocalDate;

public class ReservasDTO {

	private Date fechaSalida;
	private String barco;
	private String puertoSalida;
	private Date fechaRegreso;
	private Integer camarotesDisponibles;
	private Long totalReservas;
	
	
	 // Constructor
    public ReservasDTO(Date fechaSalida, String barco, String puertoSalida, 
    		Date fechaRegreso, Integer camarotesDisponibles, Long totalReservas) {
        this.fechaSalida = fechaSalida;
        this.barco = barco;
        this.puertoSalida = puertoSalida;
        this.fechaRegreso = fechaRegreso;
        this.camarotesDisponibles = camarotesDisponibles;
        this.totalReservas = totalReservas;
    }

    // Getters explícitos que coincidan con el reporte
    public Date getFechaSalida() { return fechaSalida; }
    public String getBarco() { return barco; }
    public String getPuertoSalida() { return puertoSalida; }
    public Date getFechaRegreso() { return fechaRegreso; }
    public Integer getCamarotesDisponibles() { return camarotesDisponibles; }
    public Long getTotalReservas() { return totalReservas; }

    // Setters
    public void setFechaSalida(Date fechaSalida) { this.fechaSalida = fechaSalida; }
    public void setBarco(String barco) { this.barco = barco; }
    public void setPuertoSalida(String puertoSalida) { this.puertoSalida = puertoSalida; }
    public void setFechaRegreso(Date fechaRegreso) { this.fechaRegreso = fechaRegreso; }
    public void setCamarotesDisponibles(Integer camarotesDisponibles) { this.camarotesDisponibles = camarotesDisponibles; }
    public void setTotalReservas(Long totalReservas) { this.totalReservas = totalReservas; }
}
