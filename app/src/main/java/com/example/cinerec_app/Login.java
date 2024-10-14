package com.example.cinerec_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextView textoRegistro;
    EditText correoLogin, contraseñaLogin;
    Button loginButton;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    //Validar los datos
    String correo = "", contraseña = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        correoLogin = findViewById(R.id.correoLogin);
        contraseñaLogin = findViewById(R.id.contraseñaLogin);
        loginButton = findViewById(R.id.loginButton);
        textoRegistro = findViewById(R.id.textoRegistro);
        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarDatos();
            }
        });


        textoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registro.class));

            }
        });
    }

    private void ValidarDatos() {

        correo = correoLogin.getText().toString();
        contraseña = contraseñaLogin.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(contraseña)){
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }
        else {
            LoginUsuario(); //Si las dos condiciones anteriores no se cumplen, se hara esta
        }
    }

    private void LoginUsuario() {
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, MenuPrincipal.class));
                            Toast.makeText(Login.this, "Bienvenido(a): "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Verifique si el correo y contraseña son correctos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}