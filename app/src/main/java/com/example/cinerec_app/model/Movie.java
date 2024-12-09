package com.example.cinerec_app.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    private int id;  // Campo id de la película

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_average")
    private double voteAverage;

    // Getters
    public int getId() {
        return id;  // Método para obtener el ID de la película
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }
}
