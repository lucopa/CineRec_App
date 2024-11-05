package com.example.cinerec_app.Perfil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerec_app.MenuPrincipal;
import com.example.cinerec_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Perfil_Usuario extends AppCompatActivity {

    ImageView imagen_perfil;
    TextView Correo_Perfil,Uid_Perfil;
    EditText Nombres_Perfil, Apellidos_Perfil, Edad_Perfil, Telefono_Perfil, Direccion_Perfil, Estudios_Perfil, Profesion_Perfil;
    Button Guardar_Datos;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        IncializarVariables();

    }

    private void IncializarVariables(){
        imagen_perfil = findViewById(R.id.imagen_perfil);
        Correo_Perfil = findViewById(R.id.Correo_Perfil);
        Uid_Perfil = findViewById(R.id.Uid_Perfil);
        Nombres_Perfil = findViewById(R.id.Nombres_Perfil);
        Apellidos_Perfil = findViewById(R.id.Apellidos_Perfil);
        Edad_Perfil = findViewById(R.id.Edad_Perfil);
        Telefono_Perfil = findViewById(R.id.Telefono_Perfil);
        Direccion_Perfil = findViewById(R.id.Direccion_Perfil);
        Estudios_Perfil = findViewById(R.id.Estudios_Perfil);
        Profesion_Perfil = findViewById(R.id.Profesion_Perfil);

        Guardar_Datos = findViewById(R.id.Guardar_Datos);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");


    }

    private void LecturaDeDAtos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //Obtenemos sus datos
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombre = ""+snapshot.child("nombre").getValue();
                    String apellidos = ""+snapshot.child("apellidos").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    String direccion = ""+snapshot.child("direccion").getValue();
                    String edad = ""+snapshot.child("edad").getValue();
                    String estudios = ""+snapshot.child("estudios").getValue();
                    String profesion = ""+snapshot.child("profesion").getValue();
                    String telefono = ""+snapshot.child("telefono").getValue();


                    //Seteo de datos

                    Uid_Perfil.setText(uid);
                    Nombres_Perfil.setText(nombre);
                    Apellidos_Perfil.setText(apellidos);
                    Correo_Perfil.setText(correo);
                    Edad_Perfil.setText(edad);
                    Telefono_Perfil.setText(telefono);
                    Direccion_Perfil.setText(direccion);
                    Estudios_Perfil.setText(estudios);
                    Profesion_Perfil.setText(profesion);


                } else {
                    Toast.makeText(Perfil_Usuario.this, "Esperando datos", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Perfil_Usuario.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ComprobarInicioSesion(){
        if (user!=null){
            LecturaDeDAtos();
        } else {
            startActivity(new Intent(Perfil_Usuario.this, MenuPrincipal.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }
}