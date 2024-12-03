package com.example.cinerec_app.PeliculasActuales.Detalles.Trailer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideoResponse {

    @SerializedName("results")
    private List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }

    public static class Video {

        @SerializedName("key")
        private String key;

        @SerializedName("type")
        private String type; // Declara el campo "type" que contiene el tipo del video (trailer, behind the scenes, etc.)

        public String getKey() {
            return key;
        }

        public String getType() {
            return type;
        }
    }
}
