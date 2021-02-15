package com.example.bestfood;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_1 = 100;
    public static final int REQUEST_CODE_2 = 200;
    private EditText emailEdit, passwordEdit, nameEdit, pwckEdit;
    private ImageButton backButton, agreeAllButton, agreeButton1, agreeButton2, agreeButton3;
    private TextView checkButton, registerButton, sextypeText, birthText;
    private AlertDialog dialog;
    private boolean validate = false;
    private boolean validate2 = false;
    private boolean validate3 = false;
    private final String TAG = this.getClass().getSimpleName();
    Context context;
    UserItem userItem;

    String[] items_sextype = {"남성", "여성"};
    int[] items_1 = new int[80];
    int[] items_2 = new int[12];
    int[] items_3 = new int[31];
    String[] items_year = new String[80];
    String[] items_month = new String[12];
    String[] items_day = new String[31];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        userItem = new UserItem();

        for(int i = 0; i < 80; i++) items_1[i] = 1930 + i;
        for(int i = 0; i < 12; i++) items_2[i] = 1 + i;
        for(int i = 0; i < 31; i++) items_3[i] = 1 + i;
        for(int i=0; i<80; i++) items_year[i] = Integer.toString(items_1[i]);
        for(int i=0; i<12; i++) items_month[i] = Integer.toString(items_2[i]);
        for(int i=0; i<31; i++) items_day[i] = Integer.toString(items_3[i]);

        backButton = findViewById(R.id.button_back);
        emailEdit = findViewById( R.id.edit_email );
        passwordEdit = findViewById( R.id.edit_password );
        pwckEdit = findViewById(R.id.edit_pwck);
        nameEdit = findViewById( R.id.edit_name );
        sextypeText = findViewById(R.id.text_sextype);
        birthText = findViewById(R.id.text_birth);
        registerButton = findViewById(R.id.button_register);
        agreeAllButton = findViewById(R.id.button_agree_all);
        agreeButton1 = findViewById(R.id.button_agree_1);
        agreeButton2 = findViewById(R.id.button_agree_2);
        agreeButton3 = findViewById(R.id.button_agree_3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailEdit.setTextColor(context.getColorStateList(R.color.editor_focus));
                if(validate){
                    validate = false;
                    checkButton.setSelected(false);
                }
                if (!emailEdit.getText().toString().equals("")){
                    checkButton.setActivated(true);
                }else {
                    checkButton.setSelected(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(EditorInfo.IME_ACTION_DONE == i) emailEdit.clearFocus();
                return false;
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!passwordEdit.getText().toString().equals(pwckEdit.getText().toString())){
                    pwckEdit.setTextColor(context.getColor(R.color.colorRed));
                    validate3 = false;
                } else{
                    pwckEdit.setTextColor(context.getColorStateList(R.color.editor_focus));
                    validate3 = true;
                }
                String pwPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
                Matcher matcher = Pattern.compile(pwPattern).matcher(passwordEdit.getText().toString());
                if(!matcher.matches()){
                    passwordEdit.setTextColor(context.getColor(R.color.colorRed));
                    validate2 = false;
                }else{
                    passwordEdit.setTextColor(context.getColorStateList(R.color.editor_focus));
                    validate2 = true;
                }

                validateForm();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(EditorInfo.IME_ACTION_DONE == i) emailEdit.clearFocus();
                return false;
            }
        });


        pwckEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!pwckEdit.getText().toString().equals(passwordEdit.getText().toString())){
                    pwckEdit.setTextColor(context.getColor(R.color.colorRed));
                    validate3 = false;
                } else{
                    pwckEdit.setTextColor(context.getColorStateList(R.color.editor_focus));
                    validate3 = true;
                }
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        pwckEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(EditorInfo.IME_ACTION_DONE == i) emailEdit.clearFocus();
                return false;
            }
        });

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(EditorInfo.IME_ACTION_DONE == i) emailEdit.clearFocus();
                return false;
            }
        });

        sextypeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("requestCode", REQUEST_CODE_1);
                intent.putExtra("itemList", items_sextype);
                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });
        birthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("requestCode", REQUEST_CODE_2);
                intent.putExtra("itemList1", items_year);
                intent.putExtra("itemList2", items_month);
                intent.putExtra("itemList3", items_day);
                startActivityForResult(intent, REQUEST_CODE_2);
            }
        });

        //아이디 중복 체크
        checkButton = findViewById(R.id.button_check);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserEmail = emailEdit.getText().toString();
                if (validate) {
                    return; //검증 완료
                }
                validateEmail(UserEmail);
            }
        });

        //약관 동의
        agreeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agreeAllButton.isSelected()) {
                    agreeAllButton.setSelected(false);
                    agreeButton1.setSelected(false);
                    agreeButton2.setSelected(false);
                    agreeButton3.setSelected(false);
                }
                else {
                    agreeAllButton.setSelected(true);
                    agreeButton1.setSelected(true);
                    agreeButton2.setSelected(true);
                    agreeButton3.setSelected(true);
                }
                validateForm();
            }
        });
        agreeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agreeButton1.isSelected()) {
                    agreeButton1.setSelected(false);
                    if (agreeAllButton.isSelected()) agreeAllButton.setSelected(false);
                }
                else agreeButton1.setSelected(true);
                validateForm();
            }
        });
        agreeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agreeButton2.isSelected()) {
                    agreeButton2.setSelected(false);
                    if (agreeAllButton.isSelected()) agreeAllButton.setSelected(false);
                }
                else agreeButton2.setSelected(true);
                validateForm();
            }
        });
        agreeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agreeButton3.isSelected()) {
                    agreeButton3.setSelected(false);
                    if (agreeAllButton.isSelected()) agreeAllButton.setSelected(false);
                }
                else agreeButton3.setSelected(true);
                validateForm();
            }
        });
    }

    private void validateForm(){

        if (validate && validate2 && validate3
                && !nameEdit.getText().toString().equals("")
                && !sextypeText.getText().toString().equals("") && !birthText.getText().toString().equals("")
                && agreeButton1.isSelected() && agreeButton2.isSelected()){
            registerButton.setBackgroundColor(context.getColor(R.color.colorAccent));
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userItem.email = emailEdit.getText().toString();
                    userItem.password = passwordEdit.getText().toString();
                    userItem.name = nameEdit.getText().toString();
                    userItem.sextype = sextypeText.getText().toString();
                    userItem.birthday = birthText.getText().toString().replace(" ", "");
                    registerUserInfo(userItem);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_1) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt = data.getExtras().getInt("resultInt");
            sextypeText.setText(items_sextype[resultInt]);
            validateForm();
        }else if (requestCode == REQUEST_CODE_2) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt1 = data.getExtras().getInt("resultInt1");
            int resultInt2 = data.getExtras().getInt("resultInt2");
            int resultInt3 = data.getExtras().getInt("resultInt3");

            String birthString = items_year[resultInt1] + items_month[resultInt2] + items_day[resultInt3];
            birthText.setText(birthString);
            validateForm();
        }
    }
    
    private void validateEmail(String email){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<UserItem> call = remoteService.selectUserInfo(email);
        call.enqueue(new Callback<UserItem>() {
            @Override
            public void onResponse(Call<UserItem> call, Response<UserItem> response) {
                UserItem item = response.body();

                if (response.isSuccessful() && !StringLib.getInstance().isBlank(item.name)) {
                    MyLog.d(TAG, "success " + response.body().toString());
                    emailEdit.setTextColor(context.getColor(R.color.colorRed));
                } else {
                    MyLog.d(TAG, "not success");
                    validate = true; //검증 완료
                    checkButton.setActivated(false);
                    checkButton.setSelected(true);
                    validateForm();
                }
            }

            @Override
            public void onFailure(Call<UserItem> call, Throwable t) {
                MyLog.d(TAG, "no internet connectivity");
                MyLog.d(TAG, t.toString());
            }
        });
    }

    private void registerUserInfo(UserItem item){
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
