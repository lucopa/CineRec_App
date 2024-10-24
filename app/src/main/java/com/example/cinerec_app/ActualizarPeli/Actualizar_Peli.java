package com.example.cinerec_app.ActualizarPeli;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinerec_app.AgregarPeli.Agregar_Peli;
import com.example.cinerec_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Set;

public class Actualizar_Peli extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView Id_peli_A, Uid_Usuario_A, Correo_Usuario_A, Fecha_registro_A, Fecha_A, Estado_A, estado_nuevo;
    EditText Titulo_A, Descripcion_A;
    Button btn_actualizar, Btn_Calendario_A;

    String id_peli_R, uid_usuario_R, correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    ImageView peli_vista, peli_pendiente, btnBack, save;

    Spinner spinner_estado;

    int dia, mes, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_peli);
        InicializarVistas();
        RecuperarDatos();
        SetearDatos();
        ComprobarEstadoPeli();
        Spinner_estado();
        initListener();

        Btn_Calendario_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeleccionarFecha();
            }
        });

    }

    private void InicializarVistas(){
        Id_peli_A = findViewById(R.id.Id_peli_A);
        Uid_Usuario_A = findViewById(R.id.Uid_Usuario_A);
        Correo_Usuario_A = findViewById(R.id.Correo_Usuario_A);
        Fecha_registro_A = findViewById(R.id.Fecha_registro_A);
        Fecha_A = findViewById(R.id.Fecha_A);
        Estado_A = findViewById(R.id.Estado_A);
        Titulo_A = findViewById(R.id.Titulo_A);
        btnBack = findViewById(R.id.btn_back);
        save = findViewById(R.id.save);
        Descripcion_A = findViewById(R.id.Descripcion_A);
        //btn_actualizar = findViewById(R.id.btn_actualizar);
        Btn_Calendario_A = findViewById(R.id.Btn_Calendario_A);

        peli_vista = findViewById(R.id.peli_vista);
        peli_pendiente = findViewById(R.id.peli_pendiente);

        spinner_estado = findViewById(R.id.spinner_estado);
        estado_nuevo = findViewById(R.id.estado_nuevo);


    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

        save.setOnClickListener(v -> ActualizarPeliBD());


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

    private void SetearDatos(){
        Id_peli_A.setText(id_peli_R);
        Uid_Usuario_A.setText(uid_usuario_R);
        Correo_Usuario_A.setText(correo_usuario_R);
        Fecha_registro_A.setText(fecha_registro_R);
        Titulo_A.setText(titulo_R);
        Descripcion_A.setText(descripcion_R);
        Fecha_A.setText(fecha_R);
        Estado_A.setText(estado_R);
    }

    private void ComprobarEstadoPeli(){
        String estado_peli = Estado_A.getText().toString();

        if(estado_peli.equals("Pendiente")){
            peli_pendiente.setVisibility(View.VISIBLE);
        }
        if (estado_peli.equals("Visto")){
            peli_vista.setVisibility(View.VISIBLE);
        }
    }

    private void SeleccionarFecha(){
        final Calendar calendario = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        year = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Actualizar_Peli.this, new DatePickerDialog.OnDateSetListener() {
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
                Fecha_A.setText(diaFormateado + "/" + mesFormateado + "/"+ YearSeleccionado);
            }
        }
                ,year,mes,dia);
        datePickerDialog.show();
    }

    private void Spinner_estado(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Estados_peli, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapter);
        spinner_estado.setOnItemSelectedListener(this);

    }

    private void ActualizarPeliBD(){
        String tituloActualizar = Titulo_A.getText().toString();
        String descripcionActualizar = Descripcion_A.getText().toString();
        String fechaActualizar = Fecha_A.getText().toString();
        String estadoAtualizar = estado_nuevo.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Peliculas_Publicadas");

        //Consulta
        Query query = databaseReference.orderByChild("id_peli").equalTo(id_peli_R);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("titulo").setValue(tituloActualizar);
                    ds.getRef().child("descripcion").setValue(descripcionActualizar);
                    ds.getRef().child("fecha_peli").setValue(fechaActualizar);
                    ds.getRef().child("estado").setValue(estadoAtualizar);
                }

                Toast.makeText(Actualizar_Peli.this, "Nota actualizada", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String ESTADO_ACTUAL = Estado_A.getText().toString();

        String Posicion_1 = adapterView.getItemAtPosition(1).toString();

        String estado_seleccionado = adapterView.getItemAtPosition(i).toString();
        estado_nuevo.setText(estado_seleccionado);

        if (ESTADO_ACTUAL.equals("Visto")){
            estado_nuevo.setText(Posicion_1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}