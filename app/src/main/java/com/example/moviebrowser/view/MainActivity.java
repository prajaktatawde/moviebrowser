package com.example.moviebrowser.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class MainActivity extends AppCompatActivity {
    Context context;
    RecyclerView rv_movies;
    ImageView img_sort;
    List<MoviesModel.Result> moviesResult;
    MoviesAdapter moviesAdapter;
    ProgressBar progressbar;
    boolean isAPICalled;
    int pageNo = 1;
    TextView txt_most_popular, txt_highest;
    SearchView searchView;
    String type = "listing";
    private String searchKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();

        getMoviesListing(true, type);
    }

    private void getMoviesListing(boolean isFirst, String type) {
        if (isFirst) {
            moviesResult = new ArrayList<>();
        }
        HashMap<String, String> map = new HashMap<>();

        if (type.equals("search")) {
            map.put("api_key", Constants.API_KEY);
            map.put("page", pageNo + "");
            map.put("query", searchKey);
        } else {
            map.put("api_key", Constants.API_KEY);
            map.put("page", pageNo + "");
        }

        JsonPlaceHolderApi apiService = ApiClientMain.getClient().create(JsonPlaceHolderApi.class);
        Call<MoviesModel> call = null;
        if (type.equals("listing")) {
            call = apiService.getMoviesData(map);
        } else if (type.equals("most_popular")) {
            call = apiService.getMoviesData_popular(map);
        } else if (type.equals("most_highest")) {
            call = apiService.getMoviesData_toprated(map);
        } else if (type.equals("search")) {
            call = apiService.getMoviesData_search(map);
        }

        call.enqueue(new Callback<MoviesModel>() {
            @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                final MoviesModel rsp = response.body();
                if (rsp.getResults() != null) {
                    if (rsp.getResults().size() > 0) {
                        isAPICalled = true;
                        if (moviesResult.size() == 0) {
                            moviesResult = rsp.getResults();
                            Log.d("TAG", "Response =" + moviesResult);
                            moviesAdapter = new MoviesAdapter(context, moviesResult);
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
                Log.d("TAG", "Response = " + t.toString());
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {

        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Find By Movie Name");
        searchView.setIconifiedByDefault(false);

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
                        getMoviesListing(false, type);
                    }
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String query) {

                if (query.length() > 0) {
                    searchKey = query;
                    type = "search";
                    pageNo = 1;
                    getMoviesListing(true, type);
                } else {
                    searchKey = "";
                    searchView.setFocusable(false);
                    searchView.setIconified(false);
                    searchView.clearFocus();
                    type = "listing";
                    pageNo = 1;
                    getMoviesListing(true, type);
                }

                return false;
            }
        });
    }

    private void dailog_popup(Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sort_layout, null);
        builder1.setView(view);
        builder1.setCancelable(true);
        final AlertDialog alert11 = builder1.create();
        alert11.show();

        txt_most_popular = view.findViewById(R.id.txt_most_popular);
        txt_highest = view.findViewById(R.id.txt_highest);

        txt_most_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
                type = "most_popular";
                pageNo = 1;
                getMoviesListing(true, type);
            }
        });

        txt_highest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
                type = "most_highest";
                pageNo = 1;
                getMoviesListing(true, type);
            }
        });
    }
}