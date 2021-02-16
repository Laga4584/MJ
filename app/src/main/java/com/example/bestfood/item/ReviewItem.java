package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 명장 정보를 저장하는 객체
 */
public class ReviewItem {
    public int seq;
    public String email;
    public String password;
    public String name;
    public String birthday;
    public String sextype;
    public String phone;
    public String service;
    public String product;
    public String description;
    @SerializedName("case_count") public String caseCount;
    public float score;
    @SerializedName("reply_period") public String replyPeriod;
    public String review;
    public float tag1;
    public float tag2;
    public float tag3;
    public float tag4;
    @SerializedName("profile_img_filename") public String profileImgFilename;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "ReviewItem{" +
                "seq=" + seq +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sextype='" + sextype + '\'' +
                ", phone='" + phone + '\'' +
                ", service='" + service + '\'' +
                ", product='" + product + '\'' +
                ", description='" + description + '\'' +
                ", caseCount='" + caseCount + '\'' +
                ", score='" + score + '\'' +
                ", replyPeriod='" + replyPeriod + '\'' +
                ", review='" + review + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", tag3='" + tag3 + '\'' +
                ", tag4='" + tag4 + '\'' +
                ", profileImgFilename='" + profileImgFilename + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
