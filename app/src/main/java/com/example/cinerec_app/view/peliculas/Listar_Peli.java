package com.example.cinerec_app.view.peliculas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.model.Pelicula;
import com.example.cinerec_app.presenter.ViewHolder.ViewHolder_Peli;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.example.cinerec_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Listar_Peli extends AppCompatActivity {

    ImageView btnBack, vaciar_todo, filtrar;
    RecyclerView recyclerviewPelis;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BD_Usuarios;
    SearchView search_view;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Pelicula, ViewHolder_Peli> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Pelicula> options;

    Dialog dialog, dialog_filtrar;

    FirebaseAuth auth;
    FirebaseUser user;

    SharedPreferences sharedPreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_peli);

        recyclerviewPelis = findViewById(R.id.recyclerviewPelis);
        recyclerviewPelis.setHasFixedSize(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        BD_Usuarios = firebaseDatabase.getReference("Usuarios");

        dialog = new Dialog(Listar_Peli.this);
        dialog_filtrar = new Dialog(Listar_Peli.this);

        initListener();
        Estado_Filtro();


    }

    private void initListener() {
        btnBack = findViewById(R.id.btn_back);
        search_view = findViewById(R.id.search_view);
        vaciar_todo = findViewById(R.id.vaciar_todo);
        filtrar = findViewById(R.id.filtrar);

        // Configura el evento del botón "volver" (si es necesario)
        btnBack.setOnClickListener(v -> finish());

        // Configura el evento para el botón "vaciar todo"
        vaciar_todo.setOnClickListener(v -> Vaciar_Registro_Pelis());

        filtrar.setOnClickListener(v -> FiltrarPeliculas());

    }


    private void ListarTodasPeliculas(){
        //consulta
        Query query = BD_Usuarios.child(user.getUid()).child("Peliculas_Publicadas").orderByChild("fecha_peli");

        options = new FirebaseRecyclerOptions.Builder<Pelicula>().setQuery(query, Pelicula.class).build();
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
                        //Toast.makeText(Listar_Peli.this, "On item click", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(Listar_Peli.this, Detalle_Peli.class));

                        String id_peli = getItem(position).getId_peli();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_peli = getItem(position).getFecha_peli();
                        String estado = getItem(position).getEstado();

                        Intent intent = new Intent(Listar_Peli.this, Detalle_Peli.class);
                        intent.putExtra("id_peli", id_peli);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("correo_usuario", correo_usuario);
                        intent.putExtra("fecha_registro", fecha_registro);
                        intent.putExtra("titulo", titulo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("fecha_peli", fecha_peli);
                        intent.putExtra("estado", estado);
                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_peli = getItem(position).getId_peli();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_peli = getItem(position).getFecha_peli();
                        String estado = getItem(position).getEstado();


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
                                //Toast.makeText(Listar_Peli.this, "Pelicula actualizada", Toast.LENGTH_SHORT).show();
                               //startActivity(new Intent(Listar_Peli.this, Actualizar_Peli.class));
                                Intent intent = new Intent(Listar_Peli.this, Actualizar_Peli.class);
                                intent.putExtra("id_peli", id_peli);
                                intent.putExtra("uid_usuario", uid_usuario);
                                intent.putExtra("correo_usuario", correo_usuario);
                                intent.putExtra("fecha_registro", fecha_registro);
                                intent.putExtra("titulo", titulo);
                                intent.putExtra("descripcion", descripcion);
                                intent.putExtra("fecha_peli", fecha_peli);
                                intent.putExtra("estado", estado);
                                startActivity(intent);

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

    private void ListarPeliculasVistas(){
        //consulta
        String estado_peli = "Visto";

        Query query = BD_Usuarios.child(user.getUid()).child("Peliculas_Publicadas").orderByChild("estado").equalTo(estado_peli);

        options = new FirebaseRecyclerOptions.Builder<Pelicula>().setQuery(query, Pelicula.class).build();
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
                        //Toast.makeText(Listar_Peli.this, "On item click", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(Listar_Peli.this, Detalle_Peli.class));

                        String id_peli = getItem(position).getId_peli();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_peli = getItem(position).getFecha_peli();
                        String estado = getItem(position).getEstado();

                        Intent intent = new Intent(Listar_Peli.this, Detalle_Peli.class);
                        intent.putExtra("id_peli", id_peli);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("correo_usuario", correo_usuario);
                        intent.putExtra("fecha_registro", fecha_registro);
                        intent.putExtra("titulo", titulo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("fecha_peli", fecha_peli);
                        intent.putExtra("estado", estado);
                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_peli = getItem(position).getId_peli();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_peli = getItem(position).getFecha_peli();
                        String estado = getItem(position).getEstado();


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
                                //Toast.makeText(Listar_Peli.this, "Pelicula actualizada", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(Listar_Peli.this, Actualizar_Peli.class));
                                Intent intent = new Intent(Listar_Peli.this, Actualizar_Peli.class);
                                intent.putExtra("id_peli", id_peli);
                                intent.putExtra("uid_usuario", uid_usuario);
                                intent.putExtra("correo_usuario", correo_usuario);
                                intent.putExtra("fecha_registro", fecha_registro);
                                intent.putExtra("titulo", titulo);
                                intent.putExtra("descripcion", descripcion);
                                intent.putExtra("fecha_peli", fecha_peli);
                                intent.putExtra("estado", estado);
                                startActivity(intent);

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

    private void ListarPeliculasPendientes(){
        //consulta
        String estado_peli = "Pendiente";
        Query query = BD_Usuarios.child(user.getUid()).child("Peliculas_Publicadas").orderByChild("estado").equalTo(estado_peli);

        options = new FirebaseRecyclerOptions.Builder<Pelicula>().setQuery(query, Pelicula.class).build();
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
                        //Toast.makeText(Listar_Peli.this, "On item click", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(Listar_Peli.this, Detalle_Peli.class));

                        String id_peli = getItem(position).getId_peli();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_peli = getItem(position).getFecha_peli();
                        String estado = getItem(position).getEstado();

                        Intent intent = new Intent(Listar_Peli.this, Detalle_Peli.class);
                        intent.putExtra("id_peli", id_peli);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("correo_usuario", correo_usuario);
                        intent.putExtra("fecha_registro", fecha_registro);
                        intent.putExtra("titulo", titulo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("fecha_peli", fecha_peli);
                        intent.putExtra("estado", estado);
                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_peli = getItem(position).getId_peli();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_hora_actual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_peli = getItem(position).getFecha_peli();
                        String estado = getItem(position).getEstado();


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
                                //Toast.makeText(Listar_Peli.this, "Pelicula actualizada", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(Listar_Peli.this, Actualizar_Peli.class));
                                Intent intent = new Intent(Listar_Peli.this, Actualizar_Peli.class);
                                intent.putExtra("id_peli", id_peli);
                                intent.putExtra("uid_usuario", uid_usuario);
                                intent.putExtra("correo_usuario", correo_usuario);
                                intent.putExtra("fecha_registro", fecha_registro);
                                intent.putExtra("titulo", titulo);
                                intent.putExtra("descripcion", descripcion);
                                intent.putExtra("fecha_peli", fecha_peli);
                                intent.putExtra("estado", estado);
                                startActivity(intent);

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
                Query query = BD_Usuarios.child(user.getUid()).child("Peliculas_Publicadas").orderByChild("id_peli").equalTo(idPeli);
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

    private void Vaciar_Registro_Pelis() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Peli.this);
        builder.setTitle("Vaciar todas las peliculas");
        builder.setMessage("¿Quieres eliminar todas las peliculas?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Eliminar todas las notas
                Query query = BD_Usuarios.child(user.getUid()).child("Peliculas_Publicadas");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Peli.this, "Las peliculas se han eliminado", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Listar_Peli.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();

    }

    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_borrar_todo_pelis, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Vaciar_Todas_Pelis ){
            Vaciar_Registro_Pelis();
        }
        return super.onOptionsItemSelected(item);
    }

    public void FiltrarPeliculas(){
        Button Todas_Peliculas, Peliculas_vistas, Peliculas_pendientes;

        dialog_filtrar.setContentView(R.layout.cuadro_dialog_filtrar_peliculas);

        Todas_Peliculas = dialog_filtrar.findViewById(R.id.Todas_Peliculas);
        Peliculas_vistas = dialog_filtrar.findViewById(R.id.Peliculas_vistas);
        Peliculas_pendientes = dialog_filtrar.findViewById(R.id.Peliculas_pendientes);

        Todas_Peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "Todas");
                editor.apply();
                recreate();
                Toast.makeText(Listar_Peli.this, "Todas las notas", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();
            }
        });

        Peliculas_vistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "Visto");
                editor.apply();
                recreate();
                Toast.makeText(Listar_Peli.this, "Peliculas vistas", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();

            }
        });

        Peliculas_pendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Listar", "Pendientes");
                editor.apply();
                recreate();
                Toast.makeText(Listar_Peli.this, "Peliculas pendientes", Toast.LENGTH_SHORT).show();
                dialog_filtrar.dismiss();

            }
        });

        dialog_filtrar.show();


    }

    public void Estado_Filtro(){
        sharedPreferences = Listar_Peli.this.getSharedPreferences("Peliculas", MODE_PRIVATE);

        String estado_filtro = sharedPreferences.getString("Listar", "Todas");

        switch (estado_filtro){
            case "Todas":
                ListarTodasPeliculas();
                break;
            case "Visto":
                ListarPeliculasVistas();
                break;
            case "Pendientes":
                ListarPeliculasPendientes();
                break;
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();

        }
    }
}