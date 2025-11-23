package com.ues.edu.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ues.edu.modelo.ModeloBarco;
import com.ues.edu.servicios.IModeloBarcoService;

@RestController
@RequestMapping("/api/modelos-barco")
public class ModeloBarcoController {

    @Autowired
    private IModeloBarcoService modeloBarcoService;

    @GetMapping
    public ResponseEntity<List<ModeloBarco>> listar() {
        return ResponseEntity.ok(modeloBarcoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloBarco> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(modeloBarcoService.leerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ModeloBarco> guardar(@RequestBody ModeloBarco modelo) {
        return ResponseEntity.ok(modeloBarcoService.guardar(modelo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeloBarco> actualizar(@PathVariable Integer id, @RequestBody ModeloBarco modelo) {
        modelo.setIdModelo(id);
        return ResponseEntity.ok(modeloBarcoService.guardar(modelo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        modeloBarcoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
