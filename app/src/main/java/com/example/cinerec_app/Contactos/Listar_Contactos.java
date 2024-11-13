package com.example.cinerec_app.Contactos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerec_app.Perfil.Perfil_Usuario;
import com.example.cinerec_app.R;

public class Listar_Contactos extends AppCompatActivity {

    ImageView btnBack,añadirContacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contactos);

        initListener();
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