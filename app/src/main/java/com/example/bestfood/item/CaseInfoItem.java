package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 맛집 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class CaseInfoItem {
    public int seq;
    @SerializedName("member_seq") public int memberSeq;
    public String service;
    public String product;
    public String description;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("mod_date") public String modDate;
    @SerializedName("image_filename") public String imageFilename;

    @Override
    public String toString() {
        return "FoodInfoItem{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", name='" + service + '\'' +
                ", tel='" + product + '\'' +
                ", description='" + description + '\'' +
                ", regDate='" + regDate + '\'' +
                ", modDate='" + modDate + '\'' +
                ", imageFilename='" + imageFilename + '\'' +
                '}';
    }
}
