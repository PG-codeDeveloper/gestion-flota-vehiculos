package com.example.flotaapp.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flotaapp.R;
import com.example.flotaapp.model.Reserva;
import com.example.flotaapp.retrofit.ApiService;
import com.example.flotaapp.ui.adapter.ReservaAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Muestra los detalles de un usuario específico (modo administrador)

public class DetalleUsuarioActivity extends AppCompatActivity
        implements ReservaAdapter.OnReservaDeletedListener {

    private TextView textNombre, textEmail, textRol;
    private RecyclerView recyclerReservas;
    private Button btnVolver;
    private ReservaAdapter reservaAdapter;
    private final List<Reserva> listaReservas = new ArrayList<>();

    private String email, password;
    private long usuarioId;
    private static final String BASE_URL = "http://10.0.2.2:8080/";


        // Referencias a elementos visuales
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_usuario);

        textNombre       = findViewById(R.id.textNombreUsuario);
        textEmail        = findViewById(R.id.textEmailUsuario);
        textRol          = findViewById(R.id.textRolUsuario);
        recyclerReservas = findViewById(R.id.recyclerReservas);
        btnVolver        = findViewById(R.id.btnVolver);

        // Recuperar datos del intent: credenciales y datos del usuario seleccionado

        email       = getIntent().getStringExtra("email");
        password    = getIntent().getStringExtra("password");
        usuarioId   = getIntent().getLongExtra("usuarioId", 0);

        String nombre       = getIntent().getStringExtra("nombre");
        String emailUsuario = getIntent().getStringExtra("emailUsuario");
        String rolUsuario   = getIntent().getStringExtra("rol");

        // Mostrar los datos del usuario

        textNombre.setText("Nombre: " + nombre);
        textEmail .setText("Email: "  + emailUsuario);
        textRol   .setText("Rol: "    + rolUsuario);

        // Recycler + Adapter
        recyclerReservas.setLayoutManager(new LinearLayoutManager(this));
        reservaAdapter = new ReservaAdapter(
                listaReservas,
                email,
                password,
                this
        );
        recyclerReservas.setAdapter(reservaAdapter);

        btnVolver.setOnClickListener(v -> finish());

        cargarReservasDeUsuario();
    }

                // Metodo que obtiene las reservas del usuario a través de Retrofit
    private void cargarReservasDeUsuario() {
        String auth = Credentials.basic(email, password);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request req = chain.request().newBuilder()
                            .header("Authorization", auth)
                            .build();
                    return chain.proceed(req);
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);
        api.getReservasByUsuario(usuarioId).enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    listaReservas.clear();
                    listaReservas.addAll(resp.body());
                    reservaAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DetalleUsuarioActivity.this,
                            "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                    Log.e("DetalleUsuario", "Código: " + resp.code());
                }
            }
            @Override
            public void onFailure(Call<List<Reserva>> call, Throwable t) {
                Toast.makeText(DetalleUsuarioActivity.this,
                        "Fallo de conexión", Toast.LENGTH_SHORT).show();
                Log.e("DetalleUsuario", "Error: " + t.getMessage());
            }
        });
    }

    // Metodo que se llama cuando se elimina una reserva desde el adaptador
    @Override
    public void onReservaDeleted(int position) {
        listaReservas.remove(position);
        reservaAdapter.notifyItemRemoved(position);
    }
}
