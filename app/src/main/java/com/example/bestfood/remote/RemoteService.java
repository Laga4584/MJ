package com.example.bestfood.remote;

import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.ChatItem;
import com.example.bestfood.item.CheckItem;
import com.example.bestfood.item.ImageItem;
import com.example.bestfood.item.NoticeItem;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.item.ReviewItem;
import com.example.bestfood.item.UserItem;
import com.example.bestfood.item.SampleItem;

import java.util.ArrayList;
import java.util.Map;

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
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */
public interface RemoteService {
    //String BASE_URL = "http://192.168.1.134:3000";
    String BASE_URL = "http://ec2-54-180-82-94.ap-northeast-2.compute.amazonaws.com:3000";
    String USER_ICON_URL = BASE_URL + "/user/";
    String IMAGE_URL = BASE_URL + "/img/";
    String SAMPLE_URL = BASE_URL + "/sample/";

    //알림
    @GET("/user/notice")
    Call<ArrayList<NoticeItem>> listNoticeInfo(@Query("user_seq") int userSeq, @Query("current_page") int currentPage);

    //명장
    @GET("/repairer/info/{seq}")
    Call<RepairerItem> selectRepairerInfo(@Path("seq") int seq);

    @GET("/repairer/list")
    Call<ArrayList<RepairerItem>> listRepairerInfo(@Query("mode") int mode, @Query("query") String query, @Query("current_page") int currentPage);

    @GET("/repairer/review")
    Call<ArrayList<ReviewItem>> listRepairerReview(@Query("repairer_seq") int repairerSeq);

    //샘플
    @GET("/sample/info/{seq}")
    Call<SampleItem> selectSampleInfo(@Path("seq") int seq);

    @GET("/sample/list")
    Call<ArrayList<SampleItem>> listSampleInfo(@Query("mode") int mode, @Query("query") String query, @Query("current_page") int currentPage);

    @GET("/sample/repairer")
    Call<ArrayList<SampleItem>> repairerSampleInfo(@Query("repairer_seq") int repairerSeq, @Query("current_page") int currentPage);

    //채팅
    @GET("/chat/list")
    Call<ArrayList<ChatItem>> selectChatInfo(@Query("user_seq") int userSeq,
                                             @Query("repairer_seq") int repairerSeq);

    @POST("/chat/info")
    Call<String> insertChatInfo(@Body ChatItem ChatItem);

    @Multipart
    @POST("/chat/info/image")
    Call<ResponseBody> uploadChatImage(@Part("chat_seq") RequestBody chatSeq,
                                      @Part MultipartBody.Part file);

    //케이스
    @GET("/case/info/{seq}")
    Call<CaseItem> selectCaseInfo(@Path("seq") int seq);

    @POST("/case/info")
    Call<String> insertCaseInfo(@Body CaseItem infoItem);

    @GET("/case/list")
    Call<ArrayList<CaseItem>> listCaseInfo(@Query("user_seq") int userSeq,
                                               @Query("current_page") int currentPage, @Query("mode") int mode);

    @POST("/case/status")
    Call<String> updateCaseStatus(@Query("seq") int seq, @Query("status") String status, @Query("status2") String status2);

    @Multipart
    @POST("/case/info/image")
    Call<ResponseBody> uploadCaseImage(@Part("info_seq") RequestBody infoSeq,
                                       @Part("label") RequestBody label,
                                       @Part("dot") RequestBody dot,
                                       @Part("request") RequestBody request,
                                       @Part MultipartBody.Part file);

    @POST("/case/image/delete")
    Call<String> deleteImageInfo(@Query("seq") int seq);

    @GET("/case/image/list")
    Call<ArrayList<ImageItem>> listImageInfo(@Query("info_seq") int infoSeq);

    @POST("/case/info/dot")
    Call<String> uploadCaseDot(@Query("seq") int seq,
                                     @Query("dot") String dot);

    @GET("/case/check")
    Call<ArrayList<CheckItem>> listCheckInfo(@Query("case_seq") int caseSeq, @Query("current_page") int currentPage);

    //사용자 정보
    @GET("/user/email")
    Call<UserItem> selectUserInfo(@Query("email") String email);

    @GET("/user/info/{seq}")
    Call<UserItem> selectUserSeq(@Path("seq") int seq);

    @POST("/user/info")
    Call<String> insertUserInfo(@Body UserItem userItem, @Query("token") String token);

    @POST("/user/update")
    Call<String> updateUserInfo(@Body UserItem userItem);

    @FormUrlEncoded
    @POST("/user/phone")
    Call<String> insertUserPhone(@Field("phone") String phone);

    @Multipart
    @POST("/user/icon_upload")
    Call<ResponseBody> uploadUserIcon(@Part("user_seq") RequestBody userSeq,
                                        @Part MultipartBody.Part file);

    @POST("/user/register")
    Call<String> registerUserInfo(@Body UserItem userItem);

    @POST("/user/login")
    Call<UserItem> loginUserInfo(@Query("email") String email, @Query("password") String password, @Query("token") String token);

    //즐겨찾기
    @GET("/keep/repairer")
    Call<ArrayList<RepairerItem>> listRepairerKeep(@Query("user_seq") int userSeq, @Query("current_page") int currentPage);

    @GET("/keep/sample")
    Call<ArrayList<SampleItem>> listSampleKeep(@Query("user_seq") int userSeq, @Query("current_page") int currentPage);

    @GET("/keep/{user_seq}/{item_seq}/{mode}")
    Call<String> selectKeep(@Path("user_seq") int userSeq, @Path("item_seq") int infoSeq, @Path("mode") int mode);

    @POST("/keep/{user_seq}/{item_seq}/{mode}")
    Call<String> insertKeep(@Path("user_seq") int userSeq, @Path("item_seq") int infoSeq, @Path("mode") int mode);

    @DELETE("/keep/{user_seq}/{item_seq}/{mode}")
    Call<String> deleteKeep(@Path("user_seq") int userSeq, @Path("item_seq") int infoSeq, @Path("mode") int mode);

}
