package com.example.cinerec_app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {

    TextView tengoUnaCuenta;
    EditText nombre,correo,contraseña,confirmarContraseña;
    Button registroBoton;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String nombreV = " ", correoV = " ", password = "", confirmarPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro);


        nombre = findViewById(R.id.nombre);
        correo = findViewById(R.id.correo);
        contraseña = findViewById(R.id.contraseña);
        confirmarContraseña = findViewById(R.id.confirmarContraseña);
        registroBoton = findViewById(R.id.registroBoton);
        tengoUnaCuenta = findViewById(R.id.tengoUnaCuenta);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espera por favor");
        progressDialog.setCanceledOnTouchOutside(false); //hace que no pueda cerrarse cuando el usuario presione en cualquier parte, se cerrará hasta que finalice

        tengoUnaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registro.this, Login.class));
            }
        });

        registroBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarDatos();
            }
        });


    }

    private void ValidarDatos(){
        nombreV = nombre.getText().toString();
        correoV = correo.getText().toString();
        password = contraseña.getText().toString();
        confirmarPassword = confirmarContraseña.getText().toString();
        
        if (TextUtils.isEmpty(nombreV)){
            Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correoV).matches()){ //comprueba que tiene los patrones de un correo
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmarPassword)){
            Toast.makeText(this, "Confirma la contraseña", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmarPassword)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        else {
            CrearCuenta();
        }
    }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        //Creamos un usuario en Firebase
        firebaseAuth.createUserWithEmailAndPassword(correoV, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                    GuardarInformacion();
            }
        }).addOnFailureListener(new OnFailureListener() { //nos permite detectar si el registro no fue exitoso
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Guardando su informacion");
        progressDialog.show();


        //Obtenemos la identificacion de usuario actual
        String uid = firebaseAuth.getUid(); //se hace para obtener el usuario actual que quiere registrarse en la app

        //Confirguramos los datos que vamos a registrar en la base de datos

        HashMap<String, String> Datos = new HashMap<>();
        //Datos del usuario
        Datos.put("uid", uid);
        Datos.put("correo", correoV);
        Datos.put("nombre", nombreV);
        Datos.put("contraseña", password);

        Datos.put("apellidos", "");
        Datos.put("edad", "");
        Datos.put("telefono", "");
        Datos.put("direccion", "");
        Datos.put("estudios", "");
        Datos.put("profesion", "");
        Datos.put("fecha_nacimiento", "");
        Datos.put("imagen_perfil", "");

        //Inicializamos la base de datos y le establecemos el nombre de la base de datos

        DatabaseReference dataBaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        dataBaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registro.this,MenuPrincipal.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}