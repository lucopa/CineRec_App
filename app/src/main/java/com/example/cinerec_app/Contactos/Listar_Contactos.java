package com.example.cinerec_app.Contactos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Listar_Contactos extends AppCompatActivity {

    RecyclerView recyclerViewContactos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BD_Usuarios;
    ImageView btnBack,añadirContacto;


    FirebaseAuth firebaseAuth;
    FirebaseUser user;

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

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        ListarContactos();
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

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void initListener() {
        btnBack = findViewById(R.id.btn_back);
        añadirContacto = findViewById(R.id.añadirContacto);


        btnBack.setOnClickListener(v -> finish());

        añadirContacto.setOnClickListener(v -> itemContacto());


    }

    public void itemContacto (){
        //Recuperacion del uid de la actividad anterior
        String Uid_Recuperado = getIntent().getStringExtra("Uid");
        Intent intent = new Intent(Listar_Contactos.this, Agregar_Contacto.class);
        //Enviamos el dato uid al siguiente activity
        intent.putExtra("Uid", Uid_Recuperado);
        startActivity(intent);
    }


}