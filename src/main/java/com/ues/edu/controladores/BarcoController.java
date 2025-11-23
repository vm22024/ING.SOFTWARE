package com.ues.edu.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ues.edu.modelo.Barco;
import com.ues.edu.servicios.IBarcoService;

@RestController
@RequestMapping("/api/barcos")
public class BarcoController {

    @Autowired
    private IBarcoService barcoService;

    @GetMapping
    public ResponseEntity<List<Barco>> listar() {
        return ResponseEntity.ok(barcoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Barco> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(barcoService.leerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Barco> guardar(@RequestBody Barco barco) {
        return ResponseEntity.ok(barcoService.guardar(barco));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barco> actualizar(@PathVariable Integer id, @RequestBody Barco barco) {
        barco.setIdBarco(id); 
        return ResponseEntity.ok(barcoService.guardar(barco));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        barcoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
