package com.example.cinerec_app.PeliculasActuales.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinerec_app.PeliculasActuales.MovieResponse;
import com.example.cinerec_app.R;
import com.example.cinerec_app.PeliculasActuales.Movie;
import com.example.cinerec_app.PeliculasActuales.MovieAdapter;
import com.example.cinerec_app.PeliculasActuales.ApiClient;
import com.example.cinerec_app.PeliculasActuales.ApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelisCarteleraFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private ImageView btnBack;

    public PelisCarteleraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pelis_cartelera, container, false);

        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        recyclerView = view.findViewById(R.id.recycler_cartelera);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        fetchMoviesInTheaters();
        return view;
    }

    private void fetchMoviesInTheaters() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMoviesInTheaters("4a270cdd9146af7951a9a33ce4af681b").enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getMovies();
                    adapter = new MovieAdapter(getContext(), movies);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar películas en cartelera", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
