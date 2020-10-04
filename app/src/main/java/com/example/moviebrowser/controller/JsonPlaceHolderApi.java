package com.example.moviebrowser.controller;

import com.example.moviebrowser.model.MoviesModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<MoviesModel> getMoviesData(Map<String, String> options);
}
