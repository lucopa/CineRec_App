package com.example.cinerec_app.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.Objetos.Pelicula;
import com.example.cinerec_app.R;
import com.example.cinerec_app.ViewHolder.ViewHolder_Peli_Favorita;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PelisFavoritasFragment extends Fragment {

     RecyclerView RecyclerViewPelisFavoritas;
     DatabaseReference Mis_Usuarios;
     FirebaseAuth firebaseAuth;
     FirebaseUser user;
     FirebaseRecyclerAdapter<Pelicula, ViewHolder_Peli_Favorita> firebaseRecyclerAdapter;
     FirebaseRecyclerOptions<Pelicula> firebaseRecyclerOptions;
     Dialog dialog;
     ImageView btnBack;

    public PelisFavoritasFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pelis_favoritas, container, false);

        // Inicializar componentes
        RecyclerViewPelisFavoritas = view.findViewById(R.id.RecyclerViewPelisFavoritas);
        RecyclerViewPelisFavoritas.setHasFixedSize(true);

        btnBack = view.findViewById(R.id.btn_back);

        dialog = new Dialog(requireContext());
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Mis_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        initListener();
        ComprobarUsuario();

        return view;
    }

    private void ComprobarUsuario() {
        if (user == null) {
            Toast.makeText(requireContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        } else {
            ListarNotasImportantes();
        }
    }

    private void ListarNotasImportantes() {
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Pelicula>()
                .setQuery(Mis_Usuarios.child(user.getUid()).child("Mis peliculas favoritas").orderByChild("fecha_peli"), Pelicula.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pelicula, ViewHolder_Peli_Favorita>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Peli_Favorita viewHolder, int position, @NonNull Pelicula pelicula) {
                viewHolder.SetearDatos(
                        requireContext(),
                        pelicula.getId_peli(),
                        pelicula.getUid_usuario(),
                        pelicula.getCorreo_usuario(),
                        pelicula.getFecha_hora_actual(),
                        pelicula.getTitulo(),
                        pelicula.getDescripcion(),
                        pelicula.getFecha_peli(),
                        pelicula.getEstado()
                );
            }

            @NonNull
            @Override
            public ViewHolder_Peli_Favorita onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.peli_favorita, parent, false);
                ViewHolder_Peli_Favorita viewHolder = new ViewHolder_Peli_Favorita(view);

                viewHolder.setOnClickListener(new ViewHolder_Peli_Favorita.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Acción al hacer clic en el item (opcional)
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        String id_peli = getItem(position).getId_peli();
                        MostrarDialogoEliminar(id_peli);
                    }
                });

                return viewHolder;
            }
        };

        RecyclerViewPelisFavoritas.setLayoutManager(new LinearLayoutManager(requireContext()));
        RecyclerViewPelisFavoritas.setAdapter(firebaseRecyclerAdapter);
    }

    private void MostrarDialogoEliminar(String id_peli) {
        Button EliminarPeliImportante, CancelarPeliImportante;

        dialog.setContentView(R.layout.cuadro_dialog_eliminar_peli_importante);
        EliminarPeliImportante = dialog.findViewById(R.id.EliminarPeliImportante);
        CancelarPeliImportante = dialog.findViewById(R.id.CancelarPeliImportante);

        EliminarPeliImportante.setOnClickListener(v -> {
            EliminarPeliFavorita(id_peli);
            dialog.dismiss();
        });

        CancelarPeliImportante.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void EliminarPeliFavorita(String id_peli) {
        if (user == null) {
            Toast.makeText(requireContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis peliculas favoritas").child(id_peli)
                    .removeValue()
                    .addOnSuccessListener(unused -> Toast.makeText(requireContext(), "La peli ya no es favorita", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }
}
