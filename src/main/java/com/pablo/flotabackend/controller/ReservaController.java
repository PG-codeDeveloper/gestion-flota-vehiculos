package com.pablo.flotabackend.controller;

import com.pablo.flotabackend.model.Reserva;
import com.pablo.flotabackend.model.Usuario;
import com.pablo.flotabackend.model.Vehiculo;
import com.pablo.flotabackend.model.HistorialUso;
import com.pablo.flotabackend.repository.ReservaRepository;
import com.pablo.flotabackend.repository.UsuarioRepository;
import com.pablo.flotabackend.repository.VehiculoRepository;
import com.pablo.flotabackend.repository.HistorialUsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private HistorialUsoRepository historialUsoRepository; // NUEVO

    //  Obtener todas las reservas (para administrador)
    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return ResponseEntity.ok(reservaRepository.findAll());
    }

    //  Obtener todas las reservas de un usuario por  ID
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> getReservasByUsuarioId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    List<Reserva> reservas = reservaRepository.findByUsuario(usuario);
                    return ResponseEntity.ok(reservas);
                })
                .orElse(ResponseEntity.notFound().build());
    }

                     //  Crear una nueva reserva con validación
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
        // Validaciones básicas
        if (reserva.getUsuario() == null || reserva.getUsuario().getId() == null) {
            return ResponseEntity.badRequest().body("Usuario es obligatorio");
        }
        if (reserva.getVehiculo() == null || reserva.getVehiculo().getId() == null) {
            return ResponseEntity.badRequest().body("Vehículo es obligatorio");
        }

        // Verifica que el usuario exista
        Usuario usuario = usuarioRepository.findById(reserva.getUsuario().getId()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.badRequest().body("El usuario no existe");
        }

        // Verifica que el vehículo exista
        Vehiculo vehiculo = vehiculoRepository.findById(reserva.getVehiculo().getId()).orElse(null);
        if (vehiculo == null) {
            return ResponseEntity.badRequest().body("El vehículo no existe");
        }

        // Asignar entidades completas antes de guardar
        reserva.setUsuario(usuario);
        reserva.setVehiculo(vehiculo);

        // Guardar la reserva
        Reserva nueva = reservaRepository.save(reserva);

        // Crear historial de uso asociado
        HistorialUso historial = new HistorialUso(usuario, vehiculo, reserva.getMotivo()); // Usamos constructor
        historialUsoRepository.save(historial);

        return ResponseEntity.ok(nueva);
    }

    //  Cancelar (eliminar) una reserva por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(reserva -> {
                    reservaRepository.delete(reserva);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
