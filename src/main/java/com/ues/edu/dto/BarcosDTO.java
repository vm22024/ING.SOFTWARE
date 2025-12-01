package com.ues.edu.dto;

import java.sql.Date;

public class BarcosDTO {
	private String barco;
	private Integer construccion;
	private String modelo;
	private String naviera;
	private String pais;

	// Constructores
	public BarcosDTO() {
	}

	public BarcosDTO(String barco, Integer construccion, String modelo, String naviera, String pais) {
		this.barco = barco;
		this.construccion = construccion;
		this.modelo = modelo;
		this.naviera = naviera;
		this.pais = pais;
	}

	// Getters y Setters
	public String getBarco() {
		return barco;
	}

	public void setBarco(String barco) {
		this.barco = barco;
	}

	public Integer getConstruccion() {
		return construccion;
	}

	public void setConstruccion(Integer construccion) {
		this.construccion = construccion;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNaviera() {
		return naviera;
	}

	public void setNaviera(String naviera) {
		this.naviera = naviera;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}
}
