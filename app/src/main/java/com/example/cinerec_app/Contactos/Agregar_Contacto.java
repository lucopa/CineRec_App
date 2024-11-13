package com.example.cinerec_app.Contactos;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerec_app.MenuPrincipal;
import com.example.cinerec_app.Objetos.Contacto;
import com.example.cinerec_app.Perfil.Perfil_Usuario;
import com.example.cinerec_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class Agregar_Contacto extends AppCompatActivity {

    TextView Uid_UsuarioC,Telefono_Contacto;
    EditText Nombre_Contacto, Apellidos_Contacto, Correo_Contacto, Edad_Contacto,Direccion_Contacto;
    Button Btn_Guardar_Contacto;
    ImageView Editar_Telefono_C;
    Dialog dialog_establecer_tlf;

    DatabaseReference BD_Usuarios;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);
        initListener();
        ObtenerUidUsuario();

        Editar_Telefono_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Establecer_tfl_contacto();
            }
        });

        Btn_Guardar_Contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AñadirContacto();
            }
        });
    }

    private void initListener(){
        Uid_UsuarioC = findViewById(R.id.Uid_UsuarioC);
        Nombre_Contacto = findViewById(R.id.Nombre_Contacto);
        Apellidos_Contacto = findViewById(R.id.Apellidos_Contacto);
        Correo_Contacto = findViewById(R.id.Correo_Contacto);
        Telefono_Contacto = findViewById(R.id.Telefono_Contacto);
        Edad_Contacto = findViewById(R.id.Edad_Contacto);
        Direccion_Contacto = findViewById(R.id.Direccion_Contacto);
        Editar_Telefono_C = findViewById(R.id.Editar_Telefono_C);
        Btn_Guardar_Contacto = findViewById(R.id.Btn_Guardar_Contacto);

        dialog_establecer_tlf = new Dialog(Agregar_Contacto.this);
        BD_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


    }

    private void ObtenerUidUsuario(){
        String UidRecuperado = getIntent().getStringExtra("Uid");
        Uid_UsuarioC.setText(UidRecuperado);
    }

    private void Establecer_tfl_contacto(){
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
                    Telefono_Contacto.setText(codigo_pais_telefono);
                    dialog_establecer_tlf.dismiss();
                } else {
                    Toast.makeText(Agregar_Contacto.this, "Ingresa tu número", Toast.LENGTH_SHORT).show();
                    dialog_establecer_tlf.dismiss();
                }
            }
        });

        dialog_establecer_tlf.show();
        dialog_establecer_tlf.setCanceledOnTouchOutside(true);
    }

    private void AñadirContacto(){
        //Obtener los datos

        String uid = Uid_UsuarioC.getText().toString();
        String nombre = Nombre_Contacto.getText().toString();
        String apellidos = Apellidos_Contacto.getText().toString();
        String correo = Correo_Contacto.getText().toString();
        String telefono = Telefono_Contacto.getText().toString();
        String edad = Edad_Contacto.getText().toString();
        String direccion = Direccion_Contacto.getText().toString();

        //Creamos la cadena unica
        String id_contacto = BD_Usuarios.push().getKey();

        //Validar los datos
        if (!uid.equals("") && !nombre.equals("")){
            Contacto contacto = new Contacto(
                    id_contacto, uid, nombre, apellidos, correo, telefono, edad, direccion,  "");

            //Establecer el nombre de la base de datos
            String Nombre_BD = "Contactos";
            assert id_contacto != null;
            BD_Usuarios.child(user.getUid()).child(Nombre_BD).child(id_contacto).setValue(contacto);
            Toast.makeText(this, "Contacto agregado", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            Toast.makeText(this, "Completa un campo del contacto", Toast.LENGTH_SHORT).show();
        }

    }


}