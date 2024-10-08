package com.example.cinerec_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PantallaCarga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);


       final int TIEMPO = 5000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PantallaCarga.this, MainActivity.class ));
                finish();
            }
        }, TIEMPO);

        ImageView imageView = findViewById(R.id.imageGif);

        Glide.with(this)
                .load(R.drawable.cine)
                .into(imageView);

    }
}