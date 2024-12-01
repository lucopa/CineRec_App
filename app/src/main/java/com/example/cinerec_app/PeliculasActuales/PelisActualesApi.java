package com.example.cinerec_app.PeliculasActuales;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelisActualesApi extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelis_actuales_api);

        recyclerView = findViewById(R.id.recycler_peliculas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchMovies();
    }

    private void fetchMovies() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getPopularMovies("4a270cdd9146af7951a9a33ce4af681b").enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    adapter = new MovieAdapter(PelisActualesApi.this, movies);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(PelisActualesApi.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
