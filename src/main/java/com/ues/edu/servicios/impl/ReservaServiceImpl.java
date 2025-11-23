package com.ues.edu.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ues.edu.modelo.CruceroProgramado;
import com.ues.edu.modelo.Reserva;
import com.ues.edu.repositorios.ICruceroProgramadoRepo;
import com.ues.edu.repositorios.IReservaRepo;
import com.ues.edu.servicios.IReservaService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReservaServiceImpl implements IReservaService {

	private final IReservaRepo reservaRepo;
	private final ICruceroProgramadoRepo cruceroRepo;

	public ReservaServiceImpl(IReservaRepo reservaRepo, ICruceroProgramadoRepo cruceroRepo) {
		this.reservaRepo = reservaRepo;
		this.cruceroRepo = cruceroRepo;
	}

	/**
	 * Guardar o actualizar una reserva. - Si es nueva, suma la cantidadPersonas al
	 * crucero. - Si es actualización, ajusta la diferencia entre la nueva y la
	 * antigua cantidadPersonas.
	 */
	@Override
	@Transactional
	public Reserva guardar(Reserva obj) {

		Reserva reservaExistente = null;
		boolean esActualizacion = obj.getIdReserva() != null;
//
		if (esActualizacion) {
			reservaExistente = reservaRepo.findById(obj.getIdReserva())
					.orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
		}

		// Guardar la reserva
		Reserva reservaGuardada = reservaRepo.save(obj);

		// Cargar el crucero completo desde la BD
		CruceroProgramado crucero = cruceroRepo.findById(reservaGuardada.getCrucero().getIdCrucero())
				.orElseThrow(() -> new RuntimeException("Crucero no encontrado"));

		int pasajerosActuales = crucero.getPasajerosRegistrados() == null ? 0 : crucero.getPasajerosRegistrados();

		if (esActualizacion) {
			int diferencia = reservaGuardada.getCantidadPersonas() - reservaExistente.getCantidadPersonas();
			crucero.setPasajerosRegistrados(pasajerosActuales + diferencia);
		} else {
			crucero.setPasajerosRegistrados(pasajerosActuales + reservaGuardada.getCantidadPersonas());
		}

		// Guardar crucero completo con todos sus campos
		cruceroRepo.save(crucero);

		return reservaGuardada;
	}

	@Override
	public List<Reserva> listar() {
		return reservaRepo.findAll();
	}

	@Override
	public Reserva leerPorId(Integer id) {
		return reservaRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
	}

	/**
	 * Elimina una reserva y ajusta el conteo de pasajeros en el crucero
	 */
	@Override
	@Transactional
	public boolean eliminar(Integer id) {
		Reserva reserva = reservaRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));

		CruceroProgramado crucero = reserva.getCrucero();
		int pasajerosActuales = crucero.getPasajerosRegistrados() == null ? 0 : crucero.getPasajerosRegistrados();

		// Restar la cantidad de la reserva eliminada
		crucero.setPasajerosRegistrados(pasajerosActuales - reserva.getCantidadPersonas());
		cruceroRepo.save(crucero);

		// Eliminar reserva
		reservaRepo.delete(reserva);

		return true;
	}
}
