package com.example.bestfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestfood.adapter.ChatMessageAdapter;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.ChatItem;
import com.example.bestfood.lib.BitmapLib;
import com.example.bestfood.lib.FileLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.RemoteLib;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bestfood.CaseActivity.INFO_SEQ;

public class ChatActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public static final String REPAIRER_SEQ = "REPAIRER_SEQ";

    public static ArrayList<ChatItem> rMessageList = new ArrayList<ChatItem>();

    Context context;

    int currentUserSeq;
    public static int currentRepairerSeq;
    int sendPosition;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    ImageButton back;
    RecyclerView message_list;
    RecyclerView.LayoutManager layoutManager;
    ChatMessageAdapter chatMessageAdapter;
    ImageButton btUpload;
    TextInputEditText input_message;
    ImageButton btSend;

    File ImageMessageFile = null;
    String ImageMessageFilename = null;

    String strmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = this;

        currentUserSeq = ((App)getApplication()).getUserSeq();
        currentRepairerSeq = getIntent().getIntExtra(REPAIRER_SEQ, 0);

        //rMessageList = new ArrayList<ChatItem>();
        //strRepairerName = (TextView) findViewById(R.id.show_repairer_name);
        back = (ImageButton) findViewById(R.id.button_back);
        message_list = (RecyclerView) findViewById(R.id.message_list);
        btUpload = (ImageButton) findViewById(R.id.upload);
        input_message = (TextInputEditText) findViewById(R.id.input_message);
        btSend = (ImageButton) findViewById(R.id.Send);

        btSend.setEnabled(false);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //strRepairerName.setText("이름");
        input_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0) {
                    btSend.setEnabled(false);
                } else {
                    btSend.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getList();
        getChatItem();

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override //이미지 불러오기(갤러리 접근)
            public void onClick(View v) {
                sendPosition = 3;
                Toast.makeText(getApplicationContext(), "사진은 한 장씩 전송 가능합니다.", Toast.LENGTH_LONG).show();
                getImageFromAlbum();
                //getImageFromCamera();
            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //rMessageList.add(getChatItem());
                sendPosition = 1;
                strmessage = input_message.getText().toString();
                ArrayList<ChatItem> newList = new ArrayList<ChatItem>();
                newList.add(getChatItem());
                MyLog.d(TAG, "here newlist " + newList.toString());
                chatMessageAdapter.addItemList(newList);
                insertChatInfo(getChatItem());
                message_list.scrollToPosition(rMessageList.size());
                input_message.setText(null);
            }
        });
    }

    private void getList(){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ArrayList<ChatItem>> call = remoteService.selectChatInfo(currentUserSeq, currentRepairerSeq);

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
        layoutManager = new LinearLayoutManager(this);
        message_list.setLayoutManager(layoutManager);

        chatMessageAdapter = new ChatMessageAdapter(this, new ArrayList<ChatItem>());
        message_list.setAdapter(chatMessageAdapter);
        chatMessageAdapter.addItemList(rMessageList);
        message_list.scrollToPosition(rMessageList.size()-1);
    }

    private ChatItem getChatItem() {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd/hh/mm/ss");

        ChatItem item = new ChatItem();
        item.userSeq = currentUserSeq;
        item.repairerSeq = currentRepairerSeq;
        item.sending = sendPosition;
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

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(ImageMessageFile));
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /**
     * 카메라 앨범앱을 실행해서 이미지를 선택한다.
     */
    private void getImageFromAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * startActivityForResult() 메소드로 호출한 액티비티의 결과를 처리한다.
     * @param requestCode 액티비티를 실행하면서 전달한 요청 코드
     * @param resultCode 실행한 액티비티가 설정한 결과 코드
     * @param intent 결과 데이터
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        MyLog.d(TAG, "onActivityResult " + intent);

        if (resultCode != RESULT_OK) return;

        if (requestCode == PICK_FROM_CAMERA) {
            ArrayList<ChatItem> newList = new ArrayList<ChatItem>();
            newList.add(getChatItem());
            MyLog.d(TAG, "here newlist " + newList.toString());
            chatMessageAdapter.addItemList(newList);
            insertChatInfo(getChatItem());
            message_list.scrollToPosition(rMessageList.size());
            ImageMessageFile = null;
            ImageMessageFilename = null;

        } else if (requestCode == PICK_FROM_ALBUM) {
            Uri dataUri = intent.getData();
            MyLog.d("here3" + dataUri);

            if (intent.getDataString() != null) {
                //Picasso.get().load(dataUri).into(infoImage);
                ImageMessageFilename = currentUserSeq + "_" + String.valueOf(System.currentTimeMillis());
                Picasso.get().load(dataUri).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        MyLog.d("here1");
                        //ImageMessageFilename = currentUserSeq + "_" + String.valueOf(System.currentTimeMillis());
                        ImageMessageFile = FileLib.getInstance().getImageFile(context, ImageMessageFilename);
                        MyLog.d("here imagefile" + ImageMessageFile.length());
                        BitmapLib.getInstance().saveBitmapToFileThread(imageUploadHandler,
                                ImageMessageFile, bitmap);
                        MyLog.d("here2");
                        //isSavingImage = true;
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        MyLog.d("here5");
                    }
                });
            }
            Log.d("checking image file name: ", ImageMessageFilename);
            strmessage = ImageMessageFilename;
            ArrayList<ChatItem> newList = new ArrayList<ChatItem>();
            newList.add(getChatItem());
            MyLog.d(TAG, "here newlist " + newList.toString());
            chatMessageAdapter.addItemList(newList);
            insertChatInfo(getChatItem());
            message_list.scrollToPosition(rMessageList.size());
        }
    }

    Handler imageUploadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //isSavingImage = false;
            //setImageItem();
            //Picasso.get().invalidate(RemoteService.IMAGE_URL + imageItem.filename);
            MyLog.d("here imagefile" + ImageMessageFile.length());
            uploadChatImage(currentUserSeq, ImageMessageFile);
        }
    };

    //http://blog.naver.com/PostView.nhn?blogId=jjjjokk&logNo=220743286618

    public void uploadChatImage(int chatSeq, File file) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("multipart/form-data"));

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody chatSeqBody =
                RequestBody.create(
                        "" + chatSeq, MediaType.parse("multipart/form-data"));

        Call<ResponseBody> call =
                remoteService.uploadChatImage(chatSeqBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                MyLog.d(TAG, "uploadChatImage success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadChatImage fail");
            }
        });
    }
}