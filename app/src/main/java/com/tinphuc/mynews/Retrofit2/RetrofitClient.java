package com.tinphuc.mynews.Retrofit2;

import android.icu.util.MeasureUnit;
import android.icu.util.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //Khởi tạo một singleton của RetrofitClient
    private static Retrofit retrofit = null;

    //Địa chỉ url
    public static final String BASE_ULS = "https://newsapi.org/v2/";
    public static Retrofit getClient(){
        Gson gson = new GsonBuilder().setLenient().create();
        if(retrofit == null){
            OkHttpClient buider = new OkHttpClient.Builder()
                    .readTimeout(5000, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .writeTimeout(5000, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .connectTimeout(10000, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_ULS)
                    .client(buider)
                    // Xác định bộ chuyển đổi JSON chúng ta cần đó là GSON
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
