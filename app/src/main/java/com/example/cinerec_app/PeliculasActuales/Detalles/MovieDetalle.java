package com.example.cinerec_app.PeliculasActuales.Detalles;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cinerec_app.PeliculasActuales.ApiClient;
import com.example.cinerec_app.PeliculasActuales.ApiService;
import com.example.cinerec_app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetalle extends AppCompatActivity {

     TextView titleTextView;
     TextView overviewTextView;
     TextView ratingTextView;
     TextView releaseDateTextView;
     ImageView posterImageView, btn_back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detalle);

        titleTextView = findViewById(R.id.movie_title);
        overviewTextView = findViewById(R.id.movie_overview);
        ratingTextView = findViewById(R.id.movie_rating);
        releaseDateTextView = findViewById(R.id.movie_release_date);
        posterImageView = findViewById(R.id.movie_poster);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(v -> finish());

        // Obtener el ID de la película que se pasó como extra en el Intent
        int movieId = getIntent().getIntExtra("movie_id", -1);

        // Si no se pasó el ID de la película, mostramos un mensaje de error
        if (movieId == -1) {
            Toast.makeText(this, "Error: No se proporcionó el ID de la película", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Llamar a la API para obtener los detalles de la película
        fetchMovieDetails(movieId);
    }

    private void fetchMovieDetails(int movieId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMovieDetails(movieId,"es", "4a270cdd9146af7951a9a33ce4af681b").enqueue(new Callback<MovieDetailResponse>() {
            @Override
            public void onResponse(Call<MovieDetailResponse> call, Response<MovieDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetailResponse movieDetail = response.body();
                    titleTextView.setText(movieDetail.getTitle());
                    overviewTextView.setText(movieDetail.getOverview());
                    ratingTextView.setText(String.format("%.1f", movieDetail.getVoteAverage())); // Formatear la calificación
                    releaseDateTextView.setText(movieDetail.getReleaseDate());
                    Glide.with(MovieDetalle.this)
                            .load("https://image.tmdb.org/t/p/w500" + movieDetail.getPosterPath())
                            .into(posterImageView);
                } else {
                    // Si no se obtiene una respuesta válida
                    Toast.makeText(MovieDetalle.this, "Error al obtener los detalles de la película", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetailResponse> call, Throwable t) {
                // Manejo de error en caso de fallo de la conexión o la llamada API
                Toast.makeText(MovieDetalle.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
