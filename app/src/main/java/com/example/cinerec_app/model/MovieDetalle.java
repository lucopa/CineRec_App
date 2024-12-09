package com.example.cinerec_app.model;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cinerec_app.presenter.ApiClient;
import com.example.cinerec_app.presenter.ApiService;
import com.example.cinerec_app.presenter.MovieVideoResponse;
import com.example.cinerec_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetalle extends AppCompatActivity {

    TextView titleTextView, overviewTextView, ratingTextView, releaseDateTextView;
    ImageView posterImageView, btn_back, trailerThumbnail;
    WebView trailerWebView;
    FloatingActionButton fabTrailer;

    String trailerUrl;  // Para almacenar la URL del trailer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detalle);

        // Referencias a las vistas del layout
        titleTextView = findViewById(R.id.movie_title);
        overviewTextView = findViewById(R.id.movie_overview);
        ratingTextView = findViewById(R.id.movie_rating);
        releaseDateTextView = findViewById(R.id.movie_release_date);
        posterImageView = findViewById(R.id.movie_poster);
        btn_back = findViewById(R.id.btn_back);
        trailerWebView = findViewById(R.id.webview_trailer);  // WebView para el trailer
        trailerThumbnail = findViewById(R.id.trailer_thumbnail);  // ImageView para la miniatura
        fabTrailer = findViewById(R.id.fab_trailer);

        // Configurar el botón de retroceso
        btn_back.setOnClickListener(v -> finish());

        // Obtener el ID de la película desde el Intent
        int movieId = getIntent().getIntExtra("movie_id", -1);

        if (movieId == -1) {
            Toast.makeText(this, "Error: No se proporcionó el ID de la película", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Obtener los detalles de la película
        fetchMovieDetails(movieId);
    }

    private void fetchMovieDetails(int movieId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMovieDetails(movieId, "es", "4a270cdd9146af7951a9a33ce4af681b").enqueue(new Callback<MovieDetailResponse>() {
            @Override
            public void onResponse(Call<MovieDetailResponse> call, Response<MovieDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetailResponse movieDetail = response.body();
                    titleTextView.setText(movieDetail.getTitle());
                    overviewTextView.setText(movieDetail.getOverview());
                    ratingTextView.setText(String.format("%.1f", movieDetail.getVoteAverage()));
                    releaseDateTextView.setText(movieDetail.getReleaseDate());
                    Glide.with(MovieDetalle.this)
                            .load("https://image.tmdb.org/t/p/w500" + movieDetail.getPosterPath())
                            .into(posterImageView);

                    // Llamar al método para obtener el trailer después de cargar los detalles
                    fetchMovieTrailer(movieId);
                } else {
                    Toast.makeText(MovieDetalle.this, "Error al obtener los detalles de la película", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetailResponse> call, Throwable t) {
                Toast.makeText(MovieDetalle.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMovieTrailer(int movieId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMovieVideos(movieId, "4a270cdd9146af7951a9a33ce4af681b").enqueue(new Callback<MovieVideoResponse>() {
            @Override
            public void onResponse(Call<MovieVideoResponse> call, Response<MovieVideoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MovieVideoResponse.Video> videos = response.body().getVideos();
                    MovieVideoResponse.Video trailer = null;

                    // Filtrar solo los videos de tipo "Trailer"
                    for (MovieVideoResponse.Video video : videos) {
                        if ("Trailer".equalsIgnoreCase(video.getType())) {
                            trailer = video;
                            break;  // Solo tomamos el primer trailer disponible
                        }
                    }

                    if (trailer != null) {
                        String videoKey = trailer.getKey();
                        String thumbnailUrl = "https://img.youtube.com/vi/" + videoKey + "/0.jpg"; // URL de la miniatura

                        // Mostrar la miniatura del trailer
                        Glide.with(MovieDetalle.this)
                                .load(thumbnailUrl)
                                .into(trailerThumbnail);

                        // Detectar clic en la miniatura y cargar el trailer
                        fabTrailer.setOnClickListener(v -> {
                            // Cargar el trailer completo en el WebView
                            trailerUrl = "https://www.youtube.com/watch?v=" + videoKey;
                            trailerWebView.setVisibility(View.VISIBLE);  // Hacer visible el WebView
                            trailerWebView.loadUrl(trailerUrl);  // Cargar la URL completa
                        });
                    } else {
                        Toast.makeText(MovieDetalle.this, "No hay trailers oficiales disponibles", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MovieDetalle.this, "Error al obtener el trailer", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieVideoResponse> call, Throwable t) {
                Toast.makeText(MovieDetalle.this, "Error al obtener el trailer", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
