package com.example.bestfood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class KakaoPayActivity extends AppCompatActivity {
    String pg_token_url;
    String pg_token = null;

    WebView payView;
    WebSettings payViewSettings;

    KakaoPay kakaoPay;
    KakaoPayReadyVO kakaoPayReadyVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_pay);

        kakaoPay = new KakaoPay();
        kakaoPayReadyVO = new KakaoPayReadyVO();
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
        payViewSettings = payView.getSettings();
        payViewSettings.setJavaScriptEnabled(true);
        payView.setWebViewClient(new MyWebViewClient());
        shouldOverrideUrlLoading(payView, kakaoPayReadyVO.getNext_redirect_app_url());


        //CallbackHandler

        /*try {
            kakaoPay.kakaoPayApprove(kakaoPayReadyVO.getTid(), pg_token);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*payView.setWebChromeClient(new WebChromeClient());
        payViewSettings = payView.getSettings();
        payViewSettings.setJavaScriptEnabled(true);
        shouldOverrideUrlLoading(payView, kakaoPayReadyVO.getNext_redirect_app_url());*/
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url != null && url.startsWith("intent://") && !url.contains("pg_token=")) {
            Intent intent = null;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Uri uri = Uri.parse(intent.getDataString());
                Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                if (existPackage != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
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
        } else if (url != null && url.startsWith("market://") && !url.contains("pg_token=")) {
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
        view.loadUrl(url);
        return false;
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && url.startsWith("intent://") && !url.contains("pg_token=")) {
                Intent intent = null;
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    Uri uri = Uri.parse(intent.getDataString());
                    Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                    if (existPackage != null) {
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
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
            } else if (url != null && url.startsWith("market://") && !url.contains("pg_token=")) {
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
            view.loadUrl(url);
            Log.d("Success url is", url);
            pg_token_url = url;
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}