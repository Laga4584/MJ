package com.example.bestfood.item;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 이미지 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class CaptureItem {
    public Bitmap bitmap;
    public String description;
    public ArrayList<Integer> positionX;
    public ArrayList<Integer> positionY;

    @Override
    public String toString() {
        return "CaptureItem{" +
                '}';
    }
}
