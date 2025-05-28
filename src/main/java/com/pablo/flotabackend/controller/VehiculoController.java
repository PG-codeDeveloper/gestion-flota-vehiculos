package com.pablo.flotabackend.controller;

import com.pablo.flotabackend.model.Vehiculo;
import com.pablo.flotabackend.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    //  GET: Obtener todos los vehículos
    @GetMapping
    public List<Vehiculo> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    // GET: Obtener vehículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable Long id) {
        Optional<Vehiculo> vehiculo = vehiculoRepository.findById(id);
        return vehiculo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  POST: Crear un nuevo vehículo
    @PostMapping
    public Vehiculo createVehiculo(@RequestBody Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    //  PUT: Actualizar vehículo por ID
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculoActualizado) {
        return vehiculoRepository.findById(id)
                .map(vehiculo -> {
                    vehiculo.setMarca(vehiculoActualizado.getMarca());
                    vehiculo.setModelo(vehiculoActualizado.getModelo());
                    vehiculo.setAnio(vehiculoActualizado.getAnio());
                    vehiculo.setMatricula(vehiculoActualizado.getMatricula());
                    return ResponseEntity.ok(vehiculoRepository.save(vehiculo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //  DELETE: Eliminar vehículo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Long id) {
        return vehiculoRepository.findById(id)
                .map(vehiculo -> {
                    vehiculoRepository.delete(vehiculo);
                    return ResponseEntity.noContent().<Void>build(); // 204 No Content
                })
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }
}
