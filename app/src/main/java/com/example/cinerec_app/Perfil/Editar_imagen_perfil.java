package com.example.cinerec_app.Perfil;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.cinerec_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Editar_imagen_perfil extends AppCompatActivity {

    ImageView ImagenPerfilActualizar;
    Button BtnElegirImagen, BtnActualizarImagen;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Dialog dialog_elegir_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_imagen_perfil);

        ImagenPerfilActualizar = findViewById(R.id.ImagenPerfilActualizar);
        BtnElegirImagen = findViewById(R.id.BtnElegirImagen);
        BtnActualizarImagen = findViewById(R.id.BtnActualizarImagen);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        dialog_elegir_imagen = new Dialog(Editar_imagen_perfil.this);

        BtnElegirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Editar_imagen_perfil.this, "Elegir imagen de", Toast.LENGTH_SHORT).show();
                ElegirImagenDe();
            }

        });

        BtnActualizarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Editar_imagen_perfil.this, "Actualizar imagen", Toast.LENGTH_SHORT).show();
            }
        });

        LecturaDeImagen();
    }

    private void LecturaDeImagen(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imagen_perfil = ""+snapshot.child("imagen_perfil").getValue();
                Glide.with(getApplicationContext())
                        .load(imagen_perfil)
                        .placeholder(R.drawable.perfilmenu)
                        .into(ImagenPerfilActualizar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ElegirImagenDe() {
        Button Btn_Elegir_Galeria, Btn_Elegir_Camara;

        dialog_elegir_imagen.setContentView(R.layout.cuadro_dialog_elegir_imagen);

        Btn_Elegir_Galeria = dialog_elegir_imagen.findViewById(R.id.Btn_Elegir_Galeria);
        Btn_Elegir_Camara = dialog_elegir_imagen.findViewById(R.id.Btn_Elegir_Camara);
        
        Btn_Elegir_Galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Editar_imagen_perfil.this, "Elegir galeria", Toast.LENGTH_SHORT).show();
                dialog_elegir_imagen.dismiss();
            }
            
        });
        
        Btn_Elegir_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Editar_imagen_perfil.this, "Elegir de cámara", Toast.LENGTH_SHORT).show();
                dialog_elegir_imagen.dismiss();
            }
        });

        dialog_elegir_imagen.show();
        dialog_elegir_imagen.setCanceledOnTouchOutside(true);
    }



}