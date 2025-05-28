package com.pablo.flotabackend.repository;

import com.pablo.flotabackend.model.HistorialUso;
import com.pablo.flotabackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialUsoRepository extends JpaRepository<HistorialUso, Long> {
    // Nuevo método para obtener historiales por usuario
    List<HistorialUso> findByUsuario(Usuario usuario);
}
