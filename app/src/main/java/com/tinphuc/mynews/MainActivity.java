package com.tinphuc.mynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
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
    private RecyclerView newsRecyView;
    private ArticleAdapter articleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRecyView = findViewById(R.id.newsRecyView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        newsRecyView.setLayoutManager(layoutManager);

        articleAdapter = new ArticleAdapter(articles, MainActivity.this);
        newsRecyView.setAdapter(articleAdapter);
        newsRecyView.setHasFixedSize(true);
        newsRecyView.setItemAnimator(new DefaultItemAnimator());
        newsRecyView.setNestedScrollingEnabled(false);
        LoadJson(null);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void LoadJson(String query){
//        Thực hiện request
        NewsService newsService = RetrofitClient.getClient().create(NewsService.class);
        String country = Utils.getCountry();
        String language = Utils.geLanguage();
        Call<News> resultgetNews;

        if(query != null){
            resultgetNews =
                    newsService.getNewsSearch(query, language, "publishedAt", API_KEY);
        }else{
            resultgetNews = newsService.getNews(country, API_KEY);
        }

//        Phương thức enqueue thực hiện request bất đồng bộ
//        và thông báo cho ứng dụng khi có phản hồi từ server.
        resultgetNews.enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        if(response.isSuccessful() && response.body().getListArticle() != null){
                            if (!articles.isEmpty()){
                                articles.clear();
                            }
                            articles.addAll(response.body().getListArticle());
                            articleAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(MainActivity.this,
                                    "Kết nối thất bại !", Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return MenuInflacter:
//        được dùng để khởi tạo menu XML file vào trong Menu object của context hiện tại
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem itemSearch = menu.findItem(R.id.itemSearch);
        //return SearchView được đặt trong menu item
        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setQueryHint("Tìm kiếm mới nhất...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 2)
                    LoadJson(query);
                else
                    Toast.makeText(MainActivity.this,
                            "Tìm kiếm với hai từ khóa trở lên !", Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadJson(null);
                return false;
            }
        });
        itemSearch.getIcon().setVisible(false,false);
        return true;

    }
}
