package com.pablo.flotabackend.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "historial_uso")
public class HistorialUso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "fecha_uso", nullable = false)
    private ZonedDateTime fechaUso;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    // Constructor vacío
    public HistorialUso() {}

    // Constructor con parámetros
    public HistorialUso(Usuario usuario, Vehiculo vehiculo, String descripcion) {
        this.usuario = usuario;
        this.vehiculo = vehiculo;
        this.fechaUso = ZonedDateTime.now();
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public ZonedDateTime getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(ZonedDateTime fechaUso) {
        this.fechaUso = fechaUso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @PrePersist
    public void prePersist() {
        // Asegurarse de establecer la fecha actual justo antes de guardar
        if (this.fechaUso == null) {
            this.fechaUso = ZonedDateTime.now();
        }
    }
}
