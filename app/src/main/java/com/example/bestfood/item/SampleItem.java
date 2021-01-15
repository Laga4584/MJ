package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 명작 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class SampleItem {
    public int seq;
    @SerializedName("repairer_seq") public int repairerSeq;
    public String brand;
    public String service;
    public String product;
    public String description;
    public String method;
    public String price;
    @SerializedName("result_time") public String resultTime;
    @SerializedName("result_description") public String resultDescription;
    public String review;
    public float score;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("mod_date") public String modDate;
    @SerializedName("before_image_filename") public String beforeImageFilename;
    @SerializedName("after_image_filename") public String afterImageFilename;
    public String dot;
    @SerializedName("profile_image_filename") public String profileImageFilename;

    @Override
    public String toString() {
        return "SampleItem{" +
                "seq=" + seq +
                ", repairerSeq='" + repairerSeq + '\'' +
                ", brand='" + brand + '\'' +
                ", service='" + service + '\'' +
                ", product='" + product + '\'' +
                ", description='" + description + '\'' +
                ", method='" + method + '\'' +
                ", price='" + price + '\'' +
                ", resultTime='" + resultTime + '\'' +
                ", result_description='" + resultDescription + '\'' +
                ", review='" + review + '\'' +
                ", regDate='" + regDate + '\'' +
                ", modDate='" + modDate + '\'' +
                ", before_image_filename='" + beforeImageFilename + '\'' +
                ", after_image_filename='" + afterImageFilename + '\'' +
                ", dot='" + dot + '\'' +
                ", profile_image_filename='" + profileImageFilename + '\'' +
                '}';
    }
}
