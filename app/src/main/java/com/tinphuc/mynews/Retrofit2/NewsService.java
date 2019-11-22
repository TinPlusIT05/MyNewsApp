package com.tinphuc.mynews.Retrofit2;

import com.tinphuc.mynews.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("top-headlines")
    Call<News> getNews(
        @Query("country") String country,
        @Query("apiKey") String apiKey
    );
}
