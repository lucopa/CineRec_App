package com.example.cinerec_app.Ajustes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinerec_app.MainActivity;
import com.example.cinerec_app.MenuPrincipal;
import com.example.cinerec_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Ajustes extends AppCompatActivity {

    TextView Uid_Eliminar, EliminarCuenta;
    ImageView btnBack;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ajustes);

        InitVariables();
        ObtenerUid();

        EliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EliminarUsuarioAutenticacion();
            }
        });

    }



    private void EliminarUsuarioAutenticacion() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Ajustes.this);
        alertDialog.setTitle("¿Estas seguro/a de que quieres borrar esta cuenta?");
        alertDialog.setMessage("Su cuenta se borrará y no podrá ser recuperada");
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            EliminarUsuarioBD();
                            Intent intent = new Intent(Ajustes.this, MenuPrincipal.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Ajustes.this, "Se ha eliminado la cuenta", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Ajustes.this, "Ha habido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Ajustes.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Ajustes.this, "Cancelada la acción", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.create().show();
    }

    private void EliminarUsuarioBD(){
        String uid_eliminar = Uid_Eliminar.getText().toString();
        Query query = databaseReference.child(uid_eliminar);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().removeValue();
                }
                Toast.makeText(Ajustes.this, "Se ha eliminado la cuenta de la BD", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InitVariables(){
        EliminarCuenta = findViewById(R.id.EliminarCuenta);
        Uid_Eliminar = findViewById(R.id.Uid_Eliminar);
        btnBack = findViewById(R.id.btn_back);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");

        btnBack.setOnClickListener(v -> finish());


    }



    private void ObtenerUid(){
        String uid = getIntent().getStringExtra("Uid");
        Uid_Eliminar.setText(uid);

    }
}