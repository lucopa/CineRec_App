package com.example.cinerec_app.Contactos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinerec_app.R;

public class Detalle_contacto extends AppCompatActivity {

    ImageView btnBack, Imagen_C_D;
    TextView Id_Usuario_D, Uid_Usuario_D, Nombre_Contacto_D, Apellidos_Contacto_D, Correo_Contacto_D, Edad_Contacto_D, Telefono_Contacto_D, Direccion_Contacto_D;

    //String donde almacenaremos los datos del contacto seleccionado
    String id_c, uid_usuario, nombre_c, apellidos_c, correo_c, telefono_c, edad_c, direccion_c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);
        initListener();
        recuperarDatos();
        DatosContacto();
        ObtenerImagen();

    }

    private void initListener() {
        btnBack = findViewById(R.id.btn_back);
        Id_Usuario_D = findViewById(R.id.Id_Usuario_D);
        Imagen_C_D = findViewById(R.id.Imagen_C_D);
        Uid_Usuario_D = findViewById(R.id.Uid_Usuario_D);
        Nombre_Contacto_D = findViewById(R.id.Nombre_Contacto_D);
        Apellidos_Contacto_D = findViewById(R.id.Apellidos_Contacto_D);
        Correo_Contacto_D = findViewById(R.id.Correo_Contacto_D);
        Edad_Contacto_D = findViewById(R.id.Edad_Contacto_D);
        Telefono_Contacto_D = findViewById(R.id.Telefono_Contacto_D);
        Direccion_Contacto_D = findViewById(R.id.Direccion_Contacto_D);


        btnBack.setOnClickListener(v -> finish());

    }

    private void recuperarDatos(){
        Bundle bundle = getIntent().getExtras();

        id_c = bundle.getString("id_c");
        uid_usuario = bundle.getString("uid_usuario");
        nombre_c = bundle.getString("nombre_c");
        apellidos_c = bundle.getString("apellidos_c");
        correo_c = bundle.getString("correo_c");
        telefono_c = bundle.getString("telefono_c");
        edad_c = bundle.getString("edad_c");
        direccion_c = bundle.getString("direccion_c");


    }

    private void DatosContacto(){
        Id_Usuario_D.setText(id_c);
        Uid_Usuario_D.setText(uid_usuario);
        Nombre_Contacto_D.setText("Nombre: "+nombre_c);
        Apellidos_Contacto_D.setText("Apellidos: "+apellidos_c);
        Correo_Contacto_D.setText("Correo: "+correo_c);
        Edad_Contacto_D.setText("Edad: "+edad_c);
        Telefono_Contacto_D.setText("Teléfono: "+telefono_c);
        Direccion_Contacto_D.setText("Dirección: "+direccion_c);


    }

    private void ObtenerImagen(){
        String imagen = getIntent().getStringExtra("imagen_c");
        try {
            Glide.with(getApplicationContext())
                    .load(imagen)  // Carga la imagen
                    .placeholder(R.drawable.usuario)  // Imagen de marcador de posición
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))  // Aplica la transformación de bordes redondeados (ajustar el valor)
                    .into(Imagen_C_D);
        }catch (Exception e){
            Toast.makeText(this, "Cargando imagen", Toast.LENGTH_SHORT).show();
        }
    }
}