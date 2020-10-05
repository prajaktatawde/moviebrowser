package com.example.moviebrowser.controller;

import com.example.moviebrowser.model.MoviesModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface JsonPlaceHolderApi {

    @GET("movie/now_playing")
    Call<MoviesModel> getMoviesData(@QueryMap Map<String, String> options);
}
