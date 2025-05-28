package com.example.flotaapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flotaapp.R;
import com.example.flotaapp.model.Reserva;
import com.example.flotaapp.retrofit.ApiService;

import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    public interface OnReservaDeletedListener {
        void onReservaDeleted(int position);
    }

    private final List<Reserva> reservas;
    private final String email, password;
    private final OnReservaDeletedListener listener;
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    public ReservaAdapter(List<Reserva> reservas,
                          String email,
                          String password,
                          OnReservaDeletedListener listener) {
        this.reservas = reservas;
        this.email = email;
        this.password = password;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva r = reservas.get(position);
        holder.textVehiculo .setText(r.getVehiculo().getMarca() + " " + r.getVehiculo().getModelo());
        holder.textUsuario  .setText(r.getUsuario().getNombre());
        holder.textMotivo   .setText(r.getMotivo());
        holder.textFecha    .setText(r.getFecha());

        holder.btnEliminar.setOnClickListener(v -> { // Obtiene la posición actual del elemento en el RecyclerView
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) { // Verifica que la posición es válida (que el ítem no ha sido eliminado)
                Reserva reservaActual = reservas.get(currentPosition);   // Recupera la reserva correspondiente a esta posición

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

                ApiService api = retrofit.create(ApiService.class);
                api.deleteReserva(reservaActual.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            listener.onReservaDeleted(currentPosition);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Opcional: mostrar un Toast o log de error
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView textVehiculo, textUsuario, textMotivo, textFecha;
        ImageButton btnEliminar;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            textVehiculo  = itemView.findViewById(R.id.textVehiculo);
            textUsuario   = itemView.findViewById(R.id.textUsuario);
            textMotivo    = itemView.findViewById(R.id.textMotivo);
            textFecha     = itemView.findViewById(R.id.textFecha);
            btnEliminar   = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
