package com.derrick.popularmoviesstage1.Data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/{sorting_string}")
    Call<Movie> getPopularMovies(@Path("sorting_string") String sorting_string, @Query("api_key") String apiKey);
}
