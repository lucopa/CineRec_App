package com.example.cinerec_app.Contactos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerec_app.R;

public class Actualizar_Contacto extends AppCompatActivity {

    ImageView imagen_perfil_contacto, editar_imagen_contacto;
    TextView  Id_Contacto_A, Uid_Contacto_A, Telefono_Contacto_A;
    EditText Nombre_Contacto_A, Apellidos_Contacto_A, Edad_Contacto_A, Direccion_Contacto_A, Correo_Contacto_A;
    Button Guardar_Datos_Contacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contacto);

        InitListener();

    }

    private void InitListener(){
        Guardar_Datos_Contacto = findViewById(R.id.Guardar_Datos_Contacto);
        Nombre_Contacto_A = findViewById(R.id.Nombre_Contacto_A);
        Apellidos_Contacto_A = findViewById(R.id.Apellidos_Contacto_A);
        Edad_Contacto_A = findViewById(R.id.Edad_Contacto_A);
        Direccion_Contacto_A = findViewById(R.id.Direccion_Contacto_A);
        Correo_Contacto_A = findViewById(R.id.Correo_Contacto_A);
        Telefono_Contacto_A = findViewById(R.id.Telefono_Contacto_A);
        Id_Contacto_A = findViewById(R.id.Id_Contacto_A);
        Uid_Contacto_A = findViewById(R.id.Uid_Contacto_A);
        imagen_perfil_contacto = findViewById(R.id.imagen_perfil_contacto);
        editar_imagen_contacto = findViewById(R.id.editar_imagen_contacto);

    }
}