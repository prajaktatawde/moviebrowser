package com.example.moviebrowser.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

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
    ImageView img_sort;
    List<MoviesModel.Result> moviesResult;
    MoviesAdapter moviesAdapter;
    ProgressBar progressbar;
    boolean isAPICalled;
    int pageNo = 1;
    TextView txt_most_popular,txt_highest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();

        getMoviesListing(true);
    }

    private void getMoviesListing(boolean isFirst) {
        if (isFirst) {
            moviesResult = new ArrayList<>();
        }
        HashMap<String,String> map = new HashMap<>();
        map.put("api_key", Constants.API_KEY);
        map.put("page",pageNo+"");
        JsonPlaceHolderApi apiService = ApiClientMain.getClient().create(JsonPlaceHolderApi.class);
        Call<MoviesModel> call = apiService.getMoviesData(map);

        call.enqueue(new Callback<MoviesModel>() {
            @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                final MoviesModel rsp = response.body();
                if (rsp.getResults() != null) {
                    if (rsp.getResults().size() > 0) {
                        isAPICalled = true;
                        if (moviesResult.size() == 0) {
                            moviesResult = rsp.getResults();
                            Log.d("TAG","Response ="+moviesResult);
                            moviesAdapter = new MoviesAdapter(context,moviesResult);
                            rv_movies.setAdapter(moviesAdapter);
                        } else {
                            moviesResult.addAll(rsp.getResults());
                            moviesAdapter.notifyDataSetChanged();
                        }
                    }
                }

                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<MoviesModel> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {

        img_sort = findViewById(R.id.img_sort);
        img_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailog_popup(context);
            }
        });
        rv_movies = findViewById(R.id.rv_movies);
        final GridLayoutManager lm = new GridLayoutManager(context, 3);
        rv_movies.setLayoutManager(lm);

        progressbar = findViewById(R.id.progressbar);

        rv_movies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    int visibleItemCount = lm.getChildCount();
                    int totalItemCount = lm.getItemCount();
                    int firstVisibleItemPosition = lm.findFirstVisibleItemPosition();
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && isAPICalled) {
                        isAPICalled = false;
                        pageNo += 1;
                        progressbar.setVisibility(View.VISIBLE);
                        getMoviesListing(false);
                    }
                }
            }
        });

    }

    private void dailog_popup(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sort_layout, null);
        builder1.setView(view);
        builder1.setCancelable(true);
        AlertDialog alert11 = builder1.create();
        alert11.show();
        
        txt_most_popular = view.findViewById(R.id.txt_most_popular);
        txt_highest = view.findViewById(R.id.txt_highest);
    }
}