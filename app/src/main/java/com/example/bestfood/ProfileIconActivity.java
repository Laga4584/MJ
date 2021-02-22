package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.FileLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 프로필 아이콘을 등록하는 액티비티
 */
public class ProfileIconActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int CROP_FROM_ALBUM = 3;

    Context context;
    UserItem userItem;

    ImageButton backButton;
    TextView finishButton;
    ImageView profileIcon;

    File profileIconFile;
    String profileIconFilename;

    /**
     * 액티비티를 생성하고 화면을 구성한다.
     * @param savedInstanceState 액티비티가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_icon);

        context = this;

        userItem = ((App) getApplication()).getUserItem();

        setView();
        setProfileIcon();
    }

    /**
     * 액티비티 화면을 설정한다.
     */
    public void setView() {
        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(this);
        finishButton = findViewById(R.id.button_finish);
        finishButton.setOnClickListener(this);

        profileIcon = findViewById(R.id.icon_profile);

        TextView albumButton = findViewById(R.id.button_album);
        albumButton.setOnClickListener(this);

        TextView cameraButton = findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(this);
    }

    /**
     * 프로필 아이콘을 설정한다.
     */
    private void setProfileIcon() {
        MyLog.d(TAG, "onResume " +
                RemoteService.USER_ICON_URL + userItem.userIconFilename);

        if (StringLib.getInstance().isBlank(userItem.userIconFilename)) {
            Picasso.get().load(R.drawable.ic_person).into(profileIcon);
        } else {
            Picasso.get()
                    .load(RemoteService.USER_ICON_URL + userItem.userIconFilename)
                    .into(profileIcon);
        }
    }

    /**
     * 사용자가 선택한 프로필 아이콘을 저장할 파일 이름을 설정한다.
     */
    private void setProfileIconFile() {
        profileIconFilename = userItem.seq + "_" + String.valueOf(System.currentTimeMillis());

        profileIconFile = FileLib.getInstance().getProfileIconFile(context, profileIconFilename);
    }

    /**
     * 프로필 아이콘을 설정하기 위해 선택할 수 있는 앨범이나 카메라 버튼의 클릭 이벤트를 처리한다.
     * @param v 클릭한 뷰 객체
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            finish();

        } else if (v.getId() == R.id.button_finish) {
            uploadProfileIcon();

        } else if (v.getId() == R.id.button_album) {
            setProfileIconFile();
            getImageFromAlbum();

        } else if (v.getId() == R.id.button_camera) {
            setProfileIconFile();
            getImageFromCamera();
        }
    }

    /**
     * 카메라 앱을 실행해서 이미지를 촬영한다.
     */
    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profileIconFile));
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
     * 이미지를 자르기 위한 Intent를 생성해서 반환한다.
     * @param inputUri 이미지를 자르기전 Uri
     * @param outputUri 이미지를 자른 결과 파일 Uri
     * @return 이미지를 자르기 위한 인텐트
     */
    private Intent getCropIntent(Uri inputUri, Uri outputUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

        return intent;
    }

    /**
     * 카메라에서 촬영한 이미지를 프로필 아이콘에 사용할 크기로 자른다.
     */
    private void cropImageFromCamera() {
        Uri uri = Uri.fromFile(profileIconFile);
        Intent intent = getCropIntent(uri, uri);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    /**
     * 카메라 앨범에서 선택한 이미지를 프로필 아이콘에 사용할 크기로 자른다.
     */
    private void cropImageFromAlbum(Uri inputUri) {
        Uri outputUri = Uri.fromFile(profileIconFile);

        MyLog.d(TAG, "startPickFromAlbum uri " + inputUri.toString());
        Intent intent = getCropIntent(inputUri, outputUri);
        startActivityForResult(intent, CROP_FROM_ALBUM);
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
            cropImageFromCamera();

        } else if (requestCode == CROP_FROM_CAMERA) {
            Picasso.get().load(profileIconFile).into(profileIcon);

        } else if (requestCode == PICK_FROM_ALBUM && intent != null) {
            Uri dataUri = intent.getData();
            if (dataUri != null) {
                cropImageFromAlbum(dataUri);
            }
        } else if (requestCode == CROP_FROM_ALBUM && intent != null) {
            Picasso.get().load(profileIconFile).into(profileIcon);
        }
    }

    /**
     * 프로필 아이콘을 서버에 업로드한다.
     */
    private void uploadProfileIcon() {
        uploadUserIcon(userItem.seq, profileIconFile);

        userItem.userIconFilename = profileIconFilename + ".png";
    }

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
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.e(TAG, "uploadUserIcon fail");
            }
        });
    }
}