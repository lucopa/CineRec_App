package com.example.cinerec_app.Favoritas;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.ActualizarPeli.Actualizar_Peli;
import com.example.cinerec_app.Detalle.Detalle_Peli;
import com.example.cinerec_app.ListarPeli.Listar_Peli;
import com.example.cinerec_app.Objetos.Pelicula;
import com.example.cinerec_app.R;
import com.example.cinerec_app.ViewHolder.ViewHolder_Peli;
import com.example.cinerec_app.ViewHolder.ViewHolder_Peli_Favorita;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Pelis_Favoritas extends AppCompatActivity {

    RecyclerView RecyclerViewPelisFavoritas;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference Mis_Usuarios;
    DatabaseReference Notas_Favoritas;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    FirebaseRecyclerAdapter<Pelicula, ViewHolder_Peli_Favorita> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> firebaseRecyclerOptions;

    LinearLayoutManager linearLayoutManager;

    ImageView btnBack;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelis_archivadas);


        RecyclerViewPelisFavoritas = findViewById(R.id.RecyclerViewPelisFavoritas);
        RecyclerViewPelisFavoritas.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Mis_Usuarios = firebaseDatabase.getReference("Usuarios");
        Notas_Favoritas = firebaseDatabase.getReference("Mis peliculas favoritas");

        dialog = new Dialog(Pelis_Favoritas.this);

        initListener();
        ComprobarUsuario();


    }
    
    private void ComprobarUsuario(){
        if (user == null){
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        } else {
            ListarNotasImportantes();
        }
    }

    private void ListarNotasImportantes() {


        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Pelicula>().setQuery(Mis_Usuarios.child(user.getUid()).child("Mis peliculas favoritas").orderByChild("fecha_peli"), Pelicula.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pelicula, ViewHolder_Peli_Favorita>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Peli_Favorita ViewHolder_Peli_Favorita, int position, @NonNull Pelicula pelicula) {
                ViewHolder_Peli_Favorita.SetearDatos(
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
            public ViewHolder_Peli_Favorita onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.peli_favorita,parent,false);
                ViewHolder_Peli_Favorita ViewHolder_Peli_Favorita = new ViewHolder_Peli_Favorita(view);
                ViewHolder_Peli_Favorita.setOnClickListener(new ViewHolder_Peli_Favorita.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {



                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_peli = getItem(position).getId_peli();

                        //Declaramos las vistas
                        Button EliminarPeliImportante, CancelarPeliImportante;

                        //Realizamos la conexion con el diseño

                        dialog.setContentView(R.layout.cuadro_dialog_eliminar_peli_importante);

                        //Inicalizamos las vistas
                        EliminarPeliImportante = dialog.findViewById(R.id.EliminarPeliImportante);
                        CancelarPeliImportante = dialog.findViewById(R.id.CancelarPeliImportante);

                        EliminarPeliImportante.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(Pelis_Favoritas.this, "Pelicula eliminada", Toast.LENGTH_SHORT).show();
                                EliminarPeliFavorita(id_peli);
                                dialog.dismiss();

                            }
                        });

                        CancelarPeliImportante.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(Pelis_Favoritas.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                });

                return ViewHolder_Peli_Favorita;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Pelis_Favoritas.this,LinearLayoutManager.VERTICAL,false);


        RecyclerViewPelisFavoritas.setLayoutManager(linearLayoutManager);
        RecyclerViewPelisFavoritas.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarPeliFavorita(String id_peli){
        if (user == null){
            Toast.makeText(Pelis_Favoritas.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        } else {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis peliculas favoritas").child(id_peli)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Pelis_Favoritas.this, "La peli ya no es favorita", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Pelis_Favoritas.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    protected void onStart(){
        //es para que esta pendiente de que surja algun cambio
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
        super.onStart();
    }

    private void initListener() {
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

    }
}