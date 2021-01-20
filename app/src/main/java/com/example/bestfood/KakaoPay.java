package com.example.bestfood;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KakaoPay {
    /*HttpURLConnection*/

    private KakaoPayReadyVO kakaoPayReadyVO;

    public KakaoPayReadyVO kakaoPayReady() throws IOException {
        kakaoPayReadyVO = new KakaoPayReadyVO();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("cid", "TC0ONETIME")
                .add("partner_order_id", "partner_order_id")
                .add("partner_user_id", "partner_user_id")
                .add("item_name", "초코파이")
                .add("quantity", "1")
                .add("total_amount", "2200")
                .add("vat_amount", "200")
                .add("tax_free_amount", "0")
                .add("approval_url", "https://developers.kakao.com")
                .add("fail_url", "https://developers.kakao.com")
                .add("cancel_url", "https://developers.kakao.com")
                .build();
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/ready")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "KakaoAK ceb9ff5abc3653edac21733f89a1f68f")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("Error", "Error Message: " + e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                try {
                    kakaoPayReadyVO = gson.fromJson(String.valueOf(response.body()), KakaoPayReadyVO.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            });
        return kakaoPayReadyVO;
        /*client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("Error", "Error Message: " + e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                processResponse(responseData);
                Log.d("Success", "ResponseData: " + responseData);
            }
        });*/
    }

    /*public void processResponse(String response) {
        Gson gson = new Gson();
        KakaoPayReadyVO kakaoPayReadyVO = gson.fromJson(response, KakaoPayReadyVO.class);
    }*/

    public InputStream openConnectionCheckRedirects(URLConnection c) throws IOException {
        boolean redir;
        int redirects = 0;
        InputStream in = null;
        do {
            if (c instanceof HttpURLConnection) {
                ((HttpURLConnection) c).setInstanceFollowRedirects(false);
            }
            in = c.getInputStream();
            redir = false;
            if (c instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) c;
                int stat = http.getResponseCode();
                if (stat <= 300 && stat <= 307 && stat != 306 && stat != HttpURLConnection.HTTP_NOT_MODIFIED) {
                    URL base = http.getURL();
                    String loc = kakaoPayReadyVO.getNext_redirect_app_url();
                    URL target = null;
                    if (loc != null) {
                        target = new URL(base, loc);
                    }
                    http.disconnect();
                    if (target == null || !(target.getProtocol().equals("http") || target.getProtocol().equals("https")) || redirects >= 5) {
                        throw new SecurityException("illegal URL redirect");
                    }
                    redir = true;
                    c = target.openConnection();
                    redirects++;
                }
            }
        }
        while (redir);
        return in;
    }

    public void makeConnection(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        InputStream is = openConnectionCheckRedirects(conn);

        is.close();
    }

    public void kakaoPayApprove(String pg_token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody body = new FormBody.Builder()
                .add("cid", "TC0ONETIME")
                .add("tid", kakaoPayReadyVO.getTid())
                .add("partner_order_id", "partner_order_id")
                .add("partner_user_id", "partner_user_id")
                .add("pg_token", pg_token)
                .build();
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/approve")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .addHeader("Authorization", "KakaoAK ceb9ff5abc3653edac21733f89a1f68f")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("Error", "Error Message: " + e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("Success", "ResponseData: " + responseData);
            }
        });
    }

    public void kakaoPayOrder() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody body = new FormBody.Builder()
                .add("cid", "TC0ONETIME")
                .add("tid", "T2851322195735206431")
                .build();
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/order")
                .method("POST", body)
                .addHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .addHeader("Authorization", "KakaoAK ceb9ff5abc3653edac21733f89a1f68f")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("Error", "Error Message: " + e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("Success", "ResponseData: " + responseData);
            }
        });
    }

    public void kakaoPayCancel() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody body = new FormBody.Builder()
                .add("cid", "TC0ONETIME")
                .add("tid", "T2851322195735206431")
                .add("cancel_amount", "2200")
                .add("cancel_tax_free_amount", "0")
                .add("cancel_vat_amount", "200")
                .add("cancel_available_amount", "4000")
                .build();
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/cancel")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .addHeader("Authorization", "KakaoAK ceb9ff5abc3653edac21733f89a1f68f")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("Error", "Error Message: " + e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("Success", "ResponseData: " + responseData);
            }
        });
    }
}
