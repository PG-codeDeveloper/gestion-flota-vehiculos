package com.pablo.flotabackend.repository;

import com.pablo.flotabackend.model.Reserva;
import com.pablo.flotabackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Obtener todas las reservas asociadas a un usuario
    List<Reserva> findByUsuario(Usuario usuario);
}
