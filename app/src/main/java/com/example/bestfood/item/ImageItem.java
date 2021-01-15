package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 이미지 정보를 저장하는 객체
 */
public class ImageItem {
    @SerializedName("seq") public int seq;
    @SerializedName("info_seq") public int infoSeq;
    @SerializedName("filename") public String filename;
    @SerializedName("label") public String label;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "ImageItem{" +
                "seq=" + seq +
                ", info_seq=" + infoSeq +
                ", filename='" + filename + '\'' +
                ", label='" + label + '\'' +
                ", reg_date='" + regDate + '\'' +
                '}';
    }
}
