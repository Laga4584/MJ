package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 사용자 정보를 저장하는 객체
 */
public class UserInfoItem {
    public int seq;
    public String phone;
    public String name;
    public String sextype;
    public String birthday;
    public String email;
    public String password;
    @SerializedName("user_icon_filename") public String userIconFilename;
    @SerializedName("reg_date") public String regDate;

    @Override
    public String toString() {
        return "UserInfoItem{" +
                "seq=" + seq +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", sextype='" + sextype + '\'' +
                ", birthday='" + birthday + '\'' +
                ", userIconFilename='" + userIconFilename + '\'' +
                ", regDate='" + regDate + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}