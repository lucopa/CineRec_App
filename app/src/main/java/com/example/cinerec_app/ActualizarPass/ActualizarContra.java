package com.example.cinerec_app.ActualizarPass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import com.example.cinerec_app.Login;
import com.example.cinerec_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ActualizarContra extends AppCompatActivity {

    TextView actualContraTXT, actualContra;
    EditText contraseñaActual, contraseñaNueva, contraseñaRepetida;
    Button actualizarButton;
    ImageView btnBack;

    DatabaseReference BD_Usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contra);

        InitListener();
        LeerDatos();

        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Contra_Actual = contraseñaActual.getText().toString().trim();
                String Nueva_Contra =  contraseñaNueva.getText().toString().trim();
                String RNueva_Contra = contraseñaRepetida.getText().toString().trim();

                if (Contra_Actual.equals("")){
                    contraseñaActual.setError("La contraseña actual esta vacio");
                } else if (Nueva_Contra.equals("")) {
                    contraseñaNueva.setError("La contraseña nueva esta vacio");

                } else if (RNueva_Contra.equals("")) {
                    contraseñaRepetida.setError("La contraseña repetida esta vacia");

                } else if (!Nueva_Contra.equals(RNueva_Contra)) {
                    Toast.makeText(ActualizarContra.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();

                } else if (Nueva_Contra.length()<6) {
                    contraseñaNueva.setError("Tiene que tener 6 o mas caracteres");
                } else {
                    Actualizar_Contrasena(Contra_Actual, Nueva_Contra);
                }
            }
        });
    }

    private void Actualizar_Contrasena(String contraActual, String nuevaContra) {
        progressDialog.show();
        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor");

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), contraActual);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(nuevaContra)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        String Nueva_contra = contraseñaNueva.getText().toString().trim();
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("contraseña", Nueva_contra);
                                        BD_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
                                        BD_Usuarios.child(user.getUid()).updateChildren(hashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ActualizarContra.this, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                                                        firebaseAuth.signOut();
                                                        Intent intent = new Intent(ActualizarContra.this, Login.class).
                                                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(ActualizarContra.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ActualizarContra.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ActualizarContra.this, "La contraseña actual no es correcta", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void InitListener(){
        actualContraTXT = findViewById(R.id.actualContraTXT);
        actualContra = findViewById(R.id.actualContra);
        contraseñaActual = findViewById(R.id.contraseñaActual);
        contraseñaNueva = findViewById(R.id.contraseñaNueva);
        contraseñaRepetida = findViewById(R.id.contraseñaRepetida);
        actualizarButton = findViewById(R.id.actualizarButton);
        btnBack = findViewById(R.id.btn_back);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        btnBack.setOnClickListener(v -> finish());

        progressDialog = new ProgressDialog(ActualizarContra.this);

    }

    private void LeerDatos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pass = ""+snapshot.child("contraseña").getValue();
                actualContra.setText(pass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}