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
import com.google.android.material.snackbar.Snackbar;
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
    Button CerrarSesion;
    FirebaseAuth firebaseAuth;

    TextView NombresPrincipal, CorreoPrincipal;
    ProgressBar progressBar;

    DatabaseReference Usuarios;


    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //ActionBar actionBar = getSupportActionBar();
        // actionBar.setTitle("Cine Rec");
        NombresPrincipal = findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
        progressBar = findViewById(R.id.progressBar);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");


        initViews();
        initListener();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


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
                    NombresPrincipal.setVisibility(View.VISIBLE);
                    CorreoPrincipal.setVisibility(View.VISIBLE);

                    //Obtenemos los datos
                    String nombre = "" + snapshot.child("nombre").getValue();
                    String correo = "" + snapshot.child("correo").getValue();

                    //Seteamos los datos en los respectivos TextView
                    NombresPrincipal.setText(nombre);
                    CorreoPrincipal.setText(correo);
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
        //CerrarSesion = findViewById(R.id.CerrarSesion);
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

        menuGallery.setOnClickListener(v -> Snackbar.make(v, "Abriendo el perfil...", Snackbar.LENGTH_SHORT).show());

       /* CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });

    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesión", Toast.LENGTH_SHORT).show();
    }
    */

    }
}
