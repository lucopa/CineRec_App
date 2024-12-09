package com.example.cinerec_app.view.contactos;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinerec_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class Actualizar_Contacto extends AppCompatActivity {

    ImageView imagen_perfil_contacto, editar_imagen_contacto, EditarTelefonoContacto, btn_back;
    TextView  Id_Contacto_A, Uid_Contacto_A, Telefono_Contacto_A;
    EditText Nombre_Contacto_A, Apellidos_Contacto_A, Edad_Contacto_A, Direccion_Contacto_A, Correo_Contacto_A;
    Button Guardar_Datos_Contacto;

    String id_c, uid_usuario, nombres_c, apellidos_c, correo_c, telefono_c, edad_c, direccion_c;

    Dialog dialog_establecer_tlf;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Uri imagenUri = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contacto);

        InitListener();
        RecuperarDatos();
        SetDatosRecuperados();
        ObtenerImagen();

        EditarTelefonoContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Establecer_tfl_usuario();
            }
        });

        Guardar_Datos_Contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarInfoContacto();
            }
        });

        editar_imagen_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeleccionarImagenGaleria();
            }
        });

        progressDialog = new ProgressDialog(Actualizar_Contacto.this);
        progressDialog.setTitle("Espera por favor");
        progressDialog.setCanceledOnTouchOutside(false);

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
        EditarTelefonoContacto = findViewById(R.id.EditarTelefonoContacto);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(v -> finish());

        dialog_establecer_tlf = new Dialog(Actualizar_Contacto.this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

    }

    private void RecuperarDatos(){
        Bundle bundle = getIntent().getExtras();
        id_c = bundle.getString("id_c");
        uid_usuario = bundle.getString("uid_usuario");
        nombres_c = bundle.getString("nombre_c");
        apellidos_c = bundle.getString("apellidos_c");
        edad_c = bundle.getString("edad_c");
        telefono_c = bundle.getString("telefono_c");
        correo_c = bundle.getString("correo_c");
        direccion_c = bundle.getString("direccion_c");



    }

    private void SetDatosRecuperados(){
        Id_Contacto_A.setText(id_c);
        Uid_Contacto_A.setText(uid_usuario);
        Nombre_Contacto_A.setText(nombres_c);
        Apellidos_Contacto_A.setText(apellidos_c);
        Correo_Contacto_A.setText(correo_c);
        Telefono_Contacto_A.setText(telefono_c);
        Edad_Contacto_A.setText(edad_c);
        Direccion_Contacto_A.setText(direccion_c);

    }

    private void ObtenerImagen(){
        String imagen_c = getIntent().getStringExtra("imagen_c");

        try {
            Glide.with(getApplicationContext())
                    .load(imagen_c)  // Carga la imagen
                    .placeholder(R.drawable.usuario)  // Imagen por defecto mientras se carga
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))  // Ajusta el valor según el radio del borde
                    .into(imagen_perfil_contacto);  // Aplica la imagen al ImageView
            
        } catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Telefono_Contacto_A.setText(codigo_pais_telefono);
                    dialog_establecer_tlf.dismiss();
                } else {
                    Toast.makeText(Actualizar_Contacto.this, "Ingresa tu número", Toast.LENGTH_SHORT).show();
                    dialog_establecer_tlf.dismiss();
                }
            }
        });

        dialog_establecer_tlf.show();
        dialog_establecer_tlf.setCanceledOnTouchOutside(true);
    }

    private void ActualizarInfoContacto(){
        String NombresActualizar = Nombre_Contacto_A.getText().toString().trim();
        String ApellidosActualizar = Apellidos_Contacto_A.getText().toString().trim();
        String CorreoActualizar = Correo_Contacto_A.getText().toString().trim();
        String TelefonoActualizar = Telefono_Contacto_A.getText().toString().trim();
        String EdadActualizar = Edad_Contacto_A.getText().toString().trim();
        String DireccionActualizar = Direccion_Contacto_A.getText().toString().trim();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");

        Query query = databaseReference.child(user.getUid()).child("Contactos").orderByChild("id_contacto").equalTo(id_c);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("nombre").setValue(NombresActualizar);
                    ds.getRef().child("apellidos").setValue(ApellidosActualizar);
                    ds.getRef().child("correo").setValue(CorreoActualizar);
                    ds.getRef().child("telefono").setValue(TelefonoActualizar);
                    ds.getRef().child("edad").setValue(EdadActualizar);
                    ds.getRef().child("direccion").setValue(DireccionActualizar);


                }
                Toast.makeText(Actualizar_Contacto.this, "Informacion Actualizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void subirImagenStorage(){
        progressDialog.setMessage("Subiendo imagen");
        progressDialog.show();
        String id_c = getIntent().getStringExtra("id_c");

        String carpetaImagenesContactos = "ImagenesContactos/";
        String NombreImagen = carpetaImagenesContactos+id_c;
        StorageReference reference = FirebaseStorage.getInstance().getReference(NombreImagen);
        reference.putFile(imagenUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uriIMAGEN = ""+uriTask.getResult();
                        AactualizarImagenBD(uriIMAGEN);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Actualizar_Contacto.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void AactualizarImagenBD(String uriIMAGEN) {
        progressDialog.setMessage("Actualizando...");
        progressDialog.show();

        String id_c = getIntent().getStringExtra("id_c");

        HashMap<String, Object> hashMap = new HashMap<>();
        if (imagenUri !=null){
            hashMap.put("imagen", ""+uriIMAGEN);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(user.getUid()).child("Contactos").child(id_c)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Actualizar_Contacto.this, "Imagen actualizada con exito", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Actualizar_Contacto.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SeleccionarImagenGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> galeriaActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imagenUri = data.getData();
                        imagen_perfil_contacto.setImageURI(imagenUri);
                        subirImagenStorage();
                    } else {
                        Toast.makeText(Actualizar_Contacto.this, "Cancelado ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );
}