/*package com.example.cinerec_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinerec_app.databinding.ActivityMenuPrincipal2Binding;
import com.google.android.material.navigation.NavigationBarView;

public class MenuPrincipal2 extends AppCompatActivity {

    ActivityMenuPrincipal2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuPrincipal2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurando el BottomNavigationView
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item == R.id.info) {
                    Toast.makeText(MenuPrincipal2.this, "Mostrando información", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (item == R.id.cerrar) {// Aquí puedes implementar el método SalirAplicacion()
                    SalirAplicacion();
                    return true;
                } else if (item == R.id.perfil) {
                    Toast.makeText(MenuPrincipal2.this, "Mostrando perfil", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            }

    }

    private void SalirAplicacion() {
        // Aquí puedes implementar la lógica para cerrar sesión
        Toast.makeText(this, "Cerraste sesión", Toast.LENGTH_SHORT).show();
    }
}
*/