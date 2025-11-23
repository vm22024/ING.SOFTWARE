package com.ues.edu.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ues.edu.modelo.Reserva;

public interface IReservaRepo extends JpaRepository<Reserva, Integer>{

}
