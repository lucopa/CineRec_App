package com.example.cinerec_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MenuPrincipal extends AppCompatActivity {

    ImageView btnBack;
    ImageView tvTitleToolbar;
    ImageView menuGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        initViews();
        initListener();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitleToolbar = findViewById(R.id.tv_title_toolbar);
        menuGallery = findViewById(R.id.menu_gallery_toolbar);
    }

    private void initListener() {
        btnBack.setOnClickListener(v -> finish());

        menuGallery.setOnClickListener(v -> Snackbar.make(v, "Opening Gallery...", Snackbar.LENGTH_SHORT).show());


    }
}
