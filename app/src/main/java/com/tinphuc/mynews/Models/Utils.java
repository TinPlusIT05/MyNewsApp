package com.tinphuc.mynews.Models;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utils {

    public static String DateToTimeFomat(String oldDateString){
//        Set Locale mong muốn vào đối tương Locale của ta
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String isTime = null;
        try {
//            Chuyển định dạng của JSON về chuẩn định dạng SimpleDateFomat của ENGLISH
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
//            Chuyển oldDateString thành định dạng date
            Date date = sdf.parse(oldDateString);
//            Chuyển định dạng date theo đúng định dạng đối tượng PrettyTime ở trên và cuối cùng là thành định dạng String
            isTime = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTime;
    }

//    Chuyển định dạng Date cho pulishedAt
    public static String DateFomat(String oldDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MM yyyy",
                Locale.ENGLISH);
        String isDate = null;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH).parse(oldDateString);
            isDate = sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isDate;
    }

//    Lấy tên quốc gia
    public static String getCountry(){
//        Lấy định dạng locale theo đúng máy ảo JVM
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }

    public static String geLanguage(){
        Locale locale = Locale.getDefault();
        String launguage = String.valueOf(locale.getLanguage());
        return launguage.toLowerCase();
    }

    public static ColorDrawable vibrantLightColorList[] = {
            new ColorDrawable(Color.parseColor("#ffeead")),
            new ColorDrawable(Color.parseColor("#93cfb3")),
            new ColorDrawable(Color.parseColor("#fd7a7a")),
            new ColorDrawable(Color.parseColor("#faca5f")),
            new ColorDrawable(Color.parseColor("#1ba798")),
            new ColorDrawable(Color.parseColor("#6aa9ae")),
            new ColorDrawable(Color.parseColor("#ffbf27")),
            new ColorDrawable(Color.parseColor("#d93947"))
    };

//    Random ngẫu nhiên ColorDrawable sử dụng lúc RequestOptions Glide
    public static ColorDrawable randomColorDrawable(){
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }
}
