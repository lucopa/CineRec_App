package com.example.cinerec_app.PeliculasActuales;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.cinerec_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PelisApi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelis_api);

        // Configurar el FragmentContainerView (NavHostFragment)
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_hostfragment2);
        if (navHostFragment != null) {
            // Obtener el NavController
            NavController navController = navHostFragment.getNavController();

            // Configuración del BottomNavigationView
            setUpNavigation(navController);
        } else {
            Log.e("NavHost", "NavHostFragment no encontrado");
        }
    }

    // Configura la navegación con el BottomNavigationView
    private void setUpNavigation(NavController navController) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation2);

        // Sincronizar el BottomNavigationView con el NavController
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
