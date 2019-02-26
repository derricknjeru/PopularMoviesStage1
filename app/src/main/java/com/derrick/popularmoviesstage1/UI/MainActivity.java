package com.derrick.popularmoviesstage1.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.derrick.popularmoviesstage1.BuildConfig;
import com.derrick.popularmoviesstage1.Data.local.MoviePreferences;
import com.derrick.popularmoviesstage1.R;
import com.derrick.popularmoviesstage1.Data.network.MovieNetworkDataSource;
import com.derrick.popularmoviesstage1.Data.network.Result;

import java.util.ArrayList;

import timber.log.Timber;

import static com.derrick.popularmoviesstage1.UI.DetailsActivity.EXTRA_MOVIE_DATA;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private TextView mTvError;
    private ContentLoadingProgressBar mLoadingProgressBar;
    private RecyclerView mRecycleView;

    private final String SAVED_MOVIES_LIST = "saved_movies";
    ArrayList<Result> movieList;
    private MovieAdapter adapter;
    private String query;
    private int numberOfColumns = 2;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_MOVIES_LIST, movieList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews();


        if (savedInstanceState == null || !savedInstanceState.containsKey(SAVED_MOVIES_LIST)) {
            query = MoviePreferences.getSortingQuery(this, getString(R.string.pref_sorting_key));
            fetchMovies();

        } else {
            mLoadingProgressBar.hide();
            movieList = savedInstanceState.getParcelableArrayList(SAVED_MOVIES_LIST);
            Timber.d("@Movies movieList onSavedInstance::" + movieList.size());
            adapter.setResults(movieList);

        }

        adapter.setOnListClickLister(new MovieAdapter.onListClickLister() {
            @Override
            public void onClick(int pos) {
                Result movieData = movieList.get(pos);
                if (movieData != null) {
                    Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                    i.putExtra(EXTRA_MOVIE_DATA, movieData);
                    startActivity(i);
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }

    /**
     * This fetches movies from TMDB using {@link MovieNetworkDataSource class}
     */
    private void fetchMovies() {
        if (movieList != null && movieList.size() > 0) {
            movieList = null;
        }
          mLoadingProgressBar.show();
        //Checking if the key is set
        if (BuildConfig.API_KEY.isEmpty() || BuildConfig.API_KEY.contentEquals(getString(R.string.add_key_message))) {
            mTvError.setText(getString(R.string.add_api_key_error_message));
            return;
        }
        //Fetching the movies
        MovieNetworkDataSource.getsInstance(new MovieNetworkDataSource.OnNetworkResult() {
            @Override
            public void movies(ArrayList<Result> movies) {

                mLoadingProgressBar.hide();

                if (movies != null) {
                    movieList = movies;
                    Timber.d("@Movies fetchMovies movieList::" + movieList);
                    adapter.setResults(movieList);
                }
            }

            @Override
            public void error(Throwable t) {

                mLoadingProgressBar.hide();

                mTvError.setText(t.toString());
            }
        }).fetchMovies(query);
    }

    private void initializeViews() {
        mTvError = findViewById(R.id.error_tv);
        mLoadingProgressBar = findViewById(R.id.content_pbar);
        mRecycleView = findViewById(R.id.recycler_view);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mRecycleView.setHasFixedSize(true);
        adapter = new MovieAdapter();
        mRecycleView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        InitialToggle(menu);
        return true;
    }

    /**
     * Used when menu is created or recreated
     *
     * @param menu
     */
    private void InitialToggle(Menu menu) {
        if (query.contentEquals(getString(R.string.pref_sorting_top_rated))) {
            menu.getItem(1).setChecked(true);
        } else {
            menu.getItem(0).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            ToggleMenu(item);
            MoviePreferences.setSortingQuery(this, getString(R.string.pref_sorting_default_value));
            return true;
        }

        if (id == R.id.action_top_rated) {
            ToggleMenu(item);
            MoviePreferences.setSortingQuery(this, getString(R.string.pref_sorting_top_rated));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Used on click of a menu
     *
     * @param item
     */
    private void ToggleMenu(MenuItem item) {
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        query = MoviePreferences.getSortingQuery(this, key);
        adapter.setResults(null);
        fetchMovies();
    }
}
