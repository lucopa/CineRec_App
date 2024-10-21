package com.example.cinerec_app.AgregarPeli;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerec_app.Objetos.Pelicula;
import com.example.cinerec_app.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Agregar_Peli extends AppCompatActivity {

    TextView  Uid_Usuario,Correo_Usuario,fecha_hora,Fecha,Estado;
    EditText Titulo, Descripcion;
    Button Btn_Calendario,btn_añadir;
    ImageView btnBack, add_film;

    int dia,mes,year;

    DatabaseReference BD_Firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_peli);

        InicializarVariables();
        ObtenerDatos();
        ObtenerFechayHora();
        initListener();

        //Logica Boton Añadir Pelicula
        btn_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarPeli();
            }
        });
        //Logica para el boton Calendario

        Btn_Calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();

                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                year = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Agregar_Peli.this, new DatePickerDialog.OnDateSetListener() {
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
                        Fecha.setText(diaFormateado + "/" + mesFormateado + "/"+ YearSeleccionado);
                    }
                }
                ,year,mes,dia);
                datePickerDialog.show();
            }
        });

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
        btn_añadir = findViewById(R.id.btn_añadir);
        add_film = findViewById(R.id.add_film);

        BD_Firebase = FirebaseDatabase.getInstance().getReference();

    }

    private void ObtenerDatos(){
        String uid_recuperado = getIntent().getStringExtra("Uid");
        String correo_recuperado = getIntent().getStringExtra("Correo");

        Uid_Usuario.setText(uid_recuperado);
        Correo_Usuario.setText(correo_recuperado);


    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

        add_film.setOnClickListener(v -> AgregarPeli());


    }

    private void ObtenerFechayHora(){
        String fecha_hora_registro = new SimpleDateFormat("dd-MM-yyyy/HH-mm-ss a",
                Locale.getDefault()).format(System.currentTimeMillis());

        fecha_hora.setText(fecha_hora_registro);
    }

    private void AgregarPeli(){
        //Obtener los datos

        String uid_usuario = Uid_Usuario.getText().toString();
        String correo_usuario = Correo_Usuario.getText().toString();
        String fecha_hora_Actual = fecha_hora.getText().toString();
        String titulo = Titulo.getText().toString();
        String descripcion = Descripcion.getText().toString();
        String fecha = Fecha.getText().toString();
        String estado = Estado.getText().toString();

        //Validar datos
        if (!uid_usuario.equals("") && !correo_usuario.equals("") && !fecha_hora_Actual.equals("") &&
        !titulo.equals("") && !descripcion.equals("") && !fecha.equals("") && !estado.equals("")){

            Pelicula pelicula = new Pelicula(correo_usuario+"/"+fecha_hora_Actual,uid_usuario,
                    correo_usuario,
                    fecha_hora_Actual,
                    titulo,
                    descripcion,
                    fecha,
                    estado);
            String pelicula_usuario = BD_Firebase.push().getKey();
            //Establecer el nombre de la BD
            String nombre_BD = "Peliculas_Publicadas";

            BD_Firebase.child(nombre_BD).child(pelicula_usuario).setValue(pelicula);
            onBackPressed();

            Toast.makeText(this, "Pelicula añadida correctamente", Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this, "LLenar todos los campos", Toast.LENGTH_SHORT).show();
        }

    }


}