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
    public String method;
    public String price;
    public String time;
    public String address;
    public String address_detail;
    public String review;
    public String status;
    public String status2;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("mod_date") public String modDate;
    @SerializedName("image_filename") public String imageFilename;
    @SerializedName("image_memo") public String imageMemo;

    @Override
    public String toString() {
        return "CaseInfoItem{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", service='" + service + '\'' +
                ", product='" + product + '\'' +
                ", description='" + description + '\'' +
                ", method='" + method + '\'' +
                ", price='" + price + '\'' +
                ", time='" + time + '\'' +
                ", address='" + address + '\'' +
                ", address_detail='" + address_detail + '\'' +
                ", review='" + review + '\'' +
                ", status='" + status + '\'' +
                ", status2='" + status2 + '\'' +
                ", regDate='" + regDate + '\'' +
                ", modDate='" + modDate + '\'' +
                ", image_filename='" + imageFilename + '\'' +
                ", image_memo='" + imageMemo + '\'' +
                '}';
    }
}
