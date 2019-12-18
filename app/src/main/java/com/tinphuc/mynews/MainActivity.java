package com.tinphuc.mynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String API_KEY = "c91ee0f2487d4f9eb6140b6711a6af19";
    private TextView topHeading;
    private RecyclerView newsRecyView;
    private ArticleAdapter articleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private Article article;

    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView textErrorTitle, textErrorMessage;
    private Button buttonTryAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topHeading = findViewById(R.id.txtTopHeading);
        newsRecyView = findViewById(R.id.newsRecyView);
        swipeRefreshLayout = findViewById(R.id.swipe_fresh_layout);

        layoutManager = new LinearLayoutManager(MainActivity.this);
        newsRecyView.setLayoutManager(layoutManager);

        articleAdapter = new ArticleAdapter(articles, MainActivity.this);
        newsRecyView.setAdapter(articleAdapter);
        newsRecyView.setHasFixedSize(true);
        newsRecyView.setItemAnimator(new DefaultItemAnimator());
        newsRecyView.setNestedScrollingEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        onLoadingLayoutRefresh(null);

        errorLayout = findViewById(R.id.error_layout);
        errorImage = findViewById(R.id.error_image);
        textErrorTitle = findViewById(R.id.text_error_title);
        textErrorMessage = findViewById(R.id.text_error_message);
        buttonTryAgain = findViewById(R.id.button_try_again);

    }

    public void LoadJson(final String query) {

        topHeading.setText("Top Headlines");
//        set cho view errorLayout luôn GONE;
        errorLayout.setVisibility(View.GONE);

//        Thực hiện request
        NewsService newsService = RetrofitClient.getClient().create(NewsService.class);
        String country = Utils.getCountry();
        String language = Utils.geLanguage();
        Call<News> resultgetNews;

        if (query != null) {
            resultgetNews =
                    newsService.getNewsSearch(query, language, "publishedAt", API_KEY);
        } else {
            resultgetNews = newsService.getNews("us", API_KEY);
        }

        swipeRefreshLayout.setRefreshing(true);
//        Phương thức enqueue thực hiện request bất đồng bộ
//        và thông báo cho ứng dụng khi có phản hồi từ server.
        resultgetNews.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getListArticle() != null) {
                    if (!articles.isEmpty()) {
                        articles.clear();
                    }
                    articles.addAll(response.body().getListArticle());
                    articleAdapter.notifyDataSetChanged();

//                    khởi tạo lệnh khi thực hiện click adapter
                    initListener();

//                    dừng refreshing và đồng nghĩa là tắt vòng tròn chạy
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    topHeading.setVisibility(View.INVISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()){
                        case 404:
                            errorCode = "404 not found!";
                            break;
                        case 500:
                            errorCode = "500 error server";
                            break;
                        default:
                            errorCode = "Unknown error!";
                            break;
                    }
                    showErrorMessage(R.drawable.no_result, "Không có dữ liệu",
                            "Vui lòng kết nối và thử lại" + errorCode);
                }
            }

            //Phương thức này dùng để chỉ ngoại lệ xảy không trong phạm vị loading dữ liệu về
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                topHeading.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.oops, "Rất tiếc! đã xảy ra lỗi",
                        t.toString());
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
                if (query.length() > 2)
                    onLoadingLayoutRefresh(query);
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
        itemSearch.getIcon().setVisible(false, false);
        return true;

    }

//  phương thức onRefresh sẽ thực hiện khi lắng nghe sự kiện swipeRefreshLayout.setOnRefreshListener
//  còn được gọi là khi ta swipe
    @Override
    public void onRefresh() {
        LoadJson(null);
    }

//    mục đích của phương thức để tạo độ trễ cho luồng UI nên ta mới thấy vòng tròn chạy bên trong
    private void onLoadingLayoutRefresh(final String query){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                LoadJson(query);
            }
        });
    }

    private void initListener() {
        articleAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

                ImageView imageTitle = findViewById(R.id.imgTitle);
                article = articles.get(position);
                Intent intent = new Intent(MainActivity.this, NewsDetailActivity.class);
                intent.putExtra("image", article.getUrlToImage());
                intent.putExtra("author", article.getAuthor());
                intent.putExtra("date", article.getPublishedAt());
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("source", article.getSource().getName());

//                Shared Element Transition
                Pair<View, String> pair = Pair.create((View)imageTitle, "img");
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this, pair
                );
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    startActivity(intent, optionsCompat.toBundle());
                }else
                    startActivity(intent);
            }
        });
    }

    private void showErrorMessage(int imageView, String errorTitle, String errorMessage){

        if(errorLayout.getVisibility() == View.GONE){
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(imageView);
        textErrorTitle.setText(errorTitle);
        textErrorMessage.setText(errorMessage);
        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingLayoutRefresh(null);
            }
        });
    }
}