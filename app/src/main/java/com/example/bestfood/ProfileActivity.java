package com.example.bestfood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 프로필을 설정할 수 있는 액티비티
 */
public class ProfileActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public static final int REQUEST_CODE_1 = 100;
    public static final int REQUEST_CODE_2 = 200;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    Context context;
    ImageButton back;
    TextView option2, option3, option5;
    EditText edit1, edit4, edit6;

    UserItem currentItem;

    String[] items_sextype = {"남성", "여성"};
    int[] items_1 = new int[80];
    int[] items_2 = new int[12];
    int[] items_3 = new int[31];
    String[] items_year = new String[80];
    String[] items_month = new String[12];
    String[] items_day = new String[31];

    /**
     * 액티비티를 생성하고 화면을 구성한다.
     * @param savedInstanceState 액티비티가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = this;
        currentItem = ((App) getApplication()).getUserItem();

        for(int i = 0; i < 80; i++) items_1[i] = 1930 + i;
        for(int i = 0; i < 12; i++) items_2[i] = 1 + i;
        for(int i = 0; i < 31; i++) items_3[i] = 1 + i;
        for(int i=0; i<80; i++) items_year[i] = Integer.toString(items_1[i]);
        for(int i=0; i<12; i++) items_month[i] = Integer.toString(items_2[i]);
        for(int i=0; i<31; i++) items_day[i] = Integer.toString(items_3[i]);

        back = findViewById(R.id.button_back);

        edit1 = findViewById(R.id.edit1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        edit4 = findViewById(R.id.edit4);
        option5 = findViewById(R.id.option5);
        edit6 = findViewById(R.id.edit6);

        edit1.setText(currentItem.name);
        option2.setText(currentItem.sextype);
        option3.setText(currentItem.birthday);
        edit4.setText(currentItem.phone);
        option5.setText(currentItem.address);
        edit6.setText(currentItem.addressDetail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edit1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_DONE == i) {
                    currentItem.name = edit1.getText().toString();
                    edit1.clearFocus();
                    updateUserInfo(currentItem);
                }
                return false;
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DialogActivity.class);
                intent.putExtra("requestCode", REQUEST_CODE_1);
                intent.putExtra("itemList", items_sextype);
                startActivityForResult(intent, REQUEST_CODE_1);
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
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
        edit4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_DONE == i) {
                    currentItem.phone = edit4.getText().toString();
                    edit4.clearFocus();
                    updateUserInfo(currentItem);
                }
                return false;
            }
        });
        option5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddressActivity.class);
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
            }
        });
        edit6.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_DONE == i) {
                    currentItem.addressDetail = edit6.getText().toString();
                    edit6.clearFocus();
                    updateUserInfo(currentItem);
                }
                return false;
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_1) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt = data.getExtras().getInt("resultInt");
            currentItem.sextype = items_sextype[resultInt];
            option2.setText(items_sextype[resultInt]);
            updateUserInfo(currentItem);
        }else if (requestCode == REQUEST_CODE_2) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            int resultInt1 = data.getExtras().getInt("resultInt1");
            int resultInt2 = data.getExtras().getInt("resultInt2");
            int resultInt3 = data.getExtras().getInt("resultInt3");

            String birthString = items_year[resultInt1] + items_month[resultInt2] + items_day[resultInt3];
            currentItem.birthday = birthString;
            option3.setText(birthString);
            updateUserInfo(currentItem);
        }else if (requestCode == SEARCH_ADDRESS_ACTIVITY){
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            String resultString = data.getExtras().getString("data");
            currentItem.address = resultString;
            option5.setText(resultString);
            updateUserInfo(currentItem);
        }
    }

    private void updateUserInfo(final UserItem userItem) {
        RemoteService remoteService =
                ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.updateUserInfo(userItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    MyToast.s(context, "프로필이 수정되었습니다");
                    ((App) getApplicationContext()).setUserItem(userItem);
                }else{
                    MyToast.s(context, "프로필 업데이트에 실패했습니다");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyToast.s(context, "프로필 업데이트에 실패했습니다");
            }
        });
    }
}