package com.ues.edu.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ues.edu.modelo.Reserva;
import com.ues.edu.servicios.IReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {
        return ResponseEntity.ok(reservaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(reservaService.leerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Reserva> guardar(@RequestBody Reserva reserva) {
        return ResponseEntity.ok(reservaService.guardar(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(@PathVariable Integer id, @RequestBody Reserva reserva) {
        reserva.setIdReserva(id);
        return ResponseEntity.ok(reservaService.guardar(reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
