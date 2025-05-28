package com.example.flotaapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flotaapp.R;
import com.example.flotaapp.model.Reserva;
import com.example.flotaapp.model.Usuario;
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

public class MisReservasActivity extends AppCompatActivity
        implements ReservaAdapter.OnReservaDeletedListener {

    private RecyclerView recyclerMisReservas;
    private String email, password;
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);

        recyclerMisReservas = findViewById(R.id.recyclerMisReservas);
        recyclerMisReservas.setLayoutManager(new LinearLayoutManager(this));

        email    = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        if (email == null || password == null) {
            Toast.makeText(this, "Credenciales no recibidas", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        findViewById(R.id.btnVolverCatalogo).setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("email", email);
            i.putExtra("password", password);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });

        fetchUsuarioYReservas();
    }

    private void fetchUsuarioYReservas() {
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
        api.getUsuarioActual().enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    long userId = resp.body().getId();
                    loadReservas(userId, api);
                } else {
                    Toast.makeText(MisReservasActivity.this,
                            "No se pudo obtener usuario", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(MisReservasActivity.this,
                        "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadReservas(long userId, ApiService api) {
        api.getReservasByUsuario(userId).enqueue(new Callback<List<Reserva>>() {
            @Override
            public void onResponse(Call<List<Reserva>> call, Response<List<Reserva>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    ReservaAdapter adapter = new ReservaAdapter(
                            resp.body(),
                            email,
                            password,
                            MisReservasActivity.this
                    );
                    recyclerMisReservas.setAdapter(adapter);
                } else {
                    Toast.makeText(MisReservasActivity.this,
                            "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Reserva>> call, Throwable t) {
                Toast.makeText(MisReservasActivity.this,
                        "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onReservaDeleted(int position) {

        Toast.makeText(this, "Reserva cancelada", Toast.LENGTH_SHORT).show();
    }
}
