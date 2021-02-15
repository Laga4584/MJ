package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.EtcLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {
    UserItem newItem;
    Context context;
    private final String TAG = this.getClass().getSimpleName();
    private SessionCallback sessionCallback;
    private EditText login_email, login_password;
    private CardView loginButton, registerButton, kakakoButton;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FCM 토큰
        FirebaseApp.initializeApp(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            MyLog.d(TAG, "Fetching FCM registration token failed " + task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        MyLog.d(TAG, "Firebase token " + token);
                    }
                });
        //FCM 여기까지

        login_email = findViewById( R.id.login_email );
        login_password = findViewById( R.id.login_password );

        registerButton = findViewById( R.id.button_register );
        registerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LoginActivity.this, RegisterActivity.class );
                startActivity( intent );
            }
        });

        loginButton = findViewById( R.id.button_login );
        loginButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserEmail = login_email.getText().toString();
                String UserPwd = login_password.getText().toString();

                login(UserEmail, UserPwd);
            }
        });

        //기존 코드
        newItem = new UserItem();
        // 여기서부터 나중에 고쳐야함 임시로 추가
        //newItem.phone = EtcLib.getInstance().getPhoneNumber(this);
        //Log.i("newItem", newItem.toString());
        //여기까지
        context = this;

        kakakoButton = findViewById(R.id.button_kakao);
        kakakoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionCallback = new SessionCallback();
                Session.getCurrentSession().addCallback(sessionCallback);
                Session.getCurrentSession().checkAndImplicitOpen();
            }
        });

    }

    private void login(String email, String password){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<UserItem> call = remoteService.loginUserInfo(email, password, token);
        call.enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                UserItem item = response.body();

                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)) {
                    setUserInfoItem(item);
                    MyLog.d(TAG, "success " + response.body().toString());
                    MyToast.s(context, "환영합니다");
                    finish();
                } else {
                    MyLog.d(TAG, "not success");
                    MyToast.s(context, "로그인에 실패하셨습니다");
                }
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    String newBirthYear, newBirthDay;

                    UserAccount kakaoAccount = result.getKakaoAccount();
                    if (kakaoAccount != null) {

                        // 이메일
                        String email = kakaoAccount.getEmail();

                        if (email != null) {
                            Log.i("KAKAO_API", "email: " + email);
                            newItem.email = email;

                        } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                            // 동의 요청 후 이메일 획득 가능
                            // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                            newItem.email = email;

                        } else {
                            // 이메일 획득 불가
                            newItem.email = "none";
                        }

                        // 성별
                        String gender = String.valueOf(kakaoAccount.getGender());

                        if (!gender.equals("null")) {
                            Log.i("KAKAO_API", "gender: " + gender);
                            //newItem.sextype = gender;
                            if(gender.equals("MALE")) newItem.sextype = "남성";
                            else newItem.sextype = "여성";

                        } else if (kakaoAccount.genderNeedsAgreement() == OptionalBoolean.TRUE) {
                            //newItem.sextype = gender;
                            if(gender.equals("MALE")) newItem.sextype = "남성";
                            else newItem.sextype = "여성";


                        } else {
                            newItem.sextype = "none";
                        }

                        // 생년
                        String birthYear = String.valueOf(kakaoAccount.getBirthyear());

                        if (!birthYear.equals("null")) {
                            Log.i("KAKAO_API", "birthYear: " + birthYear);
                            newBirthYear = birthYear;

                        } else if (kakaoAccount.ageRangeNeedsAgreement() == OptionalBoolean.TRUE) {
                            newBirthYear = birthYear;

                        } else {
                            newBirthYear = "none";
                        }

                        // 생일
                        String birthday = kakaoAccount.getBirthday();

                        if (birthday != null) {
                            Log.i("KAKAO_API", "birthday: " + birthday);
                            newBirthDay = birthday;

                        } else if (kakaoAccount.birthdayNeedsAgreement() == OptionalBoolean.TRUE) {
                            newBirthDay = birthday;

                        } else {
                            newBirthDay = "none";
                        }

                        newItem.birthday = newBirthYear + newBirthDay;

                        // 프로필
                        Profile profile = kakaoAccount.getProfile();

                        if (profile != null) {
                            Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                            Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                            Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());
                            newItem.name = profile.getNickname();

                        } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                            // 동의 요청 후 프로필 정보 획득 가능
                            newItem.name = profile.getNickname();

                        } else {
                            // 프로필 획득 불가
                            newItem.name = "none";
                        }
                    }
                    /*
                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());

                    //아래 단락이 추가된 부분. 차례대로 이메일, 나잇대, 성별, 생일 정보가 존재하는지 확인하고,
                    //있으면 받아온다. 없으면 그냥 "none"을 보낸다.
                    if(result.getKakaoAccount().emailNeedsAgreement() == OptionalBoolean.TRUE)
                        intent.putExtra("email", result.getKakaoAccount().getEmail());
                    else
                        intent.putExtra("email", "none");
                    if(result.getKakaoAccount().ageRangeNeedsAgreement() == OptionalBoolean.TRUE)
                        intent.putExtra("ageRange", result.getKakaoAccount().getAgeRange().getValue());
                    else
                        intent.putExtra("ageRange", "none");
                    if(result.getKakaoAccount().genderNeedsAgreement() == OptionalBoolean.TRUE)
                        intent.putExtra("gender", result.getKakaoAccount().getGender().getValue());
                    else
                        intent.putExtra("gender", "none");
                    if(result.getKakaoAccount().birthdayNeedsAgreement() == OptionalBoolean.TRUE)
                        intent.putExtra("birthday", result.getKakaoAccount().getBirthday());
                    else
                        intent.putExtra("birthday", "none");

                     */
                    //여기까지가 추가된 부분.
                    //selectUserInfo(email_2);
                    selectUserInfo(newItem.email);
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 리트로핏을 활용해서 서버로부터 사용자 정보를 조회한다.
     * 사용자 정보를 조회했다면 setUserInfoItem() 메소드를 호출하고
     * 그렇지 않다면 goProfileActivity() 메소드를 호출한다.
     *
     * @param email 이메일
     */
    public void selectUserInfo(String email) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<UserItem> call = remoteService.selectUserInfo(email);
        call.enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                UserItem item = response.body();

                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)) {
                    MyLog.d(TAG, "success " + response.body().toString());
                    setUserInfoItem(item);
                } else {
                    MyLog.d(TAG, "not success");
                    MyLog.d(TAG, newItem.toString());
                    insertUserInfo(newItem);
                }
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    /**
     * 전달받은 UserInfoItem을 Application 객체에 저장한다.
     * 그리고 startMain() 메소드를 호출한다.
     *
     * @param item 사용자 정보
     */
    private void setUserInfoItem(UserItem item) {
        ((App) getApplicationContext()).setUserItem(item);

        startMain();
    }

    /**
     * MainActivity를 실행하고 현재 액티비티를 종료한다.
     */
    public void startMain() {
        finish();
    }

    /**
     * 사용자 정보를 조회하지 못했다면 insertUserPhone() 메소드를 통해
     * 전화번호를 서버에 저장하고 MainActivity를 실행한 후 ProfileActivity를 실행한다.
     * 그리고 현재 액티비티를 종료한다.
     *
     * @param item 사용자 정보
     */

    private void insertUserInfo(UserItem item) {
        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertUserInfo(item, token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String seq = response.body();
                    try {
                        newItem.seq = Integer.parseInt(seq);
                        if (newItem.seq == 0) {
                            MyToast.s(context, R.string.user_insert_fail_message);
                            return;
                        }
                    } catch (Exception e) {
                        MyToast.s(context, R.string.user_insert_fail_message);
                        return;
                    }
                    setUserInfoItem(newItem);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

}