package com.derrick.popularmoviesstage1.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/popular")
    Call<Movie> getPopularMovies(@Query("api_key") String apiKey);
}
