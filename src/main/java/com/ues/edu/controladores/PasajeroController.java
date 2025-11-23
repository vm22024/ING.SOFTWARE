package com.ues.edu.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ues.edu.modelo.Pasajero;
import com.ues.edu.servicios.IPasajeroService;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroController {

    @Autowired
    private IPasajeroService pasajeroService;

    @GetMapping
    public ResponseEntity<List<Pasajero>> listar() {
        return ResponseEntity.ok(pasajeroService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pasajero> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pasajeroService.leerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pasajero> guardar(@RequestBody Pasajero pasajero) {
        return ResponseEntity.ok(pasajeroService.guardar(pasajero));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pasajero> actualizar(@PathVariable Integer id, @RequestBody Pasajero pasajero) {
        pasajero.setIdPasajero(id);
        return ResponseEntity.ok(pasajeroService.guardar(pasajero));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pasajeroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
