package com.tinphuc.mynews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView imgBackDrop;
    private LinearLayout appbarTitle;
    private TextView txtTitleAppbar, txtSubTitleAppbar, txtTitleNews, txtTimeAgo,txtPublishedAt;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}
