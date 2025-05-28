package com.example.flotaapp.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flotaapp.R;
import com.example.flotaapp.model.Usuario;
import com.example.flotaapp.retrofit.ApiService;
import com.example.flotaapp.ui.adapter.UsuarioAdapter;

import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsuarioListActivity extends AppCompatActivity {

    private RecyclerView recyclerUsuarios;
    private String email, password;
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_list);

        recyclerUsuarios = findViewById(R.id.recyclerUsuarios);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this));

        // Recuperar siempre con las mismas claves
        email    = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        if (email == null || password == null) {
            Toast.makeText(this, "Credenciales no recibidas", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar usuarios
        cargarUsuarios();

        // Configurar botón de volver

        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioListActivity.this, AdminActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        });
    }

    private void cargarUsuarios() {
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
        api.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    UsuarioAdapter adapter = new UsuarioAdapter(
                            resp.body(),
                            email,
                            password
                    );
                    recyclerUsuarios.setAdapter(adapter);
                } else {
                    Toast.makeText(UsuarioListActivity.this,
                            "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(UsuarioListActivity.this,
                        "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
