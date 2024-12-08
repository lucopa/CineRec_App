package com.example.cinerec_app.view.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinerec_app.view.main.MainActivity;
import com.example.cinerec_app.view.main.MenuPrincipal;
import com.example.cinerec_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PantallaCarga extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);


        firebaseAuth = FirebaseAuth.getInstance();

       final int TIEMPO = 4000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PantallaCarga.this, MainActivity.class ));
                finish();
                verificarUsuario();
            }
        }, TIEMPO);

    }

    private void verificarUsuario(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser(); //obtenemos el usuario actual (que se ha registrado previamente y ha iniciado sesion)

        if (firebaseUser == null) { // si el usuario no ha iniciado sesion previamente, va a al main
            startActivity(new Intent(PantallaCarga.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(PantallaCarga.this, MenuPrincipal.class));
            finish();
        }
    }
}