package com.example.cinerec_app.AgregarPeli;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerec_app.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Agregar_Peli extends AppCompatActivity {

    TextView  Uid_Usuario,Correo_Usuario,fecha_hora,Fecha,Estado;
    EditText Titulo, Descripcion;
    Button Btn_Calendario;
    ImageView btnBack, add_film;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_peli);

        InicializarVariables();
        ObtenerDatos();
        ObtenerFechayHora();
        initListener();

    }

    //este metodo esta para que no haya tanto codigo en el on create
    private void InicializarVariables(){
        Uid_Usuario = findViewById(R.id.Uid_Usuario);
        Correo_Usuario = findViewById(R.id.Correo_Usuario);
        fecha_hora = findViewById(R.id.fecha_hora);
        Fecha = findViewById(R.id.Fecha);
        Estado = findViewById(R.id.Estado);
        btnBack = findViewById(R.id.btn_back);
        Titulo = findViewById(R.id.Titulo);
        Descripcion = findViewById(R.id.Descripcion);
        Btn_Calendario = findViewById(R.id.Btn_Calendario);
        add_film = findViewById(R.id.add_film);
    }

    private void ObtenerDatos(){
        String uid_recuperado = getIntent().getStringExtra("Uid");
        String correo_recuperado = getIntent().getStringExtra("Correo");

        Uid_Usuario.setText(uid_recuperado);
        Correo_Usuario.setText(correo_recuperado);


    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

        add_film.setOnClickListener(v -> Snackbar.make(v, "Nota agregada con exito", Snackbar.LENGTH_SHORT).show());


    }

    private void ObtenerFechayHora(){
        String fecha_hora_registro = new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss a",
                Locale.getDefault()).format(System.currentTimeMillis());

        fecha_hora.setText(fecha_hora_registro);
    }



}