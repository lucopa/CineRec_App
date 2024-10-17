package com.example.cinerec_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinerec_app.AgregarPeli.Agregar_Peli;
import com.example.cinerec_app.Archivadas.Pelis_Archivadas;
import com.example.cinerec_app.ListarPeli.Listar_Peli;
import com.example.cinerec_app.Perfil.Perfil_Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.example.cinerec_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {

    ImageView btnBack;
    ImageView tvTitleToolbar;
    ImageView menuGallery;
    BottomNavigationView bottom_nav;
    FirebaseAuth firebaseAuth;
    Button AgregarPeli, ListarPeli, Archivados, Perfil, cerrar, acercaDe;

    TextView UidPrincipal, NombresPrincipal, CorreoPrincipal;
    ProgressBar progressBar;

    DatabaseReference Usuarios;


    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //ActionBar actionBar = getSupportActionBar();
        // actionBar.setTitle("Cine Rec");
        AgregarPeli = findViewById(R.id.AgregarPeli);
        ListarPeli = findViewById(R.id.ListarPeli);
        Archivados = findViewById(R.id.Archivados);
        Perfil = findViewById(R.id.Perfil);
        cerrar = findViewById(R.id.cerrar);
        acercaDe = findViewById(R.id.acercaDe);
        UidPrincipal = findViewById(R.id.UidPrincipal);
        NombresPrincipal = findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
        progressBar = findViewById(R.id.progressBar);
        bottom_nav = findViewById(R.id.bottom_nav);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");


        initViews();
        initListener();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        AgregarPeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid_usuario = UidPrincipal.getText().toString();
                String correo_usuario = CorreoPrincipal.getText().toString();

               Intent intent = new Intent(MenuPrincipal.this, Agregar_Peli.class);
               //establecemos una clave para el dato que vamos a enviar, sirve para recuperar el dato en otra actividad y hace el llamado
               intent.putExtra("Uid", uid_usuario);
               intent.putExtra("Correo", correo_usuario);
               startActivity(intent);

            }
        });

        ListarPeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Listar_Peli.class));
                Toast.makeText(MenuPrincipal.this, "Listar Peliculas", Toast.LENGTH_SHORT).show();
            }
        });

        Archivados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Pelis_Archivadas.class));
                Toast.makeText(MenuPrincipal.this, "Peliculas archivadas", Toast.LENGTH_SHORT).show();
            }
        });

        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class));
                Toast.makeText(MenuPrincipal.this, "Perfil", Toast.LENGTH_SHORT).show();
            }
        });

        acercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuPrincipal.this, "Acerca de", Toast.LENGTH_SHORT).show();
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });



    }


    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }

    private void ComprobarInicioSesion() {
        if (user != null) {
            //El usuario ha iniciado sesión
            CargarDatos();
        } else {
            //Lo dirigirá al MainActivity
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    private void CargarDatos() {
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if (snapshot.exists()) {
                    //El progress bar se oculta
                    progressBar.setVisibility(View.GONE);
                    //Los TextView se muestran
                    UidPrincipal.setVisibility(View.VISIBLE);
                    NombresPrincipal.setVisibility(View.VISIBLE);
                    CorreoPrincipal.setVisibility(View.VISIBLE);

                    //Obtenemos los datos
                    String uid = "" + snapshot.child("uid").getValue();
                    String nombre = "" + snapshot.child("nombre").getValue();
                    String correo = "" + snapshot.child("correo").getValue();

                    //Seteamos los datos en los respectivos TextView
                    UidPrincipal.setText(uid);
                    NombresPrincipal.setText(nombre);
                    CorreoPrincipal.setText(correo);

                    //Habilitar los botones del menu
                    AgregarPeli.setEnabled(true);
                    ListarPeli.setEnabled(true);
                    Archivados.setEnabled(true);
                    Perfil.setEnabled(true);
                    cerrar.setEnabled(true);
                    acercaDe.setEnabled(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitleToolbar = findViewById(R.id.tv_title_toolbar);
        menuGallery = findViewById(R.id.menu_gallery_toolbar);
        cerrar = findViewById(R.id.cerrar);
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

        menuGallery.setOnClickListener(v -> Snackbar.make(v, "Abriendo el perfil...", Snackbar.LENGTH_SHORT).show());


    }
    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesión", Toast.LENGTH_SHORT).show();
    }


}

