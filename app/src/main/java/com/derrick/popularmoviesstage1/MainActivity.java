package com.derrick.popularmoviesstage1;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.derrick.popularmoviesstage1.network.MovieNetworkDataSource;
import com.derrick.popularmoviesstage1.network.Result;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private TextView tvError;
    private ContentLoadingProgressBar loadingProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeViews();

        fetchMovies();

    }

    /**
     * This fetches movies from TMDB and set it to {@link }
     */
    private void fetchMovies() {

        if (BuildConfig.API_KEY.isEmpty() || BuildConfig.API_KEY.contentEquals(getString(R.string.add_key_message))) {
            tvError.setText(getString(R.string.add_api_key_error_message));
            return;
        }

        loadingProgressBar.show();

        MovieNetworkDataSource.getsInstance(this, new MovieNetworkDataSource.OnNetworkResult() {
            @Override
            public void movies(List<Result> movies) {

                loadingProgressBar.hide();

                if (movies != null) {
                    Log.d(LOG_TAG, "@Movies movieList::" + movies.size());
                }
            }

            @Override
            public void error(Throwable t) {

                loadingProgressBar.hide();

                tvError.setText(t.toString());
            }
        }).fetchMovies();
    }

    private void initializeViews() {
        tvError = findViewById(R.id.error_tv);
        loadingProgressBar = findViewById(R.id.content_pbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
