package com.tinphuc.mynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.tinphuc.mynews.Adapter.ArticleAdapter;
import com.tinphuc.mynews.Models.Article;
import com.tinphuc.mynews.Models.News;
import com.tinphuc.mynews.Models.Utils;
import com.tinphuc.mynews.Retrofit2.NewsService;
import com.tinphuc.mynews.Retrofit2.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "c91ee0f2487d4f9eb6140b6711a6af19";
    private RecyclerView newsList;
    private ArticleAdapter articleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsList = findViewById(R.id.newsList);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        newsList.setLayoutManager(layoutManager);
        newsList.setHasFixedSize(true);
        newsList.setItemAnimator(new DefaultItemAnimator());
        newsList.setNestedScrollingEnabled(false);
        LoadJson();
    }

    public void LoadJson(){
        NewsService newsService = RetrofitClient.getClient().create(NewsService.class);
        String country = Utils.getCountry();
        Call<News> call;
        call = newsService.getNews(country, API_KEY);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful() && response.body().getListArticle() != null){
                    if (!articles.isEmpty()){
                        articles.clear();
                    }
                    articles = response.body().getListArticle();
                    articleAdapter = new ArticleAdapter(articles, MainActivity.this);
                    newsList.setAdapter(articleAdapter);
                    articleAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(MainActivity.this, "No Result", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });

    }
}
