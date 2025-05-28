package com.example.flotaapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flotaapp.R;
import com.example.flotaapp.ui.adapter.VehiculoAdapter;
import com.example.flotaapp.model.Vehiculo;
import com.example.flotaapp.retrofit.ApiService;
import com.example.flotaapp.ui.login.LoginActivity;

import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VehiculoAdapter vehiculoAdapter;
    private Button btnMisReservas;
    private TextView textCerrarSesion;

    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recuperar las credenciales que  pasó LoginActivity
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        // Setup RecyclerView de vehículos
        recyclerView = findViewById(R.id.recyclerUsuarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Botón “Mis Reservas”
        btnMisReservas = findViewById(R.id.btnMisReservas);
        btnMisReservas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MisReservasActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        });

        // Cargar el catálogo desde el backend
        cargarVehiculosDesdeApi();

        //  TextView "Cerrar sesión"
        textCerrarSesion = findViewById(R.id.textCerrarSesion);
        textCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void cargarVehiculosDesdeApi() {
        String auth = Credentials.basic(email, password);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", auth)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getVehiculos().enqueue(new Callback<List<Vehiculo>>() {
            @Override
            public void onResponse(Call<List<Vehiculo>> call, Response<List<Vehiculo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Vehiculo> vehiculos = response.body();
                    for (Vehiculo v : vehiculos) {
                        v.setImagenResId(obtenerImagenPorModelo(v.getModelo()));
                    }
                    vehiculoAdapter = new VehiculoAdapter(vehiculos, email, password);
                    recyclerView.setAdapter(vehiculoAdapter);
                } else {
                    Toast.makeText(MainActivity.this, "Error al cargar vehículos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vehiculo>> call, Throwable t) {
                Log.e("MainActivity", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(MainActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int obtenerImagenPorModelo(String modelo) {
        switch (modelo) {
            case "Focus ST-Line": return R.drawable.ford_focus_stline;
            case "Corolla":        return R.drawable.toyota_corolla;
            case "308":            return R.drawable.peugeot_308;
            case "Serie 1":        return R.drawable.bmw_serie1;
            case "Clio":           return R.drawable.renault_clio;
            default:               return R.drawable.ic_launcher_background;
        }
    }
}
