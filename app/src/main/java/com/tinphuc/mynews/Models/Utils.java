package com.tinphuc.mynews.Models;

import android.util.Log;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String DateToTimeFomat(String oldDateString){
//        Set Locale mong muốn vào đối tương Locale của ta
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String isTime = null;
        try {
//            Chuyển định dạng của JSON về chuẩn định dạng SimpleDateFomat của ENGLISH
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.ENGLISH);
            Date date = sdf.parse(oldDateString);
//            Chuyển định dạng date theo đúng định dạng đối tượng PrettyTime ở trên và cuối cùng là thành định dạng String
            isTime = prettyTime.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isTime;
    }

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

    public static String getCountry(){
//        Lấy định dạng locale theo đúng máy ảo JVM
        Locale locale = Locale.getDefault();
        String country = String.valueOf(locale.getCountry());
        return country.toLowerCase();
    }
}
