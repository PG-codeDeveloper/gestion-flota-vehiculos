package com.example.flotaapp.model;

import com.example.flotaapp.R;

public class Vehiculo {
    private Long id;
    private String marca;
    private String modelo;
    private String matricula;
    private int anio;
    private String combustible;
    private int imagenResId;

    // Constructor vacío
    public Vehiculo() {
    }

    // Constructor completo
    public Vehiculo(Long id, String marca, String modelo, int anio, String matricula, String combustible, int imagenResId) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.matricula = matricula;
        this.combustible = combustible;
        this.imagenResId = imagenResId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getCombustible() { return combustible; }
    public void setCombustible(String combustible) { this.combustible = combustible; }

    public int getImagenResId() { return imagenResId; }
    public void setImagenResId(int imagenResId) { this.imagenResId = imagenResId; }

    // Métodos estático para asignar imagen local según modelo
    public static int obtenerImagenPorModelo(String modelo) {
        if (modelo == null) return R.drawable.ic_launcher_background;

        switch (modelo.toLowerCase()) {
            case "focus st-line":
                return R.drawable.ford_focus_stline;
            case "corolla":
                return R.drawable.toyota_corolla;
            case "308":
                return R.drawable.peugeot_308;
            case "serie 1":
                return R.drawable.bmw_serie1;
            case "clio":
                return R.drawable.renault_clio;
            default:
                return R.drawable.ic_launcher_background;
        }
    }
}
