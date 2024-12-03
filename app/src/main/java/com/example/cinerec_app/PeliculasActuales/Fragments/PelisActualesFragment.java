package com.example.cinerec_app.PeliculasActuales.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.PeliculasActuales.ApiClient;
import com.example.cinerec_app.PeliculasActuales.ApiService;
import com.example.cinerec_app.PeliculasActuales.Movie;
import com.example.cinerec_app.PeliculasActuales.MovieAdapter;
import com.example.cinerec_app.PeliculasActuales.MovieResponse;
import com.example.cinerec_app.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelisActualesFragment extends Fragment {

    private ImageView btnBack;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño para el Fragment
        View view = inflater.inflate(R.layout.fragment_pelis_actuales, container, false);

        // Inicializar vistas
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        recyclerView = view.findViewById(R.id.recycler_peliculas);

        // Configurar RecyclerView con GridLayoutManager de 2 columnas
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Llamar al método para obtener las películas
        fetchMovies();

        return view;
    }

    private void fetchMovies() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getPopularMovies("4a270cdd9146af7951a9a33ce4af681b").enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    // Configurar adaptador con las películas obtenidas
                    adapter = new MovieAdapter(getContext(), movies);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
