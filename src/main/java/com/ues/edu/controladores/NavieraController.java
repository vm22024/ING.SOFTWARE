package com.ues.edu.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ues.edu.modelo.Naviera;
import com.ues.edu.servicios.INavieraService;

@RestController
@RequestMapping("/api/navieras")
public class NavieraController {

    @Autowired
    private INavieraService navieraService;

    @GetMapping
    public ResponseEntity<List<Naviera>> listar() {
        return ResponseEntity.ok(navieraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Naviera> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(navieraService.leerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Naviera> guardar(@RequestBody Naviera naviera) {
        return ResponseEntity.ok(navieraService.guardar(naviera));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Naviera> actualizar(@PathVariable Integer id, @RequestBody Naviera naviera) {
        naviera.setIdNaviera(id);
        return ResponseEntity.ok(navieraService.guardar(naviera));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        navieraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
