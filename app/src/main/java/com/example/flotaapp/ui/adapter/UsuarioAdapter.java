package com.example.flotaapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flotaapp.R;
import com.example.flotaapp.model.Usuario;
import com.example.flotaapp.ui.detail.DetalleUsuarioActivity;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {
    // Muestra una lista de usuarios y credenciales del usuario actual
    private final List<Usuario> listaUsuarios;
    private final String email, password;

    // Constructor que recibe la lista de usuarios y las credenciales
    public UsuarioAdapter(List<Usuario> listaUsuarios, String email, String password) {
        this.listaUsuarios = listaUsuarios;
        this.email = email;
        this.password = password;
    }

    // Crea y devuelve un nuevo ViewHolder con el layout de item usuario
    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(vista);
    }

    // Asocia los datos del usuario con los elementos visuales del item
    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.textNombre.setText("Nombre: " + usuario.getNombre());
        holder.textEmail.setText("Email: " + usuario.getEmail());
        holder.textRol.setText("Rol: " + usuario.getRol());

        // Configura el click para abrir DetalleUsuarioActivity, pasando datos por intent

        holder.itemView.setOnClickListener(v -> {
            Context ctx = v.getContext();
            Intent intent = new Intent(ctx, DetalleUsuarioActivity.class);
            intent.putExtra("usuarioId", usuario.getId());
            intent.putExtra("nombre", usuario.getNombre());
            intent.putExtra("emailUsuario", usuario.getEmail());
            intent.putExtra("rolUsuario", usuario.getRol());
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textEmail, textRol;
        UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            textEmail  = itemView.findViewById(R.id.textEmail);
            textRol    = itemView.findViewById(R.id.textRol);
        }
    }
}
