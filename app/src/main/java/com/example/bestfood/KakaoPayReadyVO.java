package com.example.bestfood;

import java.net.URLConnection;

public class KakaoPayReadyVO {
    private String tid;
    private String next_redirect_app_url;
    private String android_app_scheme;
    private String created_at;

    public void setTid(String tid) {
        this.tid = tid;
    }
    public void setNext_redirect_app_url(String next_redirect_app_url) {
        this.next_redirect_app_url = next_redirect_app_url;
    }
    public void setAndroid_app_scheme(String android_app_scheme) {
        this.android_app_scheme = android_app_scheme;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTid() {
        return tid;
    }
    public String getNext_redirect_app_url() {
        return next_redirect_app_url;
    }
    public String getAndroid_app_scheme() {
        return android_app_scheme;
    }
    public String getCreated_at() {
        return created_at;
    }

    @Override
    public String toString() {
        return "KakaoPayReadyVO {" +
                "tid='" + tid + '\'' +
                ", next_redirect_app_url='" + next_redirect_app_url + '\'' +
                ", android_app_scheme='" + android_app_scheme + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
