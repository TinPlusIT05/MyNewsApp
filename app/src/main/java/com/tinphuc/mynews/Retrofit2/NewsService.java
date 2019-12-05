package com.tinphuc.mynews.Retrofit2;

import com.tinphuc.mynews.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
//    endpoint của API
    @GET("top-headlines")
    Call<News> getNews(
//          @Query() dùng để lọc dữ liệu từ máy chủ
        @Query("country") String country,
        @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") String query,
            @Query("language") String language,
            @Query("sortBy") String sortby,
            @Query("apiKey") String apikey
    );
}
