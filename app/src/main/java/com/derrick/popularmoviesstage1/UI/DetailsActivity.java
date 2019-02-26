package com.derrick.popularmoviesstage1.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.derrick.popularmoviesstage1.Data.network.Result;
import com.derrick.popularmoviesstage1.R;
import com.derrick.popularmoviesstage1.utils.Base_urls;
import com.derrick.popularmoviesstage1.utils.PaletteTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_DATA = "movie_data";

    private ImageView mPosterImg;
    private ImageView mBackDropImg;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private TextView mRatingTv;
    private TextView mRatingValueTv;
    private TextView mReleaseDateTv;
    private TextView mReleaseDateValueTv;
    private TextView mSynopsisValueTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Result result = getResultFromIntent();

        if (result == null) {
            // movie data unavailable
            closeOnError();
            return;
        }

        initializeViews();

        populateUI(result);

        setTitle(result.getOriginalTitle());


    }

    @Nullable
    private Result getResultFromIntent() {
        Result result = null;
        Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra(EXTRA_MOVIE_DATA)) {
                result = getIntent().getParcelableExtra(EXTRA_MOVIE_DATA);
                Timber.d("@Details result::" + result);
            }
        }
        return result;
    }

    private void initializeViews() {
        mPosterImg = findViewById(R.id.poster_img);
        mBackDropImg = findViewById(R.id.backdrop_img);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        mRatingTv = findViewById(R.id.rating_tv);
        mRatingValueTv = findViewById(R.id.rating_value_tv);
        mReleaseDateTv = findViewById(R.id.release_date_tv);
        mReleaseDateValueTv = findViewById(R.id.release_date_value_tv);
        mSynopsisValueTv = findViewById(R.id.synopsis_tv);
    }

    private void populateUI(Result result) {
        if (!TextUtils.isEmpty(result.getPosterPath())) {
            //loading poster
            Picasso.get().load(Base_urls.TMDB_IMG_BASE_URL + result.getPosterPath()).into(mPosterImg);
        }
        if (!TextUtils.isEmpty(result.getBackdropPath())) {
            //loading poster
            Picasso.get()
                    .load(Base_urls.TMDB_IMG_BASE_URL + result.getBackdropPath())
                    .fit().centerCrop()
                    .transform(PaletteTransformation.instance())
                    .into(mBackDropImg, new Callback.EmptyCallback() {
                        @Override
                        public void onSuccess() {
                            setToolbarColor();

                        }
                    });
        }

        if (result.getVoteAverage() != 0.0d) {
            mRatingTv.setVisibility(View.VISIBLE);
            mRatingValueTv.setVisibility(View.VISIBLE);
            mRatingValueTv.setText(Double.toString(result.getVoteAverage()));
        } else {
            mRatingTv.setVisibility(View.GONE);
            mRatingValueTv.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(result.getReleaseDate())) {
            mReleaseDateTv.setVisibility(View.GONE);
            mReleaseDateValueTv.setVisibility(View.GONE);
        } else {
            mReleaseDateTv.setVisibility(View.VISIBLE);
            mReleaseDateValueTv.setVisibility(View.VISIBLE);
            mReleaseDateValueTv.setText(result.getReleaseDate());
        }

        if (TextUtils.isEmpty(result.getOverview())) {
            mSynopsisValueTv.setVisibility(View.GONE);
        } else {
            mSynopsisValueTv.setVisibility(View.VISIBLE);
            mSynopsisValueTv.setText(result.getOverview());
        }
    }

    // Set the background and text colors of a toolbar given a uisng backdrop bitmap image to match
    private void setToolbarColor() {

        Bitmap bitmap = ((BitmapDrawable) mBackDropImg.getDrawable()).getBitmap();

        /*
         * Pelette from {@link PaletteTransformation class}
         */
        Palette palette = PaletteTransformation.getPalette(bitmap);

        //getting  the vibrant swatch
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        // Load default colors
        int backgroundColor = ContextCompat.getColor(getApplicationContext(),
                R.color.colorPrimary);
        int textColor = ContextCompat.getColor(getApplicationContext(),
                R.color.default_title_color);

        int statusColor = ContextCompat.getColor(getApplicationContext(), R.color.black_trans80);


        // Check that the Vibrant swatch is available
        if (vibrantSwatch != null) {
            backgroundColor = vibrantSwatch.getRgb();
            textColor = vibrantSwatch.getTitleTextColor();
        }

        // Set the title text colors
        mToolbar.setTitleTextColor(textColor);

        //setting status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);
        }
        /*
         * setting toolbar ScrimColor
         */
        mCollapsingToolbarLayout.setContentScrimColor(backgroundColor);
        mCollapsingToolbarLayout.setStatusBarScrimColor(statusColor);

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

}
