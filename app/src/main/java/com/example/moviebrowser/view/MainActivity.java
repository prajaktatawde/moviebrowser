package com.example.moviebrowser.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.moviebrowser.R;
import com.example.moviebrowser.adapter.MoviesAdapter;
import com.example.moviebrowser.constants.Constants;
import com.example.moviebrowser.controller.ApiClientMain;
import com.example.moviebrowser.controller.JsonPlaceHolderApi;
import com.example.moviebrowser.model.MoviesModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Context context;
    RecyclerView rv_movies;
    List<MoviesModel.Result> moviesResult;
    MoviesAdapter moviesAdapter;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();

        getMoviesListing();
    }

    private void getMoviesListing() {

        HashMap<String,String> map = new HashMap<>();
        map.put("api_key", Constants.API_KEY);
        map.put("page","1");
        JsonPlaceHolderApi apiService = ApiClientMain.getClient().create(JsonPlaceHolderApi.class);
        Call<MoviesModel> call = apiService.getMoviesData(map);

        call.enqueue(new Callback<MoviesModel>() {
            @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                final MoviesModel rsp = response.body();
                moviesResult = rsp.getResults();

                Log.d("TAG","Response ="+moviesResult);
              //  moviesAdapter  = new MoviesAdapter(context,moviesResult);
                rv_movies.setAdapter(moviesAdapter);
            }

            @Override
            public void onFailure(Call<MoviesModel> call, Throwable t) {
                //Log.d("TAG","Response = "+t.toString());
            }
        });
    }

    private void initView() {

        rv_movies = findViewById(R.id.rv_movies);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        rv_movies.setLayoutManager(gridLayoutManager);

        progressbar = findViewById(R.id.progressbar);

       /* rv_movies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isNeedToNextCall) {
                    if (!recyclerView.canScrollVertically(1)) {
                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();
                        int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && isAPICalled) {
                            isAPICalled = false;
                            pageNo += 1;
                            SpaList(preferences.getPreferenceValues(Constants.LATITUDE), preferences.getPreferenceValues(Constants.LONGITUDE), tab_id, pageNo);
                        }
                    }
                }
            }
        });*/

    }
}