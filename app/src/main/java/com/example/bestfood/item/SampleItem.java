package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 맛집 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class SampleItem {
    public int seq;
    public String repairer;
    public String brand;
    public String service;
    public String product;
    public String description;
    public String method;
    public String price;
    public String result_time;
    public String result_description;
    public String review;
    public float score;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("mod_date") public String modDate;
    @SerializedName("before_image_filename") public String beforeImageFilename;
    @SerializedName("after_image_filename") public String afterImageFilename;
    public String dot;

    @Override
    public String toString() {
        return "SampleItem{" +
                "seq=" + seq +
                ", repairer='" + repairer + '\'' +
                ", brand='" + brand + '\'' +
                ", service='" + service + '\'' +
                ", product='" + product + '\'' +
                ", description='" + description + '\'' +
                ", method='" + method + '\'' +
                ", price='" + price + '\'' +
                ", result_time='" + result_time + '\'' +
                ", result_description='" + result_description + '\'' +
                ", review='" + review + '\'' +
                ", regDate='" + regDate + '\'' +
                ", modDate='" + modDate + '\'' +
                ", before_image_filename='" + beforeImageFilename + '\'' +
                ", after_image_filename='" + afterImageFilename + '\'' +
                ", dot='" + dot + '\'' +
                '}';
    }
}
