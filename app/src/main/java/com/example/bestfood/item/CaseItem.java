package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 케이스 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class CaseItem {
    public int seq;
    @SerializedName("user_seq") public int userSeq;
    @SerializedName("repairer_seq") public int repairerSeq;
    public String service;
    public String product;
    public String brand;
    @SerializedName("dot_count") public int dotCount;
    public String price;
    public String time;
    @SerializedName("pay_method") public String payMethod;
    @SerializedName("pay_corp") public String payCorp;
    @SerializedName("pay_amount") public int payAmount;
    @SerializedName("pay_interest") public String payInterest;
    @SerializedName("pay_method2") public String payMethod2;
    @SerializedName("pay_corp2") public String payCorp2;
    @SerializedName("pay_amount2") public int payAmount2;
    @SerializedName("pay_interest2") public String payInterest2;
    public String addressee;
    public String address;
    @SerializedName("address_detail") public String addressDetail;
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
        return "CaseItem{" +
                "seq=" + seq +
                ", userSeq=" + userSeq +
                ", repairerSeq=" + repairerSeq +
                ", service='" + service + '\'' +
                ", product='" + product + '\'' +
                ", brand='" + brand + '\'' +
                ", dotCount='" + dotCount + '\'' +
                ", price='" + price + '\'' +
                ", time='" + time + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", payCorp='" + payCorp + '\'' +
                ", payAmount='" + payAmount + '\'' +
                ", payInterest='" + payInterest + '\'' +
                ", payMethod='" + payMethod2 + '\'' +
                ", payCorp='" + payCorp2 + '\'' +
                ", payAmount='" + payAmount2 + '\'' +
                ", payInterest='" + payInterest2 + '\'' +
                ", addressee='" + addressee + '\'' +
                ", address='" + address + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", addresseePhone='" + addresseePhone + '\'' +
                ", memo='" + memo + '\'' +
                ", priceFinal='" + priceFinal + '\'' +
                ", timeFinal='" + timeFinal + '\'' +
                ", report='" + report + '\'' +
                ", repairState='" + repairState + '\'' +
                ", timeResult='" + timeResult + '\'' +
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
                ", imageFilename='" + imageFilename + '\'' +
                ", repairerName='" + repairerName + '\'' +
                ", repairerImageFilename='" + repairerImageFilename + '\'' +
                '}';
    }
}
