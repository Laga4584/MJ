package com.example.bestfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bestfood.adapter.ChatMessageAdapter;
import com.example.bestfood.item.ChatItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    public static ArrayList<ChatItem> rMessageList = new ArrayList<ChatItem>();

    int currentMemberSeq;
    int currentRepairerSeq;

    TextView strRepairerName;
    RecyclerView message_list;
    RecyclerView.LayoutManager layoutManager;
    ChatMessageAdapter chatMessageAdapter;
    EditText input_message;
    Button btSend;

    String strmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //rMessageList = new ArrayList<ChatItem>();
        strRepairerName = (TextView) findViewById(R.id.show_repairer_name);
        message_list = (RecyclerView) findViewById(R.id.message_list);
        input_message = (EditText) findViewById(R.id.input_message);
        btSend = (Button) findViewById(R.id.Send);

        strRepairerName.setText("이름");
        strmessage = input_message.getText().toString();

        currentMemberSeq = 1;
        currentRepairerSeq = 1;


        getList();

        getChatItem();


        btSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (input_message.getText().toString().trim().length() < 1) {
                    rMessageList.add(getChatItem());
                    insertChatInfo(getChatItem());
                } else {
                    input_message.setText(null);
                }
            }
        });

    }

    private void getList(){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<ChatItem>> call = remoteService.selectChatInfo(currentMemberSeq, currentRepairerSeq);

        call.enqueue(new Callback<ArrayList<ChatItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ChatItem>> call, Response<ArrayList<ChatItem>> response)
            {
                ArrayList<ChatItem> list = response.body();

                if (response.isSuccessful()) {
                    MyLog.d(TAG, "here item " + list.toString());
                    rMessageList = list;
                    setAdapter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ChatItem>> call, Throwable t) {
                MyLog.d(TAG, "Fail to get " + t.toString());
            }
        });
    }

    private void setAdapter(){
        MyLog.d(TAG, "here item " + rMessageList.toString());
        layoutManager = new LinearLayoutManager(this);
        message_list.setLayoutManager(layoutManager);

        chatMessageAdapter = new ChatMessageAdapter(this, new ArrayList<ChatItem>());
        message_list.setAdapter(chatMessageAdapter);
        chatMessageAdapter.addItemList(rMessageList);
    }

    private ChatItem getChatItem() {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd/hh/mm/ss");

        ChatItem item = new ChatItem();
        item.userSeq = 1;
        item.repairerSeq = 1;
        item.sending = 1;
        item.message = strmessage;
        item.regDate = simpleDate.format(mDate);

        return item;
    }

    private void insertChatInfo(final ChatItem chatItem) {
        MyLog.d(TAG, chatItem.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertChatInfo(chatItem);
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

                    } else {
                        chatItem.seq = seq;
                    }
                } else {
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
}
