package com.ues.edu.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ues.edu.modelo.Ciudad;
import com.ues.edu.servicios.ICiudadService;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {

    @Autowired
    private ICiudadService ciudadService;

    @GetMapping
    public ResponseEntity<List<Ciudad>> listar() {
        return ResponseEntity.ok(ciudadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ciudadService.leerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Ciudad> guardar(@RequestBody Ciudad ciudad) {
        return ResponseEntity.ok(ciudadService.guardar(ciudad));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> actualizar(@PathVariable Integer id, @RequestBody Ciudad ciudad) {
        ciudad.setIdCiudad(id);
        return ResponseEntity.ok(ciudadService.guardar(ciudad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ciudadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
