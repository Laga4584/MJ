package com.example.bestfood;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bestfood.item.UserInfoItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText join_email, join_password, join_name, join_pwck, sextypeEdit, birthEdit, join_phone;
    private Button join_button, check_button;
    private AlertDialog dialog;
    private boolean validate = false;
    private final String TAG = this.getClass().getSimpleName();
    Context context;
    UserInfoItem registerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        registerItem = new UserInfoItem();

        //아이디값 찾아주기
        join_email = findViewById( R.id.join_email );
        join_password = findViewById( R.id.join_password );
        join_name = findViewById( R.id.join_name );
        join_pwck = findViewById(R.id.join_pwck);

        sextypeEdit = findViewById(R.id.join_sextype);
        sextypeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSexTypeDialog();
            }
        });
        birthEdit = findViewById(R.id.join_birthday);
        birthEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBirthdayDialog();
            }
        });
        join_phone = findViewById(R.id.join_phone);


        //아이디 중복 체크
        check_button = findViewById(R.id.check_button);
        check_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String UserEmail = join_email.getText().toString();
                if (validate) {
                    return; //검증 완료
                }

                if (UserEmail.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                validateEmail(UserEmail);
            }
        });


        //회원가입 버튼 클릭 시 수행
        join_button = findViewById( R.id.join_button );
        join_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String UserEmail = join_email.getText().toString();
                final String UserPwd = join_password.getText().toString();
                final String UserName = join_name.getText().toString();
                final String PassCk = join_pwck.getText().toString();


                //아이디 중복체크 했는지 확인
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("중복된 아이디가 있는지 확인하세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                //한 칸이라도 입력 안했을 경우
                if (UserEmail.equals("") || UserPwd.equals("") || UserName.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                registerItem.name = UserName;
                registerItem.email = UserEmail;
                registerItem.password = UserPwd;
                registerItem.sextype = sextypeEdit.getText().toString();
                registerItem.birthday = birthEdit.getText().toString().replace(" ", "");
                registerUserInfo(registerItem);
            }
        });
    }

    /**
     * 성별을 선택할 수 있는 다이얼로그를 보여준다.
     */
    private void setSexTypeDialog() {
        final String[] sexTypes = new String[2];
        sexTypes[0] = getResources().getString(R.string.sex_man);
        sexTypes[1] = getResources().getString(R.string.sex_woman);

        new AlertDialog.Builder(this)
                .setItems(sexTypes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which >= 0) {
                            sextypeEdit.setText(sexTypes[which]);
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 생일을 선택할 수 있는 다이얼로그를 보여준다.
     */
    private void setBirthdayDialog() {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String myMonth;
                if (monthOfYear + 1 < 10) {
                    myMonth = "0" + (monthOfYear + 1);
                } else {
                    myMonth = "" + (monthOfYear + 1);
                }

                String myDay;
                if (dayOfMonth < 10) {
                    myDay = "0" + dayOfMonth;
                } else {
                    myDay = "" + dayOfMonth;
                }

                String date = year + " " + myMonth + " " + myDay;
                birthEdit.setText(date);
            }
        }, year, month, day).show();
    }

    private void validateEmail(String email){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<UserInfoItem> call = remoteService.selectUserInfo(email);
        call.enqueue(new Callback<UserInfoItem>() {
            @Override
            public void onResponse(Call<UserInfoItem> call, Response<UserInfoItem> response) {
                UserInfoItem item = response.body();

                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)) {
                    MyLog.d(TAG, "success " + response.body().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("이미 존재하는 아이디입니다.").setNegativeButton("확인", null).create();
                    dialog.show();
                } else {
                    MyLog.d(TAG, "not success");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                    join_email.setEnabled(false); //아이디값 고정
                    validate = true; //검증 완료
                    check_button.setBackgroundColor(getResources().getColor(R.color.colorGray));
                }
            }

            @Override
            public void onFailure(Call<UserInfoItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private void registerUserInfo(UserInfoItem item){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.registerUserInfo(item);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String seq = response.body();
                    try {
                        //newItem.seq = Integer.parseInt(seq);
                        if (Integer.parseInt(seq) == 0) {
                            MyToast.s(context, R.string.user_insert_fail_message);
                            return;
                        }
                    } catch (Exception e) {
                        MyToast.s(context, R.string.user_insert_fail_message);
                        return;
                    }
                    MyToast.s(context, "회원가입이 완료되었습니다");
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyToast.s(context, R.string.user_insert_fail_message);
            }
        });
    }

}
