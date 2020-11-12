package com.example.bestfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
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
                    Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                    UserAccount kakaoAccount = result.getKakaoAccount();
                    if (kakaoAccount != null) {

                        // 이메일
                        String email = kakaoAccount.getEmail();

                        if (email != null) {
                            Log.i("KAKAO_API", "email: " + email);
                            intent.putExtra("email", email);

                        } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                            intent.putExtra("email", email);

                            // 동의 요청 후 이메일 획득 가능
                            // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                        } else {
                            intent.putExtra("email", "none");
                            // 이메일 획득 불가
                        }

                        // 성별
                        String gender = String.valueOf(kakaoAccount.getGender());

                        if (!gender.equals("null")) {
                            Log.i("KAKAO_API", "gender: " + gender);
                            intent.putExtra("gender", gender);

                        } else if (kakaoAccount.genderNeedsAgreement() == OptionalBoolean.TRUE) {
                            intent.putExtra("gender", gender);

                            // 동의 요청 후 이메일 획득 가능
                            // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                        } else {
                            intent.putExtra("gender", "none");
                            // 이메일 획득 불가
                        }

                        // 연령대
                        String ageRange = String.valueOf(kakaoAccount.getAgeRange());

                        if (!ageRange.equals("null")) {
                            Log.i("KAKAO_API", "ageRange: " + ageRange);
                            intent.putExtra("ageRange", ageRange);

                        } else if (kakaoAccount.ageRangeNeedsAgreement() == OptionalBoolean.TRUE) {
                            intent.putExtra("ageRange", ageRange);

                            // 동의 요청 후 이메일 획득 가능
                            // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                        } else {
                            intent.putExtra("ageRange", "none");
                            // 이메일 획득 불가
                        }

                        // 생일
                        String birthday = kakaoAccount.getBirthday();

                        if (birthday != null) {
                            Log.i("KAKAO_API", "birthday: " + birthday);
                            intent.putExtra("birthday", birthday);

                        } else if (kakaoAccount.birthdayNeedsAgreement() == OptionalBoolean.TRUE) {
                            intent.putExtra("birthday", birthday);

                            // 동의 요청 후 이메일 획득 가능
                            // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.

                        } else {
                            intent.putExtra("birthday", "none");
                            // 이메일 획득 불가
                        }

                        // 프로필
                        Profile profile = kakaoAccount.getProfile();

                        if (profile != null) {
                            Log.d("KAKAO_API", "nickname: " + profile.getNickname());
                            Log.d("KAKAO_API", "profile image: " + profile.getProfileImageUrl());
                            Log.d("KAKAO_API", "thumbnail image: " + profile.getThumbnailImageUrl());
                            intent.putExtra("name", profile.getNickname());

                        } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                            // 동의 요청 후 프로필 정보 획득 가능

                        } else {
                            // 프로필 획득 불가
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

                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}