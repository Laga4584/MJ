package com.example.bestfood.remote;

import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.item.ChatItem;
import com.example.bestfood.item.FoodInfoItem;
import com.example.bestfood.item.ImageItem;
import com.example.bestfood.item.KeepItem;
import com.example.bestfood.item.UserInfoItem;
import com.example.bestfood.item.SampleItem;

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
    String USER_ICON_URL = BASE_URL + "/user/";
    String IMAGE_URL = BASE_URL + "/img/";
    String SAMPLE_URL = BASE_URL + "/sample/";

    //샘플
    @GET("/sample/info/{seq}")
    Call<SampleItem> selectSampleInfo(@Path("seq") int seq);

    @GET("/sample/list")
    Call<ArrayList<SampleItem>> listSampleInfo(@Query("current_page") int currentPage);

    //채팅
    @GET("/chat/list")
    Call<ArrayList<ChatItem>> selectChatInfo(@Query("user_seq") int userSeq,
                                             @Query("repairer_seq") int repairerSeq);


    @POST("/chat/info")
    Call<String> insertChatInfo(@Body ChatItem ChatItem);

    //케이스
    @GET("/case/info/{seq}")
    Call<CaseInfoItem> selectCaseInfo(@Path("seq") int seq);

    @POST("/case/info")
    Call<String> insertCaseInfo(@Body CaseInfoItem infoItem);

    @GET("/case/list")
    Call<ArrayList<CaseInfoItem>> listCaseInfo(@Query("user_seq") int userSeq,
                                               @Query("current_page") int currentPage);

    @POST("/case/status")
    Call<String> updateCaseStatus(@Query("seq") int seq, @Query("status") String status, @Query("status2") String status2);

    @Multipart
    @POST("/case/info/image")
    Call<ResponseBody> uploadCaseImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part("label") RequestBody label,
                                       @Part MultipartBody.Part file);

    @POST("/case/image/delete")
    Call<String> deleteImageInfo(@Query("seq") int seq);

    @GET("/case/image/list")
    Call<ArrayList<ImageItem>> listImageInfo(@Query("info_seq") int infoSeq);

    @POST("/case/info/dot")
    Call<String> uploadCaseDot(@Query("seq") int seq,
                                     @Query("dot") String dot);

    //사용자 정보
    @GET("/user/email")
    Call<UserInfoItem> selectUserInfo(@Query("email") String email);

    @POST("/user/info")
    Call<String> insertUserInfo(@Body UserInfoItem userInfoItem);

    @FormUrlEncoded
    @POST("/user/phone")
    Call<String> insertUserPhone(@Field("phone") String phone);

    @Multipart
    @POST("/user/icon_upload")
    Call<ResponseBody> uploadUserIcon(@Part("user_seq") RequestBody userSeq,
                                        @Part MultipartBody.Part file);

    @POST("/user/register")
    Call<String> registerUserInfo(@Body UserInfoItem userInfoItem);

    @POST("/user/login")
    Call<UserInfoItem> loginUserInfo(@Query("email") String email, @Query("password") String password, @Query("token") String token);

    //맛집 정보
    @GET("/food/info/{info_seq}")
    Call<FoodInfoItem> selectFoodInfo(@Path("info_seq") int foodInfoSeq,
                                      @Query("user_seq") int userSeq);

    @POST("/food/info")
    Call<String> insertFoodInfo(@Body FoodInfoItem infoItem);

    @Multipart
    @POST("/food/info/image")
    Call<ResponseBody> uploadFoodImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part("image_memo") RequestBody imageMemo,
                                       @Part MultipartBody.Part file);

    @GET("/food/list")
    Call<ArrayList<FoodInfoItem>> listFoodInfo(@Query("user_seq") int userSeq,
                                               @Query("user_latitude") double userLatitude,
                                               @Query("user_longitude") double userLongitude,
                                               @Query("order_type") String orderType,
                                               @Query("current_page") int currentPage);

    //지도
    @GET("/food/map/list")
    Call<ArrayList<FoodInfoItem>> listMap(@Query("user_seq") int userSeq,
                                          @Query("latitude") double latitude,
                                          @Query("longitude") double longitude,
                                          @Query("distance") int distance,
                                          @Query("user_latitude") double userLatitude,
                                          @Query("user_longitude") double userLongitude);

    //즐겨찾기
    @POST("/keep/{user_seq}/{info_seq}")
    Call<String> insertKeep(@Path("user_seq") int userSeq, @Path("info_seq") int infoSeq);

    @DELETE("/keep/{user_seq}/{info_seq}")
    Call<String> deleteKeep(@Path("user_seq") int userSeq, @Path("info_seq") int infoSeq);

    @GET("/keep/list")
    Call<ArrayList<KeepItem>> listKeep(@Query("user_seq") int userSeq,
                                       @Query("user_latitude") double userLatitude,
                                       @Query("user_longitude") double userLongitude);




}
