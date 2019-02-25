package com.derrick.popularmoviesstage1.Data.network;

import android.content.Context;

import com.derrick.popularmoviesstage1.BuildConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieNetworkDataSource {

    private static final String LOG_TAG = MovieNetworkDataSource.class.getSimpleName();
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MovieNetworkDataSource sInstance;
    private final Context mContext;

    private final OnNetworkResult OnNetworkResult;
    private String mSorting;

    public interface OnNetworkResult {
        void movies(ArrayList<Result> movies);

        void error(Throwable t);
    }

    public MovieNetworkDataSource(Context mContext, OnNetworkResult networkResult) {
        this.mContext = mContext;
        OnNetworkResult = networkResult;
    }


    public static MovieNetworkDataSource getsInstance(Context context, OnNetworkResult networkResult) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieNetworkDataSource(context, networkResult);
            }
        }
        return sInstance;
    }

    public void fetchMovies(String sorting_string) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Movie> call = apiService.getPopularMovies(sorting_string, BuildConfig.API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                //successful api response
                OnNetworkResult.movies(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Log error here since request failed
                OnNetworkResult.error(t);
            }
        });
    }
}
