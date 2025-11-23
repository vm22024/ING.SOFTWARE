package com.ues.edu.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ues.edu.modelo.Puerto;
import com.ues.edu.servicios.IPuertoService;

@RestController
@RequestMapping("/api/puertos")
public class PuertoController {

    @Autowired
    private IPuertoService puertoService;

    @GetMapping
    public ResponseEntity<List<Puerto>> listar() {
        return ResponseEntity.ok(puertoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Puerto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(puertoService.leerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Puerto> guardar(@RequestBody Puerto puerto) {
        return ResponseEntity.ok(puertoService.guardar(puerto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Puerto> actualizar(@PathVariable Integer id, @RequestBody Puerto puerto) {
        puerto.setIdPuerto(id);
        return ResponseEntity.ok(puertoService.guardar(puerto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        puertoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
