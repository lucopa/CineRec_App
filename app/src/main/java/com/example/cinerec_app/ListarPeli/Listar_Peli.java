package com.example.cinerec_app.ListarPeli;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.Objetos.Pelicula;
import com.example.cinerec_app.ViewHolder.ViewHolder_Peli;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.example.cinerec_app.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Listar_Peli extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView recyclerviewPelis;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Pelicula, ViewHolder_Peli> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> options;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_peli);

        recyclerviewPelis = findViewById(R.id.recyclerviewPelis);
        recyclerviewPelis.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("Peliculas_Publicadas");
        dialog = new Dialog(Listar_Peli.this);
        ListarPelisUsuarios();
        initListener();


    }

    private void initListener() {
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());



    }

    private void ListarPelisUsuarios(){
        options = new FirebaseRecyclerOptions.Builder<Pelicula>().setQuery(BASE_DE_DATOS, Pelicula.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pelicula, ViewHolder_Peli>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Peli viewHolder_peli, int position, @NonNull Pelicula pelicula) {
                viewHolder_peli.SetearDatos(
                        getApplicationContext(),
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
            public ViewHolder_Peli onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_peli,parent,false);
                ViewHolder_Peli viewHolder_peli = new ViewHolder_Peli(view);
                viewHolder_peli.setOnClickListener(new ViewHolder_Peli.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(Listar_Peli.this, "On item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_peli = getItem(position).getId_peli();

                        //Declaramos las vistas
                        Button cd_eliminar, cd_actualizar;

                        //conexion con el diseño
                        dialog.setContentView(R.layout.dialogo_opciones);

                        //incializamos las vistas
                        cd_eliminar = dialog.findViewById(R.id.cd_eliminar);
                        cd_actualizar = dialog.findViewById(R.id.cd_actualizar);

                        cd_eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EliminarPeli(id_peli);
                                dialog.dismiss();

                            }
                        });

                        cd_actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(Listar_Peli.this, "Pelicula actualizada", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        });
                                dialog.show();
                       // Toast.makeText(Listar_Peli.this, "on item long click", Toast.LENGTH_SHORT).show();
                    }
                });

                return viewHolder_peli;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Listar_Peli.this,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerviewPelis.setLayoutManager(linearLayoutManager);
        recyclerviewPelis.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarPeli(String idPeli) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Peli.this);
        builder.setTitle("Eliminar pelicula");
        builder.setMessage("¿Deseas eliminar la pelicula?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Eliminar nota en bd
                Query query = BASE_DE_DATOS.orderByChild("id_peli").equalTo(idPeli);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Peli.this, "Pelicula Eliminada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Listar_Peli.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Listar_Peli.this, "Has cancelado la acción", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();

        }
    }
}