package com.example.flotaapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flotaapp.ui.detail.DetalleVehiculoActivity;
import com.example.flotaapp.R;
import com.example.flotaapp.model.Vehiculo;

import java.util.List;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder> {
     // Lista de vehículos a mostrar y credenciales del usuario
    private List<Vehiculo> listaVehiculos;
    private String email;
    private String password;

         // Constructor que recibe la lista de vehículos y las credenciales
    public VehiculoAdapter(List<Vehiculo> listaVehiculos, String email, String password) {
        this.listaVehiculos = listaVehiculos;
        this.email = email;
        this.password = password;
    }
    // Crea el ViewHolder a partir del layout item_vehiculo
    @NonNull
    @Override
    public VehiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehiculo, parent, false);
        return new VehiculoViewHolder(vista);
    }
    // Asocia  datos del vehículo con los elementos visuales
    @Override
    public void onBindViewHolder(@NonNull VehiculoViewHolder holder, int position) {
        Vehiculo vehiculo = listaVehiculos.get(position);
        holder.textMarca.setText(vehiculo.getMarca());
        holder.textModelo.setText(vehiculo.getModelo());
        holder.textCombustible.setText("Combustible: " + vehiculo.getCombustible());
        holder.imageVehiculo.setImageResource(vehiculo.getImagenResId());
        // Pulsar click para abrir el detalle del vehículo y pasar información por intent
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetalleVehiculoActivity.class);
            intent.putExtra("id", vehiculo.getId());
            intent.putExtra("marca", vehiculo.getMarca());
            intent.putExtra("modelo", vehiculo.getModelo());
            intent.putExtra("matricula", vehiculo.getMatricula());
            intent.putExtra("anio", vehiculo.getAnio());
            intent.putExtra("combustible", vehiculo.getCombustible());
            intent.putExtra("imagenResId", vehiculo.getImagenResId());
            intent.putExtra("email", email); // ✅ añadido
            intent.putExtra("password", password); // ✅ añadido
            context.startActivity(intent);
        });
    }
    // Devuelve el número total de vehículos en la lista
    @Override
    public int getItemCount() {
        return listaVehiculos.size();
    }

    static class VehiculoViewHolder extends RecyclerView.ViewHolder {
        TextView textMarca, textModelo, textCombustible;
        ImageView imageVehiculo;
        // ViewHolder para mantener referencias a los elementos de la vista del vehículo
        public VehiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            textMarca = itemView.findViewById(R.id.textMarca);
            textModelo = itemView.findViewById(R.id.textModelo);
            textCombustible = itemView.findViewById(R.id.textCombustible);
            imageVehiculo = itemView.findViewById(R.id.imageVehiculo);
        }
    }
}
