package com.example.bestfood.remote;

import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.item.ChatItem;
import com.example.bestfood.item.FoodInfoItem;
import com.example.bestfood.item.KeepItem;
import com.example.bestfood.item.MemberInfoItem;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */
public interface RemoteService {
    //String BASE_URL = "http://192.168.1.134:3000";
    String BASE_URL = "http://ec2-54-180-82-94.ap-northeast-2.compute.amazonaws.com:3000";
    String MEMBER_ICON_URL = BASE_URL + "/member/";
    String IMAGE_URL = BASE_URL + "/img/";

    //채팅
    @GET("/chat/list")
    Call<ArrayList<ChatItem>> selectChatInfo(@Query("member_seq") int memberSeq,
                                             @Query("repairer_seq") int repairerSeq);

    @POST("/chat/info")
    Call<String> insertChatInfo(@Body ChatItem ChatItem);

    //케이스
    @GET("/case/info/{seq}")
    Call<CaseInfoItem> selectCaseInfo(@Path("seq") int seq);

    @POST("/case/info")
    Call<String> insertCaseInfo(@Body CaseInfoItem infoItem);

    @GET("/case/list")
    Call<ArrayList<CaseInfoItem>> listCaseInfo(@Query("member_seq") int memberSeq,
                                               @Query("current_page") int currentPage);
    @POST("/case/info/status")
    Call<String> updateCaseStatus(@Query("seq") int seq, @Field("status") String status);


    //사용자 정보
    @GET("/member/{phone}")
    Call<MemberInfoItem> selectMemberInfo(@Path("phone") String phone);

    @POST("/member/info")
    Call<String> insertMemberInfo(@Body MemberInfoItem memberInfoItem);

    @FormUrlEncoded
    @POST("/member/phone")
    Call<String> insertMemberPhone(@Field("phone") String phone);

    @Multipart
    @POST("/member/icon_upload")
    Call<ResponseBody> uploadMemberIcon(@Part("member_seq") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);

    //맛집 정보
    @GET("/food/info/{info_seq}")
    Call<FoodInfoItem> selectFoodInfo(@Path("info_seq") int foodInfoSeq,
                                      @Query("member_seq") int memberSeq);

    @POST("/food/info")
    Call<String> insertFoodInfo(@Body FoodInfoItem infoItem);

    @Multipart
    @POST("/food/info/image")
    Call<ResponseBody> uploadFoodImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part("image_memo") RequestBody imageMemo,
                                       @Part MultipartBody.Part file);

    @GET("/food/list")
    Call<ArrayList<FoodInfoItem>> listFoodInfo(@Query("member_seq") int memberSeq,
                                               @Query("user_latitude") double userLatitude,
                                               @Query("user_longitude") double userLongitude,
                                               @Query("order_type") String orderType,
                                               @Query("current_page") int currentPage);

    //지도
    @GET("/food/map/list")
    Call<ArrayList<FoodInfoItem>> listMap(@Query("member_seq") int memberSeq,
                                          @Query("latitude") double latitude,
                                          @Query("longitude") double longitude,
                                          @Query("distance") int distance,
                                          @Query("user_latitude") double userLatitude,
                                          @Query("user_longitude") double userLongitude);

    //즐겨찾기
    @POST("/keep/{member_seq}/{info_seq}")
    Call<String> insertKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @DELETE("/keep/{member_seq}/{info_seq}")
    Call<String> deleteKeep(@Path("member_seq") int memberSeq, @Path("info_seq") int infoSeq);

    @GET("/keep/list")
    Call<ArrayList<KeepItem>> listKeep(@Query("member_seq") int memberSeq,
                                       @Query("user_latitude") double userLatitude,
                                       @Query("user_longitude") double userLongitude);




}
