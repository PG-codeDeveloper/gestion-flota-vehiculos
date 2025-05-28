package com.example.flotaapp.ui.detail;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flotaapp.R;
import com.example.flotaapp.model.Reserva;
import com.example.flotaapp.model.Usuario;
import com.example.flotaapp.model.Vehiculo;
import com.example.flotaapp.retrofit.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalleVehiculoActivity extends AppCompatActivity {

    private TextView textMarca, textModelo, textMatricula, textAnio, textCombustible;
    private ImageView imageVehiculo;
    private EditText editTextMotivo;
    private Button btnVolver, btnReservar;

    private String usuarioEmail;
    private String usuarioPassword;
    private Long vehiculoId;

    private static final String BASE_URL = "http://10.0.2.2:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_vehiculo);

        // Asocia las vistas con los elementos del layout

        textMarca = findViewById(R.id.textDetalleMarca);
        textModelo = findViewById(R.id.textDetalleModelo);
        textMatricula = findViewById(R.id.textDetalleMatricula);
        textAnio = findViewById(R.id.textDetalleAnio);
        textCombustible = findViewById(R.id.textDetalleCombustible);
        imageVehiculo = findViewById(R.id.imageDetalleVehiculo);
        editTextMotivo = findViewById(R.id.editTextMotivo);
        btnVolver = findViewById(R.id.btnVolver);
        btnReservar = findViewById(R.id.btnReservar);

          // Obtiene datos del intent (vehículo seleccionado y credenciales del usuario)
        vehiculoId = getIntent().getLongExtra("id", 0);
        String marca = getIntent().getStringExtra("marca");
        String modelo = getIntent().getStringExtra("modelo");
        String matricula = getIntent().getStringExtra("matricula");
        String combustible = getIntent().getStringExtra("combustible");
        int anio = getIntent().getIntExtra("anio", 0);
        int imagenResId = getIntent().getIntExtra("imagenResId", R.drawable.ic_launcher_background);
        usuarioEmail = getIntent().getStringExtra("email");
        usuarioPassword = getIntent().getStringExtra("password");

              // Mostrar datos vehiculo

        textMarca.setText("Marca: " + marca);
        textModelo.setText("Modelo: " + modelo);
        textMatricula.setText("Matrícula: " + matricula);
        textAnio.setText("Año: " + anio);
        textCombustible.setText("Combustible: " + combustible);
        imageVehiculo.setImageResource(imagenResId);

        btnVolver.setOnClickListener(v -> finish());
        btnReservar.setOnClickListener(v -> hacerReservaConUsuario());
    }
        // Metodo que gestiona la creación de la reserva con Retrofit
    private void hacerReservaConUsuario() {
        String motivo = editTextMotivo.getText().toString().trim();
        if (motivo.isEmpty()) {
            Toast.makeText(this, "Por favor introduce un motivo para la reserva", Toast.LENGTH_SHORT).show();
            return;
        }

        String credentials = Credentials.basic(usuarioEmail, usuarioPassword);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", credentials)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService api = retrofit.create(ApiService.class);

        // Obtiene el usuario actual con las credenciales y crea una reserva

        api.getUsuarioActual().enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();

                    // Prepara la reserva con los datos

                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setId(vehiculoId);

                    Reserva reserva = new Reserva();
                    reserva.setMotivo(motivo);
                    reserva.setFecha(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    reserva.setUsuario(usuario);
                    reserva.setVehiculo(vehiculo);

                    // Enviar la reserva al backend

                    api.crearReserva(reserva).enqueue(new Callback<Reserva>() {
                        @Override
                        public void onResponse(Call<Reserva> call, Response<Reserva> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(DetalleVehiculoActivity.this, "Reserva realizada correctamente", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(DetalleVehiculoActivity.this, "Error al reservar", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Reserva> call, Throwable t) {
                            Toast.makeText(DetalleVehiculoActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(DetalleVehiculoActivity.this, "No se pudo obtener el usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(DetalleVehiculoActivity.this, "Fallo al obtener el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
