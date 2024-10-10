package com.example.cinerec_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuPrincipal extends AppCompatActivity {

    ImageView btnBack;
    ImageView tvTitleToolbar;
    ImageView menuGallery;
    Button CerrarSesion;
    FirebaseAuth firebaseAuth;


    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        initViews();
        initListener();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitleToolbar = findViewById(R.id.tv_title_toolbar);
        menuGallery = findViewById(R.id.menu_gallery_toolbar);
        CerrarSesion = findViewById(R.id.CerrarSesion);
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

        menuGallery.setOnClickListener(v -> Snackbar.make(v, "Opening Gallery...", Snackbar.LENGTH_SHORT).show());

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
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
}
