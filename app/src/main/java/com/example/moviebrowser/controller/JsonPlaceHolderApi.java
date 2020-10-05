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

    @GET("movie/popular")
    Call<MoviesModel> getMoviesData_popular(@QueryMap Map<String, String> options);

    @GET("movie/top_rated")
    Call<MoviesModel> getMoviesData_toprated(@QueryMap Map<String, String> options);

    @GET("search/movie")
    Call<MoviesModel> getMoviesData_search(@QueryMap Map<String, String> options);
}
