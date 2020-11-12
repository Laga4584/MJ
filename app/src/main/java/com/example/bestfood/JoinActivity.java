package com.example.bestfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

/*
  2019.02.06 ~ 2019.02.19 made by Candykick(KR)
  카카오 로그인 API 블로그 예제
  로그인한 사용자의 정보를 표시하고, 로그아웃 및 회원탈퇴를 할 수 있는 Activity.
  사용한 API: 카카오 로그인 API 1.16.0
  이 소스 코드는 개발중이신 앱에 그대로 가져다가 쓰셔도 무방합니다.
  단, 이 소스 코드를 가지고 그대로 본인의 블로그 및 책 등에 사용하는 것은 허용하지 않습니다.
 */

public class JoinActivity extends AppCompatActivity {

    //이메일, 나잇대, 성별, 생일값 String 추가됨
    String strNickname, strProfile, strEmail, strAgeRange, strGender, strBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        TextView tvNickname = findViewById(R.id.tvNickname);
        //ImageView ivProfile = findViewById(R.id.ivProfile);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnSignout = findViewById(R.id.btnSignout);
        //순서대로 각각 이메일, 나잇대, 성별, 생일을 보여주는 TextView 선언
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvAgeRange = findViewById(R.id.tvAgeRange);
        TextView tvGender = findViewById(R.id.tvGender);
        TextView tvBirthday = findViewById(R.id.tvBirthday);

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        //이메일, 나잇대, 성별, 생일을 intent에서 가져와서 각 String에 저장함
        strEmail = intent.getStringExtra("email");
        strAgeRange = intent.getStringExtra("ageRange");
        strGender = intent.getStringExtra("gender");
        strBirthday = intent.getStringExtra("birthday");

        tvNickname.setText(strNickname);
        //Glide.with(this).load(strProfile).into(ivProfile);
        //받아온 정보를 각 TextView에 표시함
        tvEmail.setText(strEmail);
        tvAgeRange.setText(strAgeRange);
        tvGender.setText(strGender);
        tvBirthday.setText(strBirthday);

        // 이 아래로는 저번 3강에서 작성한 btnLogout(로그아웃 버튼), btnSignout(회원탈퇴 버튼) 소스코드가 있습니다. 길이 문제상 여기서는 생략합니다.
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        btnSignout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(JoinActivity.this)
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onSuccess(Long result) {
                                        Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }
}