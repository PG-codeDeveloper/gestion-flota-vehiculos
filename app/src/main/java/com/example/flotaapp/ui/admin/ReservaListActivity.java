package com.example.flotaapp.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flotaapp.R;
import com.example.flotaapp.model.Reserva;
import com.example.flotaapp.retrofit.ApiService;
import com.example.flotaapp.ui.adapter.ReservaAdapter;

import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservaListActivity extends AppCompatActivity
        implements ReservaAdapter.OnReservaDeletedListener {

    private RecyclerView recyclerReservas;
    private String email, password;
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_list);

        // Configura el RecyclerView para mostrar las reservas

        recyclerReservas = findViewById(R.id.recyclerReservas);
        recyclerReservas.setLayoutManager(new LinearLayoutManager(this));

        // Recupera las credenciales del intent

        email    = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        if (email == null || password == null) {
            Toast.makeText(this, "Credenciales no recibidas", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarReservas();

        // Configurar botón de volver
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(ReservaListActivity.this, AdminActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        });
    }


    // Metodo para cargar reservas desde el backend usando Retrofit y mostrar en el RecyclerView
    private void cargarReservas() {
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
        api.getReservas().enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReservaAdapter adapter = new ReservaAdapter(
                            response.body(),
                            email,
                            password,
                            ReservaListActivity.this
                    );
                    recyclerReservas.setAdapter(adapter);
                } else {
                    Toast.makeText(ReservaListActivity.this,
                            "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Reserva>> call, Throwable t) {
                Toast.makeText(ReservaListActivity.this,
                        "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Metodo llamado cuando se elimina una reserva desde el adaptador
    @Override
    public void onReservaDeleted(int position) {

        cargarReservas();
    }
}
