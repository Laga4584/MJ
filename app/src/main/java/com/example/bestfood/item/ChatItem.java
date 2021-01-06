package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 맛집 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class ChatItem {
    public int seq;
    @SerializedName("user_seq") public int userSeq;
    @SerializedName("repairer_seq") public int repairerSeq;
    public int sending;
    public String message;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "FoodInfoItem{" +
                "seq=" + seq +
                ", userSeq=" + userSeq +
                ", repairerSeq=" + repairerSeq +
                ", sending=" + sending +
                ", message=" + message +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
