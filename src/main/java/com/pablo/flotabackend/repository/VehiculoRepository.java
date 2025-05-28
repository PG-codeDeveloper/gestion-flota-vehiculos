package com.pablo.flotabackend.repository;

import com.pablo.flotabackend.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
}
