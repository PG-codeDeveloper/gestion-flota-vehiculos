package com.pablo.flotabackend.controller;

import com.pablo.flotabackend.model.HistorialUso;
import com.pablo.flotabackend.model.Usuario;
import com.pablo.flotabackend.repository.HistorialUsoRepository;
import com.pablo.flotabackend.repository.UsuarioRepository;
import com.pablo.flotabackend.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialUsoController {

    @Autowired
    private HistorialUsoRepository historialUsoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    // GET: Listar todos los historiales
    @GetMapping
    public List<HistorialUso> getAllHistorial() {
        return historialUsoRepository.findAll();
    }

    // GET: Obtener un historial por ID
    @GetMapping("/{id}")
    public ResponseEntity<HistorialUso> getHistorialUsoById(@PathVariable Long id) {
        return historialUsoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET: Obtener historial por ID de usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistorialUso>> getHistorialByUsuario(@PathVariable Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> ResponseEntity.ok(historialUsoRepository.findByUsuario(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear un historial manualmente (opcional)
    @PostMapping
    public ResponseEntity<HistorialUso> createHistorial(@RequestBody HistorialUso historialUso) {
        // Se puede validar aquí si el usuario o vehículo existen
        return ResponseEntity.ok(historialUsoRepository.save(historialUso));
    }

    // DELETE: Eliminar historial por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHistorial(@PathVariable Long id) {
        if (historialUsoRepository.existsById(id)) {
            historialUsoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
