package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 케이스 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class SampleItem {
    public int seq;
    @SerializedName("user_seq") public int userSeq;
    @SerializedName("repairer_seq") public int repairerSeq;
    public String service;
    public String product;
    public String brand;
    @SerializedName("dot_count") public int dotCount;
    public String price;
    public String time;
    public String addressee;
    public String address;
    public String addressDetail;
    @SerializedName("addressee_phone") public String addresseePhone;
    public String memo;
    @SerializedName("price_final") public String priceFinal;
    @SerializedName("time_final") public String timeFinal;
    public String report;
    @SerializedName("repair_state") public String repairState;
    @SerializedName("time_result") public String timeResult;
    public float score;
    @SerializedName("error_rate") public float errorRate;
    @SerializedName("error_rate_price") public float errorRateTime;
    @SerializedName("error_rate_time") public float errorRatePrice;
    public int tag1;
    public int tag2;
    public int tag3;
    public int tag4;
    public String review;
    public String status;
    public String status2;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("image_filename") public String imageFilename;
    @SerializedName("repairer_name") public String repairerName;
    @SerializedName("repairer_image_filename") public String repairerImageFilename;

    @Override
    public String toString() {
        return "SampleItem{" +
                "seq=" + seq +
                ", user_seq=" + userSeq +
                ", repairer_seq=" + repairerSeq +
                ", service='" + service + '\'' +
                ", product='" + product + '\'' +
                ", brand='" + brand + '\'' +
                ", dotCount='" + dotCount + '\'' +
                ", price='" + price + '\'' +
                ", time='" + time + '\'' +
                ", addressee='" + addressee + '\'' +
                ", address='" + address + '\'' +
                ", address_detail='" + addressDetail + '\'' +
                ", addresseePhone='" + addresseePhone + '\'' +
                ", memo='" + memo + '\'' +
                ", priceFinal='" + priceFinal + '\'' +
                ", timeFinal='" + timeFinal + '\'' +
                ", report='" + report + '\'' +
                ", repair_state='" + repairState + '\'' +
                ", time_result='" + timeResult + '\'' +
                ", score='" + score + '\'' +
                ", errorRate='" + errorRate + '\'' +
                ", errorRatePrice='" + errorRatePrice + '\'' +
                ", errorRateTime='" + errorRateTime + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", tag3='" + tag3 + '\'' +
                ", tag4='" + tag4 + '\'' +
                ", review='" + review + '\'' +
                ", status='" + status + '\'' +
                ", status2='" + status2 + '\'' +
                ", regDate='" + regDate + '\'' +
                ", image_filename='" + imageFilename + '\'' +
                ", repairer_name='" + repairerName + '\'' +
                ", repairerImageFilename='" + repairerImageFilename + '\'' +
                '}';
    }
}
