package com.example.cinerec_app.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinerec_app.Ajustes.Ajustes;
import com.example.cinerec_app.MainActivity;
import com.example.cinerec_app.PeliculasActuales.PelisApi;
import com.example.cinerec_app.R;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.cinerec_app.Peliculas.Agregar_Peli;
import com.example.cinerec_app.Contactos.Listar_Contactos;
import com.example.cinerec_app.Peliculas.Listar_Peli;
import com.example.cinerec_app.Perfil.Perfil_Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipalFragment extends Fragment {

    ViewFlipper v_flipper;

    ImageView btnBack, perfil, AgregarPeli, ListarPeli, Contactos, ajustes, perfilusuario;
    FirebaseAuth firebaseAuth;
    Button  Archivados, cerrar, EstadoCuentaPrincipal;

    LinearLayoutCompat Linear_Verificado, Linear_Nombre, Linear_Correo;
    TextView UidPrincipal, NombresPrincipal, CorreoPrincipal;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    DatabaseReference Usuarios;
    Dialog dialog_cuenta_verificada;
    FirebaseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout
        View rootView = inflater.inflate(R.layout.fragment_menu_principal, container, false);


        // Inicializa las vistas desde rootView
        AgregarPeli = rootView.findViewById(R.id.AgregarPeli);
        ListarPeli = rootView.findViewById(R.id.ListarPeli);
        Contactos = rootView.findViewById(R.id.Contactos);
        btnBack = rootView.findViewById(R.id.btn_back);
        perfil = rootView.findViewById(R.id.perfil);
        UidPrincipal = rootView.findViewById(R.id.UidPrincipal);
        NombresPrincipal = rootView.findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = rootView.findViewById(R.id.CorreoPrincipal);
        progressBar = rootView.findViewById(R.id.progressBar);
        Linear_Verificado = rootView.findViewById(R.id.Linear_Verificado);
        Linear_Nombre = rootView.findViewById(R.id.Linear_Nombre);
        Linear_Correo = rootView.findViewById(R.id.Linear_Correo);
        EstadoCuentaPrincipal = rootView.findViewById(R.id.EstadoCuentaPrincipal);
        ajustes = rootView.findViewById(R.id.ajustes);
        perfilusuario = rootView.findViewById(R.id.perfilusuario);

        int images[] = {R.drawable.wicked, R.drawable.gladiator, R.drawable.anora, R.drawable.substance, R.drawable.suicidesquad, R.drawable.gonegirl, R.drawable.dune, R.drawable.twisters, R.drawable.oppenheimer};

        v_flipper = rootView.findViewById(R.id.v_flipper);

        for (int image: images){
            flipperImagenes(image);
        }



        // Configura las acciones de los botones
        AgregarPeli.setOnClickListener(view -> {
            scaleDownUpAnimation(view);
            String uid_usuario = UidPrincipal.getText().toString();
            String correo_usuario = CorreoPrincipal.getText().toString();
            Intent intent = new Intent(getActivity(), Agregar_Peli.class);
            intent.putExtra("Uid", uid_usuario);
            intent.putExtra("Correo", correo_usuario);
            startActivity(intent);
        });

        ListarPeli.setOnClickListener(view -> {
            scaleDownUpAnimation(view);
            startActivity(new Intent(getActivity(), Listar_Peli.class));
            Toast.makeText(getActivity(), "Listar Peliculas", Toast.LENGTH_SHORT).show();
        });


        Contactos.setOnClickListener(view -> {
            scaleDownUpAnimation(view);
            String uid_usuario = UidPrincipal.getText().toString();
            Intent intent = new Intent(getActivity(), Listar_Contactos.class);
            intent.putExtra("Uid", uid_usuario);
            startActivity(intent);
        });


       btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();  // Cierra la actividad que aloja este fragmento
            }
        });

       perfil.setOnClickListener(v ->  startActivity(new Intent(getActivity(), Perfil_Usuario.class)));

        v_flipper.setOnClickListener(v -> startActivity(new Intent(getActivity(), PelisApi.class)));

        ajustes.setOnClickListener(v -> {
            String uid_usuario = UidPrincipal.getText().toString();
            Intent intent = new Intent(getActivity(), Ajustes.class);
            intent.putExtra("Uid", uid_usuario);
            startActivity(intent);
        });



        // Inicializa Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Espera un momento...");
        progressDialog.setCanceledOnTouchOutside(false);

        // Verifica estado de cuenta
        EstadoCuentaPrincipal.setOnClickListener(view -> {
            if (user.isEmailVerified()) {
                Toast.makeText(getActivity(), "Cuenta ya verificada", Toast.LENGTH_SHORT).show();
                AnimacionCuentaVerificada();
            } else {
                VerificarCuentaCorreo();
            }
        });

        return rootView;
    }

    public void scaleDownUpAnimation(View view) {
        Animation scaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down_up);
        view.startAnimation(scaleAnimation);
    }




    public void flipperImagenes(int image){
        ImageView imageView = new ImageView(getActivity());

        Glide.with(getActivity())
                .load(image)
                .transform(new CenterCrop(), new RoundedCorners(20))  // 16dp de radio de esquina
                .into(imageView);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getActivity(), android.R.anim.slide_out_right);



    }



    private void VerificarCuentaCorreo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Verificar Cuenta")
                .setMessage("¿Confirmas que quieres que las instrucciones de verificación se envíen a tu correo electrónico?" + user.getEmail())
                .setPositiveButton("Enviar", (dialogInterface, i) -> EnviarCorreoVerificacion())
                .setNegativeButton("Cancelar", (dialogInterface, i) -> Toast.makeText(getActivity(), "Anulado por el usuario", Toast.LENGTH_SHORT).show())
                .show();
    }

    private void EnviarCorreoVerificacion() {
        progressDialog.setMessage("Enviando las instrucciones de verificación a su correo electrónico" + user.getEmail());
        progressDialog.show();

        user.sendEmailVerification()
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Instrucciones enviadas, revise su bandeja", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Fallo debido a: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void VerificarEstadoCuenta() {
        String Verificado = "Verificado";
        String No_Verificado = "No Verificado";

        if (user.isEmailVerified()) {
            EstadoCuentaPrincipal.setText(Verificado);
            EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(34, 153, 84));
        } else {
            EstadoCuentaPrincipal.setText(No_Verificado);
            EstadoCuentaPrincipal.setBackgroundColor(Color.rgb(231, 76, 60));
        }
    }

    private void AnimacionCuentaVerificada() {
        Button EntendidoVerificado;
        dialog_cuenta_verificada = new Dialog(getActivity());
        dialog_cuenta_verificada.setContentView(R.layout.dialog_cuenta_verificada);
        EntendidoVerificado = dialog_cuenta_verificada.findViewById(R.id.EntendidoVerificado);
        EntendidoVerificado.setOnClickListener(view -> dialog_cuenta_verificada.dismiss());
        dialog_cuenta_verificada.show();
        dialog_cuenta_verificada.setCanceledOnTouchOutside(false);
    }



    @Override
    public void onStart() {
        super.onStart();
        ComprobarInicioSesion();
    }

    private void ComprobarInicioSesion() {
        if (user != null) {
            CargarDatos();
        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    private void CargarDatos() {
        VerificarEstadoCuenta();
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    Linear_Nombre.setVisibility(View.VISIBLE);
                    Linear_Correo.setVisibility(View.VISIBLE);
                    Linear_Verificado.setVisibility(View.VISIBLE);

                    String uid = "" + snapshot.child("uid").getValue();
                    String nombre = "" + snapshot.child("nombre").getValue();
                    String correo = "" + snapshot.child("correo").getValue();
                    String imagen = "" + snapshot.child("imagen_perfil").getValue();

                    UidPrincipal.setText(uid);
                    NombresPrincipal.setText(nombre);
                    CorreoPrincipal.setText(correo);

                    AgregarPeli.setEnabled(true);
                    ListarPeli.setEnabled(true);
                    Contactos.setEnabled(true);

                    ObtenerImagenPerfil(imagen);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ObtenerImagenPerfil(String imagen) {
        try {
            // Si la imagen ha cargado exitosamente
            Glide.with(getActivity())
                    .load(imagen)  // Carga la imagen desde la URL obtenida
                    .placeholder(R.drawable.perfilmenu)  // Muestra una imagen de marcador de posición mientras carga
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(perfilusuario);  // Carga la imagen en el ImageView
        } catch (Exception e) {
            // Si la imagen no se ha cargado correctamente (en caso de error)
            Glide.with(getActivity())
                    .load(R.drawable.perfilmenu)  // Muestra una imagen predeterminada en caso de error
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                    .into(perfilusuario);
        }
    }

}
