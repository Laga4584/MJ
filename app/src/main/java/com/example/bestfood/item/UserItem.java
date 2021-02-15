package com.example.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * 사용자 정보를 저장하는 객체
 */
public class UserItem {
    public int seq;
    public String phone;
    public String name;
    public String sextype;
    public String birthday;
    public String email;
    public String password;
    public String address;
    @SerializedName("address_detail") public String addressDetail;
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
                ", address='" + address + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", userIconFilename='" + userIconFilename + '\'' +
                ", regDate='" + regDate + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
