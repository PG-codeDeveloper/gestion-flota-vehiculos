package com.example.flotaapp.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.flotaapp.R;
import com.example.flotaapp.ui.login.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    private AppCompatButton btnVerUsuarios, btnVerReservas;
    private TextView textCerrarSesion;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Recuperación de referencias de los botones y textos del layout

        btnVerUsuarios   = findViewById(R.id.btnVerUsuarios);
        btnVerReservas   = findViewById(R.id.btnVerReservas);
        textCerrarSesion = findViewById(R.id.textCerrarSesion);

        //  Credenciales del Intent por pantalla login

        email    = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        //  Navegar a lista de usuarios

        btnVerUsuarios.setOnClickListener(v -> {
            Intent i = new Intent(this, UsuarioListActivity.class);
            i.putExtra("email", email);
            i.putExtra("password", password);
            startActivity(i);
        });

        // Navegar a lista de reservas

        btnVerReservas.setOnClickListener(v -> {
            Intent i = new Intent(this, ReservaListActivity.class);
            i.putExtra("email", email);
            i.putExtra("password", password);
            startActivity(i);
        });

        //  Cerrar sesión  Login y limpiar backstack

        textCerrarSesion.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}
