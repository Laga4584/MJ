package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 명장 정보를 저장하는 객체
 */
public class CheckItem {
    public int seq;
    @SerializedName("repairer_seq") public int repairerSeq;
    public String name;
    public String product;
    @SerializedName("case_count") public String caseCount;
    public float score;
    @SerializedName("profile_img_filename") public String profileImgFilename;

    public String description;
    public String price;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "CheckItem{" +
                "seq=" + seq +
                ", repairerSeq='" + repairerSeq + '\'' +
                ", name='" + name + '\'' +
                ", product='" + product + '\'' +
                ", caseCount='" + caseCount + '\'' +
                ", score='" + score + '\'' +
                ", profileImgFilename='" + profileImgFilename + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
