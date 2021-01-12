package com.example.bestfood;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KakaoPay {
    private void kakaoPayReady() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "cid=TC0ONETIME&partner_order_id=partner_order_id&partner_user_id=partner_user_id&item_name=초코파이&quantity=1&total_amount=2200&vat_amount=200&tax_free_amount=0&approval_url=https://developers.kakao.com&fail_url=https://developers.kakao.com&cancel_url=https://developers.kakao.com");
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/ready")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "KakaoAK ceb9ff5abc3653edac21733f89a1f68f")
                .build();
        Response response = client.newCall(request).execute();
    }

    private void kakaoPayApprove() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "cid=TC0ONETIME&tid=T2850892840739524675&partner_order_id=partner_order_id&partner_user_id=partner_user_id&pg_token=xxxxxxxxxxxxxxxxxxxx");
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/approve")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .addHeader("Authorization", "KakaoAK ceb9ff5abc3653edac21733f89a1f68f")
                .build();
        Response response = client.newCall(request).execute();
    }

    private void kakaoPayOrder() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "cid=TC0ONETIME&tid=T2850892840739524675");
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/order")
                .method("POST", body)
                .addHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .addHeader("Authorization", "KakaoAK ceb9ff5abc3653edac21733f89a1f68f")
                .build();
        Response response = client.newCall(request).execute();
    }

    private void kakaoPayCancel() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "cid=TC0ONETIME&tid=T1234567890123456789&cancel_amount=2200&cancel_tax_free_amount=0&cancel_vat_amount=200&cancel_available_amount=4000");
        Request request = new Request.Builder()
                .url("https://kapi.kakao.com/v1/payment/cancel")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .addHeader("Authorization", "KakaoAK ceb9ff5abc3653edac21733f89a1f68f")
                .build();
        Response response = client.newCall(request).execute();
    }
}
