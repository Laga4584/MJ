package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 명장 정보를 저장하는 객체
 */
public class ReviewItem {

    @SerializedName("user_icon_filename") public String userIconFilename;
    @SerializedName("user_name") public String userName;
    public String review;

    @Override
    public String toString() {
        return "ReviewItem{" +
                "userIconFilename=" + userIconFilename +
                ", userName='" + userName + '\'' +
                ", review='" + review + '\'' +
                '}';
    }
}
