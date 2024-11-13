package com.example.cinerec_app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.cinerec_app.AgregarPeli.Agregar_Peli;
import com.example.cinerec_app.Contactos.Listar_Contactos;
import com.example.cinerec_app.Favoritas.Pelis_Favoritas;
import com.example.cinerec_app.ListarPeli.Listar_Peli;
import com.example.cinerec_app.Perfil.Perfil_Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {

    ImageView btnBack, perfil;
    ImageView tvTitleToolbar;
    ImageView menuGallery;
   //BottomNavigationView bottom_nav;
    FirebaseAuth firebaseAuth;
    Button AgregarPeli, ListarPeli, Archivados, Contactos, cerrar, acercaDe,EstadoCuentaPrincipal;

    LinearLayoutCompat Linear_Verificado,Linear_Nombre,Linear_Correo;

    TextView UidPrincipal, NombresPrincipal, CorreoPrincipal;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    DatabaseReference Usuarios;

    Dialog dialog_cuenta_verificada;

    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Cine Rec");
        AgregarPeli = findViewById(R.id.AgregarPeli);
        ListarPeli = findViewById(R.id.ListarPeli);
        Archivados = findViewById(R.id.Archivados);
        Contactos = findViewById(R.id.Contactos);
        cerrar = findViewById(R.id.cerrar);
        acercaDe = findViewById(R.id.acercaDe);
        UidPrincipal = findViewById(R.id.UidPrincipal);
        NombresPrincipal = findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
        progressBar = findViewById(R.id.progressBar);
        Linear_Verificado = findViewById(R.id.Linear_Verificado);
        Linear_Nombre = findViewById(R.id.Linear_Nombre);
        Linear_Correo = findViewById(R.id.Linear_Correo);
        EstadoCuentaPrincipal = findViewById(R.id.EstadoCuentaPrincipal);


        dialog_cuenta_verificada = new Dialog(this);

        progressDialog =  new ProgressDialog(this);
        progressDialog.setTitle("Espera un momento...");
        progressDialog.setCanceledOnTouchOutside(false);

       // bottom_nav = findViewById(R.id.bottom_nav);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");


        initViews();
        initListener();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        EstadoCuentaPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()){
                    Toast.makeText(MenuPrincipal.this, "Cuenta ya verificada", Toast.LENGTH_SHORT).show();
                    AnimacionCuentaVerificada();
                } else {
                    VerificarCuentaCorreo();
                }
            }
        });

        AgregarPeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid_usuario = UidPrincipal.getText().toString();
                String correo_usuario = CorreoPrincipal.getText().toString();

               Intent intent = new Intent(MenuPrincipal.this, Agregar_Peli.class);
               //establecemos una clave para el dato que vamos a enviar, sirve para recuperar el dato en otra actividad y hace el llamado
               intent.putExtra("Uid", uid_usuario);
               intent.putExtra("Correo", correo_usuario);
               startActivity(intent);

            }
        });

        ListarPeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Listar_Peli.class));
                Toast.makeText(MenuPrincipal.this, "Listar Peliculas", Toast.LENGTH_SHORT).show();
            }
        });

        Archivados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Pelis_Favoritas.class));
                Toast.makeText(MenuPrincipal.this, "Peliculas archivadas", Toast.LENGTH_SHORT).show();
            }
        });

        Contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(MenuPrincipal.this, "Contactos", Toast.LENGTH_SHORT).show();
                //Obtenemos el uid del usuario
                String uid_usuario = UidPrincipal.getText().toString();
                //Enviamos el uid a la siguiente actividad
                Intent intent = new Intent(MenuPrincipal.this, Listar_Contactos.class);
                intent.putExtra("Uid", uid_usuario);
                startActivity(intent);
            }
        });

        acercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuPrincipal.this, "Acerca de", Toast.LENGTH_SHORT).show();
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });

    }

    private void VerificarCuentaCorreo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verificar Cuenta")
                .setMessage("¿Confirmas que quieres que las instrucciones de verificación se envíen a tu correo electrónico?"
                +user.getEmail())
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EnviarCorreoVerificacion();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MenuPrincipal.this, "Anulado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void EnviarCorreoVerificacion() {
        progressDialog.setMessage("Enviando las instrucciones de verificacion a su correo electronico" +user.getEmail());
        progressDialog.show();

        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Si el envio ha sido realizado
                        progressDialog.dismiss();
                        Toast.makeText(MenuPrincipal.this, "Intruscciones enviadas, revise su bandeja", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Si el envio no ha sido realizado
                        Toast.makeText(MenuPrincipal.this, "Fallo debido a: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void VerificarEstadoCuenta(){
        String Verificado = "Verificado";
        String No_Verificado = "No Verificado";

        if (user.isEmailVerified()){
            EstadoCuentaPrincipal.setText(Verificado);
            EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(34, 153, 84));
        } else {
            EstadoCuentaPrincipal.setText(No_Verificado);
            EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(231, 76, 60));

        }

    }

    private void AnimacionCuentaVerificada(){
        Button EntendidoVerificado;

        dialog_cuenta_verificada.setContentView(R.layout.dialog_cuenta_verificada);

        EntendidoVerificado = dialog_cuenta_verificada.findViewById(R.id.EntendidoVerificado);

        EntendidoVerificado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_cuenta_verificada.dismiss();
            }
        });
        dialog_cuenta_verificada.show();
        dialog_cuenta_verificada.setCanceledOnTouchOutside(false); //en el momento en el que se ejcute para que no se cierre la animacion si pincha fuera del cuadro
    }




    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }

    private void ComprobarInicioSesion() {
        if (user != null) {
            //El usuario ha iniciado sesión
            CargarDatos();
        } else {
            //Lo dirigirá al MainActivity
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    private void CargarDatos() {


        VerificarEstadoCuenta();



        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Si el usuario existe
                if (snapshot.exists()) {
                    //El progress bar se oculta
                    progressBar.setVisibility(View.GONE);
                    //Los TextView se muestran
                    //UidPrincipal.setVisibility(View.VISIBLE);
                    Linear_Nombre.setVisibility(View.VISIBLE);
                    Linear_Correo.setVisibility(View.VISIBLE);
                    Linear_Verificado.setVisibility(View.VISIBLE);

                    //Obtenemos los datos
                    String uid = "" + snapshot.child("uid").getValue();
                    String nombre = "" + snapshot.child("nombre").getValue();
                    String correo = "" + snapshot.child("correo").getValue();

                    //Seteamos los datos en los respectivos TextView
                    UidPrincipal.setText(uid);
                    NombresPrincipal.setText(nombre);
                    CorreoPrincipal.setText(correo);

                    //Habilitar los botones del menu
                    AgregarPeli.setEnabled(true);
                    ListarPeli.setEnabled(true);
                    Archivados.setEnabled(true);
                    Contactos.setEnabled(true);
                    cerrar.setEnabled(true);
                    acercaDe.setEnabled(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitleToolbar = findViewById(R.id.tv_title_toolbar);
        cerrar = findViewById(R.id.cerrar);
        perfil = findViewById(R.id.perfil);
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

        perfil.setOnClickListener(v ->  startActivity(new Intent(MenuPrincipal.this, Perfil_Usuario.class)));


    }



    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesión", Toast.LENGTH_SHORT).show();
    }


}

