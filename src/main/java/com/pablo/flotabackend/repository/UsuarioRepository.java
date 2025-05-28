package com.pablo.flotabackend.repository;

import com.pablo.flotabackend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //  Comprueba si un email ya existe en la base de datos
    boolean existsByEmail(String email);

    // Obtiene un usuario por su email
    Optional<Usuario> findByEmail(String email);
}
