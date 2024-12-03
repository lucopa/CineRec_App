package com.example.cinerec_app.PeliculasActuales;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinerec_app.PeliculasActuales.Detalles.MovieDetalle;
import com.example.cinerec_app.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        Glide.with(context).load(movie.getPosterPath()).into(holder.poster);

        // Mostrar la nota de la película
        holder.voteAverage.setText(String.format("%.1f", movie.getVoteAverage()));  // Formatear la nota a un decimal

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetalle.class);
            intent.putExtra("movie_id", movie.getId()); // Pasa el ID de la película
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView voteAverage;  // Para la nota de la película
        ImageView poster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_title);
            voteAverage = itemView.findViewById(R.id.movie_rating);  // Inicializar el TextView para la nota
            poster = itemView.findViewById(R.id.movie_poster);
        }
    }
}
