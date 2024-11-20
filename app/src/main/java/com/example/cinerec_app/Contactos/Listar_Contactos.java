package com.example.cinerec_app.Contactos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.Objetos.Contacto;
import com.example.cinerec_app.Perfil.Perfil_Usuario;
import com.example.cinerec_app.R;
import com.example.cinerec_app.ViewHolder.ViewHolderContacto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Listar_Contactos extends AppCompatActivity {

    RecyclerView recyclerViewContactos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BD_Usuarios;
    ImageView btnBack,añadir, borrar, buscar;
    SearchView search_view;


    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Dialog dialog;

    FirebaseRecyclerAdapter<Contacto, ViewHolderContacto> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Contacto> firebaseRecyclerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contactos);

        initListener();
        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        recyclerViewContactos.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        BD_Usuarios = firebaseDatabase.getReference("Usuarios");

        dialog = new Dialog(Listar_Contactos.this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        ListarContactos();

        setupSearchListener();
    }

    private void ListarContactos(){
        Query query = BD_Usuarios.child(user.getUid()).child("Contactos").orderByChild("nombre");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contacto>().setQuery(query, Contacto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacto, ViewHolderContacto>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderContacto viewHolderContacto, int position, @NonNull Contacto contacto) {
                viewHolderContacto.SetearDatosContacto(
                        getApplicationContext(),
                        contacto.getId_contacto(),
                        contacto.getUid_contacto(),
                        contacto.getNombre(),
                        contacto.getApellidos(),
                        contacto.getCorreo(),
                        contacto.getTelefono(),
                        contacto.getEdad(),
                        contacto.getDireccion(),
                        contacto.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderContacto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
                ViewHolderContacto viewHolderContacto = new ViewHolderContacto(view);
                viewHolderContacto.setOnClickListener(new ViewHolderContacto.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id_c = getItem(position).getId_contacto();
                        String uid_usuario = getItem(position).getUid_contacto();
                        String nombre_c = getItem(position).getNombre();
                        String apellidos_c = getItem(position).getApellidos();
                        String correo_c = getItem(position).getCorreo();
                        String telefono_c = getItem(position).getTelefono();
                        String edad_c = getItem(position).getEdad();
                        String direccion_c = getItem(position).getDireccion();
                        String imagen_c = getItem(position).getImagen();

                        //Enviar los datos al siguiente activity
                        Intent intent = new Intent(Listar_Contactos.this, Detalle_contacto.class);
                        intent.putExtra("id_c", id_c);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("nombre_c", nombre_c);
                        intent.putExtra("apellidos_c", apellidos_c);
                        intent.putExtra("correo_c", correo_c);
                        intent.putExtra("telefono_c", telefono_c);
                        intent.putExtra("edad_c", edad_c);
                        intent.putExtra("direccion_c", direccion_c);
                        intent.putExtra("imagen_c", imagen_c);


                        startActivity(intent);
                        //Toast.makeText(Listar_Contactos.this, "On item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        String id_c = getItem(position).getId_contacto();
                        String uid_usuario = getItem(position).getUid_contacto();
                        String nombre_c = getItem(position).getNombre();
                        String apellidos_c = getItem(position).getApellidos();
                        String correo_c = getItem(position).getCorreo();
                        String telefono_c = getItem(position).getTelefono();
                        String edad_c = getItem(position).getEdad();
                        String direccion_c = getItem(position).getDireccion();
                        String imagen_c = getItem(position).getImagen();

                        Button Btn_Eliminar, Btn_Actualizar;

                        dialog.setContentView(R.layout.dialog_contacto_opciones);
                        Btn_Eliminar = dialog.findViewById(R.id.Btn_Eliminar);
                        Btn_Actualizar = dialog.findViewById(R.id.Btn_Actualizar);

                        Btn_Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(Listar_Contactos.this, "Eliminar contacto", Toast.LENGTH_SHORT).show();
                                EliminarContacto(id_c);
                                dialog.dismiss();
                            }
                        });
                        Btn_Actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Listar_Contactos.this, Actualizar_Contacto.class);
                                intent.putExtra("id_c", id_c);
                                intent.putExtra("uid_usuario", uid_usuario);
                                intent.putExtra("nombre_c", nombre_c);
                                intent.putExtra("apellidos_c", apellidos_c);
                                intent.putExtra("correo_c", correo_c);
                                intent.putExtra("telefono_c", telefono_c);
                                intent.putExtra("edad_c", edad_c);
                                intent.putExtra("direccion_c", direccion_c);
                                intent.putExtra("imagen_c", imagen_c);

                                startActivity(intent);

                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        Toast.makeText(Listar_Contactos.this, "On item long click", Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolderContacto;
            }
        };
        recyclerViewContactos.setLayoutManager(new GridLayoutManager(Listar_Contactos.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewContactos.setAdapter(firebaseRecyclerAdapter);
    }

    private void BuscarContactos(String Nombre_Contacto){
        Query query = BD_Usuarios.child(user.getUid()).child("Contactos").orderByChild("nombre").startAt(Nombre_Contacto).endAt(Nombre_Contacto + "\uf8ff");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contacto>().setQuery(query, Contacto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacto, ViewHolderContacto>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderContacto viewHolderContacto, int position, @NonNull Contacto contacto) {
                viewHolderContacto.SetearDatosContacto(
                        getApplicationContext(),
                        contacto.getId_contacto(),
                        contacto.getUid_contacto(),
                        contacto.getNombre(),
                        contacto.getApellidos(),
                        contacto.getCorreo(),
                        contacto.getTelefono(),
                        contacto.getEdad(),
                        contacto.getDireccion(),
                        contacto.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderContacto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
                ViewHolderContacto viewHolderContacto = new ViewHolderContacto(view);
                viewHolderContacto.setOnClickListener(new ViewHolderContacto.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String id_c = getItem(position).getId_contacto();
                        String uid_usuario = getItem(position).getUid_contacto();
                        String nombre_c = getItem(position).getNombre();
                        String apellidos_c = getItem(position).getApellidos();
                        String correo_c = getItem(position).getCorreo();
                        String telefono_c = getItem(position).getTelefono();
                        String edad_c = getItem(position).getEdad();
                        String direccion_c = getItem(position).getDireccion();
                        String imagen_c = getItem(position).getImagen();

                        //Enviar los datos al siguiente activity
                        Intent intent = new Intent(Listar_Contactos.this, Detalle_contacto.class);
                        intent.putExtra("id_c", id_c);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("nombre_c", nombre_c);
                        intent.putExtra("apellidos_c", apellidos_c);
                        intent.putExtra("correo_c", correo_c);
                        intent.putExtra("telefono_c", telefono_c);
                        intent.putExtra("edad_c", edad_c);
                        intent.putExtra("direccion_c", direccion_c);
                        intent.putExtra("imagen_c", imagen_c);


                        startActivity(intent);
                        //Toast.makeText(Listar_Contactos.this, "On item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        String id_c = getItem(position).getId_contacto();
                        String uid_usuario = getItem(position).getUid_contacto();
                        String nombre_c = getItem(position).getNombre();
                        String apellidos_c = getItem(position).getApellidos();
                        String correo_c = getItem(position).getCorreo();
                        String telefono_c = getItem(position).getTelefono();
                        String edad_c = getItem(position).getEdad();
                        String direccion_c = getItem(position).getDireccion();
                        String imagen_c = getItem(position).getImagen();

                        Button Btn_Eliminar, Btn_Actualizar;

                        dialog.setContentView(R.layout.dialog_contacto_opciones);
                        Btn_Eliminar = dialog.findViewById(R.id.Btn_Eliminar);
                        Btn_Actualizar = dialog.findViewById(R.id.Btn_Actualizar);

                        Btn_Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(Listar_Contactos.this, "Eliminar contacto", Toast.LENGTH_SHORT).show();
                                EliminarContacto(id_c);
                                dialog.dismiss();
                            }
                        });
                        Btn_Actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Listar_Contactos.this, Actualizar_Contacto.class);
                                intent.putExtra("id_c", id_c);
                                intent.putExtra("uid_usuario", uid_usuario);
                                intent.putExtra("nombre_c", nombre_c);
                                intent.putExtra("apellidos_c", apellidos_c);
                                intent.putExtra("correo_c", correo_c);
                                intent.putExtra("telefono_c", telefono_c);
                                intent.putExtra("edad_c", edad_c);
                                intent.putExtra("direccion_c", direccion_c);
                                intent.putExtra("imagen_c", imagen_c);

                                startActivity(intent);

                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        Toast.makeText(Listar_Contactos.this, "On item long click", Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolderContacto;
            }
        };
        recyclerViewContactos.setLayoutManager(new GridLayoutManager(Listar_Contactos.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewContactos.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarContacto(String id_c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Contactos.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Estas seguro/a de eliminar este contacto?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query query = BD_Usuarios.child(user.getUid()).child("Contactos").orderByChild("id_contacto").equalTo(id_c);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Contactos.this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Listar_Contactos.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Listar_Contactos.this, "Accion cancelada", Toast.LENGTH_SHORT).show();
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

    private void initListener() {
        btnBack = findViewById(R.id.btn_back);
        añadir = findViewById(R.id.añadir);
        borrar = findViewById(R.id.borrar);
        buscar = findViewById(R.id.buscar);
        search_view = findViewById(R.id.search_view);

        btnBack.setOnClickListener(v -> finish());

        buscar.setOnClickListener(v -> toggleSearchView());

        añadir.setOnClickListener(v -> itemContacto());

        borrar.setOnClickListener(v -> EliminarTodosContactos());




    }

    private void toggleSearchView() {
        if (search_view.getVisibility() == View.GONE) {
            // Mostrar el SearchView cuando el usuario hace clic en el botón
            añadir.setVisibility(View.GONE);
            borrar.setVisibility(View.GONE);
            buscar.setVisibility(View.GONE);
            search_view.setVisibility(View.VISIBLE);
            search_view.requestFocus(); // Pone el foco en el SearchView
        } else {
            // Ocultar el SearchView cuando ya no lo necesite
            añadir.setVisibility(View.VISIBLE);
            borrar.setVisibility(View.VISIBLE);
            buscar.setVisibility(View.VISIBLE);
            search_view.setVisibility(View.GONE);
        }
    }

    private void setupSearchListener() {
        // Listener para el cambio de texto en el SearchView
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BuscarContactos(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrar los resultados a medida que el usuario escribe
                BuscarContactos(newText);
                return false;
            }
        });
    }


    public void itemContacto (){
        //Recuperacion del uid de la actividad anterior
        String Uid_Recuperado = getIntent().getStringExtra("Uid");
        Intent intent = new Intent(Listar_Contactos.this, Agregar_Contacto.class);
        //Enviamos el dato uid al siguiente activity
        intent.putExtra("Uid", Uid_Recuperado);
        startActivity(intent);
    }

    private void EliminarTodosContactos(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Contactos.this);
        builder.setTitle("Eliminar todos los contactos");
        builder.setMessage("¿Estás seguro/a de eliminar todos los contactos?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query query = BD_Usuarios.child(user.getUid()).child("Contactos");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Contactos.this, "Todos han sido eliminados", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Listar_Contactos.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Listar_Contactos.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }


}