package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 맛집 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class NoticeItem {
    public int seq;
    @SerializedName("user_seq") public int userSeq;
    @SerializedName("title") public String title;
    @SerializedName("body") public String body;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "NoticeItem{" +
                "seq=" + seq +
                ", userSeq=" + userSeq +
                ", title=" + title +
                ", body=" + body +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
