package com.example.cinerec_app.Detalle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinerec_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Detalle_Peli extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Button Boton_Importante;

    String id_peli_R, uid_usuario_R, correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    TextView Id_Peli_Detalle, Uid_usuario_detalle, Correo_usuario_detalle, Titulo_Detalle, Descripcion_Detalle, Fecha_registro_Detalle, Fecha_peli_Detalle, estado;

    ImageView btnBack;

    boolean ComprobarPeliFavorita = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_peli);


        InicializarVistas();
        RecuperarDatos();
        SetearDatosRecuperados();
        initListener();
        VerificarPeliFavorita();

        Boton_Importante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ComprobarPeliFavorita){
                    EliminarPeliFavorita();
                } else {
                    AgregarPeliFavorita();
                }
            }
        });


    }

    private void InicializarVistas(){
        Id_Peli_Detalle = findViewById(R.id.Id_Peli_Detalle);
        Uid_usuario_detalle = findViewById(R.id.Uid_usuario_detalle);
        Correo_usuario_detalle = findViewById(R.id.Correo_usuario_detalle);
        Titulo_Detalle = findViewById(R.id.Titulo_Detalle);
        Descripcion_Detalle = findViewById(R.id.Descripcion_Detalle);
        Fecha_registro_Detalle = findViewById(R.id.Fecha_registro_Detalle);
        Fecha_peli_Detalle = findViewById(R.id.Fecha_peli_Detalle);
        estado = findViewById(R.id.estado);
        btnBack = findViewById(R.id.btn_back);
        Boton_Importante = findViewById(R.id.Boton_Importante);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();



    }

    private void RecuperarDatos(){
        Bundle intent = getIntent().getExtras();

        id_peli_R = intent.getString("id_peli");
        uid_usuario_R = intent.getString("uid_usuario");
        correo_usuario_R = intent.getString("correo_usuario");
        fecha_registro_R = intent.getString("fecha_registro");
        titulo_R = intent.getString("titulo");
        descripcion_R = intent.getString("descripcion");
        fecha_R = intent.getString("fecha_peli");
        estado_R = intent.getString("estado");


    }

    private void SetearDatosRecuperados(){
        Id_Peli_Detalle.setText(id_peli_R);
        Uid_usuario_detalle.setText(uid_usuario_R);
        Correo_usuario_detalle.setText(correo_usuario_R);
        Fecha_registro_Detalle.setText(fecha_registro_R);
        Titulo_Detalle.setText(titulo_R);
        Descripcion_Detalle.setText(descripcion_R);
        Fecha_peli_Detalle.setText(fecha_R);
        estado.setText(estado_R);


    }

    private void AgregarPeliFavorita(){
        if (user == null){
            //Si el user es nulo
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        } else {
            //Obtenemos los datos de la pelicula del activity anterior
            Bundle intent = getIntent().getExtras();

            id_peli_R = intent.getString("id_peli");
            uid_usuario_R = intent.getString("uid_usuario");
            correo_usuario_R = intent.getString("correo_usuario");
            fecha_registro_R = intent.getString("fecha_registro");
            titulo_R = intent.getString("titulo");
            descripcion_R = intent.getString("descripcion");
            fecha_R = intent.getString("fecha_peli");
            estado_R = intent.getString("estado");



            //hashmap para enviar los datos a la base de datos de firebase (clave, valor)
            HashMap<String, String> Peli_Favorita = new HashMap<>();
            Peli_Favorita.put("id_peli", id_peli_R);
            Peli_Favorita.put("uid_usuario", uid_usuario_R);
            Peli_Favorita.put("correo_usuario", correo_usuario_R);
            Peli_Favorita.put("fecha_hora_actual", fecha_registro_R);
            Peli_Favorita.put("titulo", titulo_R);
            Peli_Favorita.put("descripcion", descripcion_R);
            Peli_Favorita.put("fecha_peli", fecha_R);
            Peli_Favorita.put("estado", estado_R);



            //referencia a la bbdd donde estan almacenados los usuarios
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            //hace que la peli favorita se cree dentro del usuario actual y se listan con su id
            reference.child(firebaseAuth.getUid()).child("Mis peliculas favoritas").child(id_peli_R)
                    .setValue(Peli_Favorita)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Detalle_Peli.this, "Se ha añadido a peliculas favoritas", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Detalle_Peli.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void EliminarPeliFavorita(){
        if (user == null){
            Toast.makeText(Detalle_Peli.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        } else {
            Bundle intent = getIntent().getExtras();

            id_peli_R = intent.getString("id_peli");


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis peliculas favoritas").child(id_peli_R)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Detalle_Peli.this, "La peli ya no es favorita", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Detalle_Peli.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void VerificarPeliFavorita(){
        if (user == null){
            Toast.makeText(Detalle_Peli.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();

        } else {
            Bundle intent = getIntent().getExtras();

            id_peli_R = intent.getString("id_peli");


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis peliculas favoritas").child(id_peli_R)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ComprobarPeliFavorita = snapshot.exists();
                            if (ComprobarPeliFavorita){
                                String favorita = "Favorita";
                                Boton_Importante.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icono_favorita,0,0);
                                Boton_Importante.setText(favorita);
                            } else {
                                String no_Favorita = "No Favorita";
                                Boton_Importante.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icono_no_favorita, 0, 0);
                                Boton_Importante.setText(no_Favorita);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

    }
}