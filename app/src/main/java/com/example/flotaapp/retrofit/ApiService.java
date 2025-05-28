package com.example.flotaapp.retrofit;

import com.example.flotaapp.model.Usuario;
import com.example.flotaapp.model.Reserva;
import com.example.flotaapp.model.Vehiculo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

        //  Usuarios

    // Devuelve el usuario autenticado (usado en login)
    @GET("/api/usuarios/me")
    Call<Usuario> getUsuarioActual();

    // Lista todos los usuarios (solo ADMIN)
    @GET("/api/usuarios")
    Call<List<Usuario>> getUsuarios();

    // Obtiene un usuario por su ID
    @GET("/api/usuarios/{id}")
    Call<Usuario> getUsuarioById(@Path("id") long id);

    // Crea un nuevo usuario (registro)
    @POST("/api/usuarios")
    Call<Usuario> crearUsuario(@Body Usuario usuario);


        // Reservas

    // Crea una nueva reserva
    @POST("/api/reservas")
    Call<Reserva> crearReserva(@Body Reserva reserva);

    //Lista todas las reservas (ADMIN)
    @GET("/api/reservas")
    Call<List<Reserva>> getReservas();

    //Lista reservas de un usuario específico
    @GET("/api/reservas/usuario/{id}")
    Call<List<Reserva>> getReservasByUsuario(@Path("id") long usuarioId);

    // Elimina (cancela) una reserva por su ID
    @DELETE("/api/reservas/{id}")
    Call<Void> deleteReserva(@Path("id") long reservaId);


             //  Vehículos

    // Lista todos los vehículos disponibles
    @GET("/api/vehiculos")
    Call<List<Vehiculo>> getVehiculos();
}
