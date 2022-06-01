package com.example.seetheworld.util;

import android.os.Build;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static final String BASE_URL = "http://106.15.3.13:8081";
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * @description: 同步get请求
     * @params: [url]
     * @return: java.lang.String
     * @author: renjian
     * @dateTime: 2022/5/31 18:52
     */

    public static String get(String url) {
        String responseData = "";
        Request request = new Request.Builder()
                .url(BASE_URL+url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            responseData = response.body().string();
            System.out.println("responseData=" + responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    /**
     * @description: 同步post请求获取推荐新闻list
     * @params: [url, page, pageSize]
     * @return: java.lang.String
     * @author: renjian
     * @dateTime: 2022/5/31 18:51
     */

    public static String post(String url, int page, int pageSize) {
        String json = "{\"page\":" + page + ",\"pageSize\":" + pageSize + "}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .post(body)
                .build();

        String responseData = "";
        try {
            Response response = client.newCall(request).execute();
            responseData = response.body().string();
            System.out.println("responseData=" + responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}
