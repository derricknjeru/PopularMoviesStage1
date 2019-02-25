package com.derrick.popularmoviesstage1.UI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.derrick.popularmoviesstage1.utils.Base_urls;
import com.derrick.popularmoviesstage1.Data.network.Result;
import com.derrick.popularmoviesstage1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import timber.log.Timber;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    ArrayList<Result> results;

    public MovieAdapter() {
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int pos) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int pos) {
        Result movie = results.get(pos);

        String poster = Base_urls.TMDB_IMG_BASE_URL + movie.getPosterPath();

        Timber.d("@logs::" + poster);

        Picasso.get().load(poster).into(holder.mPosterImg);

    }

    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mPosterImg;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mPosterImg = itemView.findViewById(R.id.item_img);

        }

    }
}
