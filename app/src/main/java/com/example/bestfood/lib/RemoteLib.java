package com.example.bestfood.lib;

import android.content.Context;
import android.net.ConnectivityManager;

import android.net.Network;
//import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bestfood.App;
import com.example.bestfood.CaseActivity;
import com.example.bestfood.R;
import com.example.bestfood.item.UserItem;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 네트워크와 서버와 관련된 라이브러리
 */
public class RemoteLib {
    boolean check = false;
    boolean available;
    public static final String TAG = RemoteLib.class.getSimpleName();

    private volatile static RemoteLib instance;
    String[] status_list_1 = {"견적 요청", "견적 확인", "결제", "발송", "수선", "배송", "후기"};

    public static RemoteLib getInstance() {
        if (instance == null) {
            synchronized (RemoteLib.class) {
                if (instance == null) {
                    instance = new RemoteLib();
                }
            }
        }
        return instance;
    }

    /**
     * 네트워크 연결 여부를 반환한다.
     * @param context 컨텍스트
     * @return 네트워크 연결여부
     */

    public boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        cm.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                // 네트워크를 사용할 준비가 되었을 때
                MyLog.d("here network available");
                available = true;
                check = true;

            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                // 네트워크가 끊겼을 때
                MyLog.d("here network not available");
                available = false;
                check = true;
            }
        });


        while (true){
            if(check)return available;
        }
    }

    /**
    public boolean isConnected(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();

            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    */
    /**
     * 사용자 프로필 아이콘을 서버에 업로드한다.
     * @param userSeq 사용자 일련번호
     * @param file 파일 객체
     */
    public void uploadUserIcon(int userSeq, File file) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("multipart/form-data"));

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody userSeqBody =
                RequestBody.create(
                        "" + userSeq, MediaType.parse("multipart/form-data"));

        Call<ResponseBody> call =
                remoteService.uploadUserIcon(userSeqBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                MyLog.d(TAG, "uploadUserIcon success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadUserIcon fail");
            }
        });
    }

    /**
     * 케이스 이미지를 서버에 업로드한다.
     * @param infoSeq 케이스 정보 일련번호
     * @param label 이미지 설명
     * @param file 파일 객체
     * @param handler 처리 결과를 응답할 핸들러
     */
    public void uploadCaseImage(int infoSeq, String label, File file, final Handler handler) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("multipart/form-data"));

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody infoSeqBody =
                RequestBody.create(
                         "" + infoSeq, MediaType.parse("multipart/form-data"));
        RequestBody labelBody =
                RequestBody.create(
                        label, MediaType.parse("multipart/form-data"));

        Call<ResponseBody> call =
                remoteService.uploadCaseImage(infoSeqBody, labelBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                MyLog.d(TAG, "uploadCaseImage success");
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadCaseImage fail");
            }
        });
    }

    public void updateCaseStatus(int index1, int index2){
        final String status1 = status_list_1[index1];
        final String status2;
        if (index1 == 3 && index2 == 1 || index1 == 4 && index2 == 4) status2 = "발송 대기";
        else if (index1 == 4 && index2 == 5) status2 = "배송 완료";
        else status2 = CaseActivity.caseItem.status2;

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.updateCaseStatus(CaseActivity.caseItem.seq, status1, status2);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int seq = 0;
                    String seqString = response.body();

                    try {
                        seq = Integer.parseInt(seqString);
                    } catch (Exception e) {
                        seq = 0;
                    }

                    if (seq == 0) {
                        //등록 실패
                    } else {
                        CaseActivity.caseItem.seq = seq;
                        CaseActivity.caseItem.status = status1;
                        CaseActivity.caseItem.status2 = status2;
                        //goNextPage();
                    }
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

    public void uploadCaseDot(int seq, String dot){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.uploadCaseDot(seq, dot);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int seq = 0;
                    String seqString = response.body();

                    try {
                        seq = Integer.parseInt(seqString);
                    } catch (Exception e) {
                        seq = 0;
                    }

                    if (seq == 0) {
                        //등록 실패
                    } else {
                        //CaseActivity.caseItem.seq = seq;
                        //goNextPage();
                    }
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

    public void selectKeepInfo(int userSeq, int repairerSeq, int mode, final ImageButton keepButton) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.selectKeep(userSeq, repairerSeq, mode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    keepButton.setSelected(true);
                } else {
                    keepButton.setSelected(false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

    public void insertKeep(int userSeq, int itemSeq, int mode){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertKeep(userSeq, itemSeq, mode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                        MyLog.d(TAG, "insertKeep success");
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "insertKeep fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }

    public void deleteKeep(int userSeq, int itemSeq, int mode){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.deleteKeep(userSeq, itemSeq, mode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                        MyLog.d(TAG, "deleteKeep fail");
                } else { // 등록 실패
                    int statusCode = response.code();
                    ResponseBody errorBody = response.errorBody();
                    MyLog.d(TAG, "deleteKeep fail " + statusCode + errorBody.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
            }
        });
    }
}
