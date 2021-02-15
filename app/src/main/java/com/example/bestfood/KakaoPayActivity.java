package com.example.bestfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.security.auth.callback.CallbackHandler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.QueryName;

public class KakaoPayActivity extends AppCompatActivity {
    public static Context context_kakaopay;

    String pg_token;

    WebView payView;
    WebSettings payViewSettings;

    KakaoPay kakaoPay;
    KakaoPayReadyVO kakaoPayReadyVO;
    public KakaoPayApproveVO kakaoPayApproveVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_pay);

        kakaoPay = new KakaoPay();
        kakaoPayReadyVO = new KakaoPayReadyVO();
        kakaoPayApproveVO = new KakaoPayApproveVO();

        try {
            kakaoPay.kakaoPayReady(kakaoPayReadyVO);
            boolean flag = true;
            while (flag) {
                if (kakaoPayReadyVO.getTid() != null) flag = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Success result", kakaoPayReadyVO.toString());

        //https://devtalk.kakao.com/t/topic/105366/2
        payView = (WebView) findViewById(R.id.kakao_pay_webview);
        payView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("intent://")) {
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        //Uri uri = Uri.parse(intent.getDataString());
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null) {
                            startActivity(intent);
                            //startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                } else if (url != null && url.startsWith("market://")) {
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            startActivity(intent);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                Uri tokenUri = Uri.parse(url);
                pg_token = tokenUri.getQueryParameter("pg_token");
                try {
                    kakaoPay.kakaoPayApprove(kakaoPayApproveVO, kakaoPayReadyVO.getTid(), pg_token);
                    boolean flag = true;
                    while (flag) {
                        if (kakaoPayApproveVO.getApproved_at() != null) flag = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Success KakaoPayapproval: ", kakaoPayApproveVO.toString());
                if (url.contains("pg_token=") || view.getUrl().contains("pg_token=")) {
                    view.stopLoading();
                }
                if (!view.canGoForward()) {
                    finish();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        payViewSettings = payView.getSettings();
        payViewSettings.setJavaScriptEnabled(true);

        payView.loadUrl(kakaoPayReadyVO.getNext_redirect_app_url());

        context_kakaopay = this;
    }
}