package com.example.moviebrowser.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviebrowser.R;
import com.example.moviebrowser.constants.Constants;
import com.example.moviebrowser.model.MoviesModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MoviesDetailActivity extends AppCompatActivity {

    MoviesModel.Result movieModel;
    Context context;
    Bundle bundle;
    ImageView expandedImage;
    TextView tvTitle, tvDescription, rating, release_date;
    Animation myAnim;
    Toolbar mToolbar;
    CollapsingToolbarLayout collapsingToolbarLayout = null;
    MyBounceInterpolator interpolator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);
        context = this;
        bundle = getIntent().getExtras();
        if (bundle != null) {
            movieModel = (MoviesModel.Result) bundle.getSerializable("data");
        }
        myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
        interpolator = new MyBounceInterpolator(0.2, 20);
        initView();
        setData();
    }

    private void setData() {

        tvTitle.setText(movieModel.getOriginalTitle());
        tvDescription.setText(movieModel.getOverview());
        collapsingToolbarLayout.setTitle(movieModel.getOriginalTitle());
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd, MMMM yyyy");
        String dateToStr = format.format(today);
        release_date.setText("Release Date : " + dateToStr);
        String stringdouble = Double.toString(movieModel.getVoteAverage());
        rating.setText("Vote : " + stringdouble);
        String poster_path = Constants.POSTER_PATH + movieModel.getPosterPath();
        Glide.with(context)
                .load(poster_path)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(expandedImage);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent1));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        tvDescription = findViewById(R.id.tvDescription);
        release_date = findViewById(R.id.release_date);
        rating = findViewById(R.id.rating);

        tvTitle = findViewById(R.id.tvTitle);
        expandedImage = findViewById(R.id.expandedImage);

    }

    private class MyBounceInterpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        public MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
}