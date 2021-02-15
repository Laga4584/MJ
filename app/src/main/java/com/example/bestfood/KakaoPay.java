package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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
    public void kakaoPayReady(final KakaoPayReadyVO kakaoPayReadyVO, String item_name, String total_amount) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("cid", "TC0ONETIME")
                .add("partner_order_id", "partner_order_id")
                .add("partner_user_id", "partner_user_id")
                .add("item_name", item_name)
                .add("quantity", "1")
                .add("total_amount", total_amount)
                .add("tax_free_amount", "0")
                .add("approval_url", "http://ec2-54-180-82-94.ap-northeast-2.compute.amazonaws.com:3000")
                .add("fail_url", "http://ec2-54-180-82-94.ap-northeast-2.compute.amazonaws.com:3000")
                .add("cancel_url", "http://ec2-54-180-82-94.ap-northeast-2.compute.amazonaws.com:3000")
                .build();
        final Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/ready")
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
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        Log.d("Success check", responseData);
                        kakaoPayReadyVO.setTid(jsonObject.getString("tid"));
                        kakaoPayReadyVO.setNext_redirect_app_url(jsonObject.getString("next_redirect_app_url"));
                        kakaoPayReadyVO.setAndroid_app_scheme(jsonObject.getString("android_app_scheme"));
                        kakaoPayReadyVO.setCreated_at(jsonObject.getString("created_at"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public String getToken(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        return urlConnection.getURL().getHost();
    }

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
                    String loc = http.getHeaderField("Location");
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

    public void kakaoPayApprove(final KakaoPayApproveVO kakaoPayApproveVO, String tid, String pg_token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody body = new FormBody.Builder()
                .add("cid", "TC0ONETIME")
                .add("tid", tid)
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
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        Log.d("Success check", responseData);
                        kakaoPayApproveVO.setTid(jsonObject.getString("tid"));
                        kakaoPayApproveVO.setPayment_method_type(jsonObject.getString("payment_method_type"));
                        kakaoPayApproveVO.setItem_name(jsonObject.getString("item_name"));
                        kakaoPayApproveVO.setApproved_at(jsonObject.getString("approved_at"));
                        JSONObject amountObject = jsonObject.getJSONObject("amount");
                        kakaoPayApproveVO.setTotal(amountObject.getInt("total"));
                        kakaoPayApproveVO.setVat(amountObject.getInt("vat"));
                        JSONObject cardObject = jsonObject.getJSONObject("card_info");
                        kakaoPayApproveVO.setPurchase_corp(cardObject.getString("purchase_corp"));
                        kakaoPayApproveVO.setCard_type(cardObject.getString("card_type"));
                        kakaoPayApproveVO.setInstall_month(cardObject.getString("install_month"));
                        kakaoPayApproveVO.setInterest_free_install(cardObject.getString("interest_free_install"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
