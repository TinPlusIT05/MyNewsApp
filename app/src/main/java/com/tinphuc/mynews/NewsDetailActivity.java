package com.tinphuc.mynews;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tinphuc.mynews.Models.Utils;

public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private FrameLayout dateBehavior;
    private ImageView imageBackDrop;
    private LinearLayout titleAppBar;
    private TextView textTitleAppbar, textSubTitleAppbar, textTitleNews, textTimeAgo,
            textPublishedAt;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private WebView webView;
    private String mImage, mAuthor, mDate, mUrl, mTitle, mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar = findViewById(R.id.toolbar);
//        toolbar như là actionbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBar = findViewById(R.id.appbar);
        appBar.addOnOffsetChangedListener(this);
        imageBackDrop = findViewById(R.id.image_backdrop);
        titleAppBar = findViewById(R.id.title_appbar);
        textTitleAppbar = findViewById(R.id.text_title_appbar);
        textSubTitleAppbar = findViewById(R.id.text_subtitle_appbar);
        textTitleNews = findViewById(R.id.text_title_news);
        textTimeAgo = findViewById(R.id.text_time_ago);
        textPublishedAt =findViewById(R.id.text_published_at);
        dateBehavior = findViewById(R.id.date_behavior);

//        lấy dữ liệu từ intent gởi qua
        Intent intent = getIntent();
        mImage = intent.getStringExtra("image");
        mAuthor = intent.getStringExtra("author");
        mDate = intent.getStringExtra("date");
        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
        mSource = intent.getStringExtra("source");

        textTitleAppbar.setText(mSource);
        textSubTitleAppbar.setText(mUrl);
        textPublishedAt.setText(Utils.DateFomat(mDate));
        textTitleNews.setText(mTitle);

        String author;
        if(mAuthor != null){
            author = " \u2022 " + mAuthor;
        }else
            author = "";
        textTimeAgo.setText(mSource + author + " \u2022 " + Utils.DateToTimeFomat(mDate));

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(Utils.randomColorDrawable())
                .error(Utils.randomColorDrawable());
        Glide.with(this)
                .load(mImage)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageBackDrop);

        initWebView(mUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxcroll = appBarLayout.getTotalScrollRange();
        float percentage = (float)Math.abs(verticalOffset)/ (float) maxcroll;
        if(percentage == 1f){
            dateBehavior.setVisibility(View.GONE);
            titleAppBar.setVisibility(View.VISIBLE);
        }else if(percentage < 1f) {
            dateBehavior.setVisibility(View.VISIBLE);
            titleAppBar.setVisibility(View.GONE);
        }
    }

    private void initWebView(String url){
        webView = findViewById(R.id.web_view);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}
