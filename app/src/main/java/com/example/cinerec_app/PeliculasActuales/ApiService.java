package com.example.cinerec_app.PeliculasActuales;

import com.example.cinerec_app.PeliculasActuales.Detalles.MovieDetailResponse;
import com.example.cinerec_app.PeliculasActuales.Detalles.Trailer.MovieVideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<MovieResponse> getMoviesInTheaters(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDetailResponse> getMovieDetails(
            @Path("id") int movieId,
            @Query("language") String language,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/videos")
    Call<MovieVideoResponse> getMovieVideos(@Path("movie_id") int movieId,
                                            @Query("api_key") String apiKey);
}
