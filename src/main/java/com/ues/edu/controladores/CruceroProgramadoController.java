package com.ues.edu.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ues.edu.modelo.CruceroProgramado;
import com.ues.edu.servicios.ICruceroProgramadoService;

@RestController
@RequestMapping("/api/cruceros-programados")
public class CruceroProgramadoController {

    @Autowired
    private ICruceroProgramadoService cruceroProgramadoService;

    @GetMapping
    public ResponseEntity<List<CruceroProgramado>> listar() {
        return ResponseEntity.ok(cruceroProgramadoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CruceroProgramado> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cruceroProgramadoService.leerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CruceroProgramado> guardar(@RequestBody CruceroProgramado crucero) {
        return ResponseEntity.ok(cruceroProgramadoService.guardar(crucero));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CruceroProgramado> actualizar(@PathVariable Integer id, @RequestBody CruceroProgramado crucero) {
        crucero.setIdCrucero(id);
        return ResponseEntity.ok(cruceroProgramadoService.guardar(crucero));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cruceroProgramadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
