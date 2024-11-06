package com.example.cinerec_app.Perfil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.cinerec_app.ActualizarPeli.Actualizar_Peli;
import com.example.cinerec_app.MenuPrincipal;
import com.example.cinerec_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;

public class Perfil_Usuario extends AppCompatActivity {

    ImageView imagen_perfil;
    TextView Correo_Perfil,Uid_Perfil,Telefono_Perfil,Fecha_Nacimiento_Perfil;
    EditText Nombres_Perfil, Apellidos_Perfil, Edad_Perfil, Direccion_Perfil, Estudios_Perfil, Profesion_Perfil;
    Button Guardar_Datos;
    ImageView EditarTelefono,Editar_Fecha;


    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;

    Dialog dialog_establecer_tlf;

    int dia, mes, year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        IncializarVariables();

        EditarTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Establecer_tfl_usuario();
            }
        });

        Editar_Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirCalendario();
            }
        });

    }

    private void IncializarVariables(){
        imagen_perfil = findViewById(R.id.imagen_perfil);
        Correo_Perfil = findViewById(R.id.Correo_Perfil);
        Uid_Perfil = findViewById(R.id.Uid_Perfil);
        Telefono_Perfil = findViewById(R.id.Telefono_Perfil);
        Nombres_Perfil = findViewById(R.id.Nombres_Perfil);
        Apellidos_Perfil = findViewById(R.id.Apellidos_Perfil);
        Edad_Perfil = findViewById(R.id.Edad_Perfil);
        Direccion_Perfil = findViewById(R.id.Direccion_Perfil);
        Estudios_Perfil = findViewById(R.id.Estudios_Perfil);
        Profesion_Perfil = findViewById(R.id.Profesion_Perfil);
        Fecha_Nacimiento_Perfil = findViewById(R.id.Fecha_Nacimiento_Perfil);

        EditarTelefono = findViewById(R.id.EditarTelefono);
        Editar_Fecha = findViewById(R.id.Editar_Fecha);

        dialog_establecer_tlf = new Dialog(Perfil_Usuario.this);
        Guardar_Datos = findViewById(R.id.Guardar_Datos);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");


    }

    private void LecturaDeDAtos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //Obtenemos sus datos
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombre = ""+snapshot.child("nombre").getValue();
                    String apellidos = ""+snapshot.child("apellidos").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    String direccion = ""+snapshot.child("direccion").getValue();
                    String edad = ""+snapshot.child("edad").getValue();
                    String estudios = ""+snapshot.child("estudios").getValue();
                    String profesion = ""+snapshot.child("profesion").getValue();
                    String telefono = ""+snapshot.child("telefono").getValue();
                    String imagen_perfil = ""+snapshot.child("imagen_perfil").getValue();


                    //Seteo de datos

                    Uid_Perfil.setText(uid);
                    Nombres_Perfil.setText(nombre);
                    Apellidos_Perfil.setText(apellidos);
                    Correo_Perfil.setText(correo);
                    Edad_Perfil.setText(edad);
                    Telefono_Perfil.setText(telefono);
                    Direccion_Perfil.setText(direccion);
                    Estudios_Perfil.setText(estudios);
                    Profesion_Perfil.setText(profesion);

                    Cargar_Imagen(imagen_perfil);


                } else {
                    Toast.makeText(Perfil_Usuario.this, "Esperando datos", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Perfil_Usuario.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Cargar_Imagen(String imagenPerfil) {
        try {
            //Esto se ejecuta cuando la imagen se ejecuta con exito desde Firebase
            Glide.with(getApplicationContext()).load(imagenPerfil).placeholder(R.drawable.perfilmenu).into(imagen_perfil);

        }catch (Exception e){
            //Si la imagen no se ejecuta con exito muestra esta imagen
            Glide.with(getApplicationContext()).load(R.drawable.perfilmenu).into(imagen_perfil);

        }
    }

    private void Establecer_tfl_usuario(){
        CountryCodePicker ccp;
        EditText Establecer_Telefono;
        Button Btn_Aceptar_tlf;

        dialog_establecer_tlf.setContentView(R.layout.cuadro_dialog_country_code);

        ccp = dialog_establecer_tlf.findViewById(R.id.ccp);
        Establecer_Telefono = dialog_establecer_tlf.findViewById(R.id.Establecer_Telefono);
        Btn_Aceptar_tlf = dialog_establecer_tlf.findViewById(R.id.Btn_Aceptar_tlf);

        Btn_Aceptar_tlf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo_pais = ccp.getSelectedCountryCodeWithPlus();
                String telefono = Establecer_Telefono.getText().toString();
                String codigo_pais_telefono = codigo_pais+telefono; //+51953456789

                if (!telefono.equals("")){
                    Telefono_Perfil.setText(codigo_pais_telefono);
                    dialog_establecer_tlf.dismiss();
                } else {
                    Toast.makeText(Perfil_Usuario.this, "Ingresa tu número", Toast.LENGTH_SHORT).show();
                    dialog_establecer_tlf.dismiss();
                }
            }
        });

        dialog_establecer_tlf.show();
        dialog_establecer_tlf.setCanceledOnTouchOutside(true);
    }

    private void AbrirCalendario(){
        final Calendar calendario = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        year = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Perfil_Usuario.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int YearSeleccionado, int MesSeleccionado, int DiaSeleccionado) {
                String diaFormateado, mesFormateado;
                // OBTENER DIA
                if(DiaSeleccionado < 10){
                    diaFormateado = "0"+String.valueOf(DiaSeleccionado);
                } else {
                    diaFormateado = String.valueOf(DiaSeleccionado);
                }
                //OBTENER EL MES
                int Mes = MesSeleccionado + 1;
                if (Mes < 10){
                    mesFormateado = "0" + String.valueOf(Mes);

                } else {
                    mesFormateado = String.valueOf(Mes);
                }

                //Seteamos la fecha en TextView
                Fecha_Nacimiento_Perfil.setText(diaFormateado + "/" + mesFormateado + "/"+ YearSeleccionado);
            }
        }
                ,year,mes,dia);
        datePickerDialog.show();
    }

    private void ComprobarInicioSesion(){
        if (user!=null){
            LecturaDeDAtos();
        } else {
            startActivity(new Intent(Perfil_Usuario.this, MenuPrincipal.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }
}