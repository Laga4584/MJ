package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 즐겨찾기 정보를 저장하는 객체
 */
public class KeepItem extends FoodInfoItem{
    @SerializedName("keep_seq") public String keepSeq;
    @SerializedName("keep_user_seq") public String keepUserSeq;
    @SerializedName("keep_date") public String keepDate;

    @Override
    public String toString() {
        return "KeepItem{" +
                "keepSeq='" + keepSeq + '\'' +
                ", keepUserSeq='" + keepUserSeq + '\'' +
                ", keepDate='" + keepDate + '\'' +
                '}';
    }
}
