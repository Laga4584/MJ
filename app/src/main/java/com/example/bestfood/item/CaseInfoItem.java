package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 맛집 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class CaseInfoItem {
    public int seq;
    @SerializedName("user_seq") public int userSeq;
    public String service;
    public String product;
    public String brand;
    public String dot;
    public String description;
    public String method;
    public String price;
    public String time;
    public String address;
    public String addressDetail;
    public String descriptionAdd;
    public String priceAdd;
    @SerializedName("repair_description") public String repairDescription;
    public String repairState;
    public String review;
    public float score;
    public String status;
    public String status2;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("image_filename") public String imageFilename;
    @SerializedName("repair_image_filename") public String repairImageFilename;

    @Override
    public String toString() {
        return "CaseInfoItem{" +
                "seq=" + seq +
                ", user_seq=" + userSeq +
                ", service='" + service + '\'' +
                ", product='" + product + '\'' +
                ", brand='" + brand + '\'' +
                ", dot='" + dot + '\'' +
                ", description='" + description + '\'' +
                ", method='" + method + '\'' +
                ", price='" + price + '\'' +
                ", time='" + time + '\'' +
                ", address='" + address + '\'' +
                ", address_detail='" + addressDetail + '\'' +
                ", description_add='" + descriptionAdd + '\'' +
                ", price_add='" + priceAdd + '\'' +
                ", repair_description='" + repairDescription + '\'' +
                ", repair_state='" + repairState + '\'' +
                ", review='" + review + '\'' +
                ", score='" + score + '\'' +
                ", status='" + status + '\'' +
                ", status2='" + status2 + '\'' +
                ", regDate='" + regDate + '\'' +
                ", image_filename='" + imageFilename + '\'' +
                ", repair_image_filename='" + repairImageFilename + '\'' +
                '}';
    }
}
