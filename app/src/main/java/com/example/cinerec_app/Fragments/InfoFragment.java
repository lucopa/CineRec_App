package com.example.cinerec_app.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.cinerec_app.R;

public class InfoFragment extends Fragment {

    Button contact_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        contact_button = rootView.findViewById(R.id.contact_button);

        // Configurar el evento de clic para el botón
        contact_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Usamos getContext() para acceder al contexto adecuado
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    llamar();
                } else {
                    PermisoLLamada.launch(Manifest.permission.CALL_PHONE);
                }
            }
        });

        return rootView;
    }

    private void llamar() {
        String telefono = "654123878"; // Número de teléfono

        if (!telefono.equals("")) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telefono)); // Prefijo "tel:" para el número
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "El contacto no tiene un teléfono registrado", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<String> PermisoLLamada =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    llamar();
                } else {
                    Toast.makeText(getContext(), "Permiso rechazado", Toast.LENGTH_SHORT).show();
                }
            });
}
