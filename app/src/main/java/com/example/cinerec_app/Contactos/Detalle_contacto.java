package com.example.cinerec_app.Contactos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinerec_app.R;

public class Detalle_contacto extends AppCompatActivity {

    ImageView btnBack, Imagen_C_D;
    TextView Id_Usuario_D, Uid_Usuario_D, Nombre_Contacto_D, Apellidos_Contacto_D, Correo_Contacto_D, Edad_Contacto_D, Telefono_Contacto_D, Direccion_Contacto_D;
    Button llamada,mensaje;
    //String donde almacenaremos los datos del contacto seleccionado
    String id_c, uid_usuario, nombre_c, apellidos_c, correo_c, telefono_c, edad_c, direccion_c, titulo_pelicula;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);
        initListener();
        recuperarDatos();
        DatosContacto();
        ObtenerImagen();

        llamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (ContextCompat.checkSelfPermission(Detalle_contacto.this,
                       Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                   llamar();
               } else {
                   PermisoLLamada.launch(Manifest.permission.CALL_PHONE);
               }
            }
        });

        mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Detalle_contacto.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    mensajear();
                } else {
                    PermisoMensaje.launch(Manifest.permission.SEND_SMS);
                }
            }
        });

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
        llamada = findViewById(R.id.llamada);
        mensaje = findViewById(R.id.mensaje);




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
        Telefono_Contacto_D.setText(telefono_c);
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

    private void llamar(){
        String telefono = Telefono_Contacto_D.getText().toString();
        if (!telefono.equals("")){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+telefono));
            startActivity(intent);
        } else {
            Toast.makeText(this, "El contacto no tiene un telefono registrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void mensajear(){
        String telefono = Telefono_Contacto_D.getText().toString();
        if (!telefono.equals("")){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:"+telefono));

            // Incluye el nombre del contacto en el cuerpo del mensaje
            String mensajePredeterminado = "Hola " + nombre_c + "! Te recomiendo la película \"" +  "\", me ha encantado!";
            intent.putExtra("sms_body", mensajePredeterminado);

            startActivity(intent);

        } else {
            Toast.makeText(this, "Permiso rechazado", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<String> PermisoMensaje =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    mensajear();
                } else {
                    Toast.makeText(this, "Permiso rechazado", Toast.LENGTH_SHORT).show();
                }
            });

    private ActivityResultLauncher<String> PermisoLLamada =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted){
                    llamar();
                } else {
                    Toast.makeText(this, "Permiso rechazado", Toast.LENGTH_SHORT).show();
                }
            });
}
