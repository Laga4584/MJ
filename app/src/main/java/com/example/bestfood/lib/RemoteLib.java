package com.example.bestfood.lib;

import android.content.Context;
import android.net.ConnectivityManager;

import android.net.Network;
//import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bestfood.App;
import com.example.bestfood.CaseActivity;
import com.example.bestfood.R;
import com.example.bestfood.item.CaseItem;
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
    public String[] status_list_1 = {"요청", "확인", "결제", "발송", "수선", "후기", "완료", "취소"};
    public String[] status_list_2 = {"대기", "승인 대기", "수선 중", "수선 완료", "주소 확인", "완료"};

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
     * 사용자가 입력한 정보를 서버에 저장한다.
     */
    public void insertCaseInfo(CaseItem caseItem, final Handler handler) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.insertCaseInfo(caseItem);
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
                        //caseItem.seq = seq;
                        //goNextPage();
                        //CaseActivity.caseItem = caseItem;
                        //insertCaptureInfo(captureItemList.size(), seq);
                        handler.sendEmptyMessage(seq);
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

    public void updateCaseStatus(int caseSeq, final int index1, final int index2, final Handler handler){
        final String status1 = status_list_1[index1];
        final String status2 = status_list_2[index2];
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.updateCaseStatus(caseSeq, status1, status2);
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
                        //handler.sendEmptyMessage(0);
                        Message message = handler.obtainMessage();
                        message.what = seq;
                        message.arg1 = index1;
                        message.arg2 = index2;
                        handler.sendMessage(message);
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
