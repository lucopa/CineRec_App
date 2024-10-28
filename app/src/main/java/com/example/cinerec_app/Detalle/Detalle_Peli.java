package com.example.cinerec_app.Detalle;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerec_app.R;

import java.util.Set;

public class Detalle_Peli extends AppCompatActivity {

    String id_peli_R, uid_usuario_R, correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    TextView Id_Peli_Detalle, Uid_usuario_detalle, Correo_usuario_detalle, Titulo_Detalle, Descripcion_Detalle, Fecha_registro_Detalle, Fecha_peli_Detalle, estado;

    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_peli);
        InicalizarVistas();
        RecuperarDatos();
        SetearDatosRecuperados();
        initListener();

    }

    private void InicalizarVistas(){
        Id_Peli_Detalle = findViewById(R.id.Id_Peli_Detalle);
        Uid_usuario_detalle = findViewById(R.id.Uid_usuario_detalle);
        Correo_usuario_detalle = findViewById(R.id.Correo_usuario_detalle);
        Titulo_Detalle = findViewById(R.id.Titulo_Detalle);
        Descripcion_Detalle = findViewById(R.id.Descripcion_Detalle);
        Fecha_registro_Detalle = findViewById(R.id.Fecha_registro_Detalle);
        Fecha_peli_Detalle = findViewById(R.id.Fecha_peli_Detalle);
        estado = findViewById(R.id.estado);
        btnBack = findViewById(R.id.btn_back);


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

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

    }
}