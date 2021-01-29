package com.example.bestfood;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bestfood.custom.CustomView;
import com.example.bestfood.item.ImageItem;
import com.example.bestfood.lib.BitmapLib;
import com.example.bestfood.lib.FileLib;
import com.example.bestfood.lib.GoLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.lib.RemoteLib;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = this.getClass().getSimpleName();
    public static final String INFO_SEQ = "INFO_SEQ";

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    Activity context;
    int infoSeq;

    File imageFile;
    String imageFilename;

    //EditText imageMemoEdit;
    ImageView infoImage;
    CustomView pointImage;
    TextView descriptionText;
    ImageItem imageItem;

    Button prevButton;
    Button nextButton;
    Button completeButton;

    boolean isSavingImage = false;
    ArrayList position_list_X;
    ArrayList position_list_Y;
    int nextCount = 0;

    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        infoSeq = getIntent().getIntExtra(INFO_SEQ, 0);
        context = this;

        //테스트용으로 seq 지정
        infoSeq = 1;
        //테스트용으로 seq 지정

        position_list_X = new ArrayList();
        position_list_Y = new ArrayList();

        imageItem = new ImageItem();
        imageItem.infoSeq = infoSeq;

        imageFilename = infoSeq + "_" + String.valueOf(System.currentTimeMillis());
        imageFile = FileLib.getInstance().getImageFile(context, imageFilename);

        infoImage = (ImageView)findViewById(R.id.bestfood_image);
        pointImage = (CustomView)findViewById(R.id.bestfood_image_2);
        descriptionText = (TextView)findViewById(R.id.image_description);
        descriptionText.setText("사진을 업로드하고 위치를 표시해 주세요");
        //imageMemoEdit = (EditText)findViewById(R.id.register_image_memo);

        ImageView imageRegister = (ImageView)findViewById(R.id.bestfood_image_register);
        imageRegister.setOnClickListener(this);

        prevButton = (Button)findViewById(R.id.prev);
        nextButton = (Button)findViewById(R.id.next);
        completeButton = (Button)findViewById(R.id.complete);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        completeButton.setOnClickListener(this);
        completeButton.setVisibility(View.GONE);

        pointImage.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //scrollView.setVisibility(View.INVISIBLE);
                //scrollView2.setVisibility(View.VISIBLE);
                //pointImage.X = (int) motionEvent.getX();
                //pointImage.Y = (int) motionEvent.getY();
                position_list_X.add((int) motionEvent.getX());
                position_list_Y.add((int) motionEvent.getY());
                pointImage.position_list_X = position_list_X;
                pointImage.position_list_Y = position_list_Y;
                pointImage.invalidate();
                //pointImage.touched = true;
                return false;
            }
        });
    }

    /**
     * 이미지를 촬영하고 그 결과를 받을 수 있는 액티비티를 시작한다.
     */
    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        context.startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /**
     * 앨범으로부터 이미지를 선택할 수 있는 액티비티를 시작한다.
     */
    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        context.startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * 클릭이벤트를 처리한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bestfood_image_register) {
            showImageDialog(context);
        } else if (v.getId() == R.id.prev){
            pointImage.position_list_X.remove(pointImage.position_list_X.size()-1);
            pointImage.position_list_Y.remove(pointImage.position_list_Y.size()-1);
            pointImage.invalidate();
        } else if (v.getId() == R.id.next) {
            saveImage();
            infoImage.setImageResource(0);
            descriptionText.setText("상세 사진을 찍어 주세요");
            nextCount += 1;
            if (nextCount == 1){
                String dot = "";
                for (int i=0; i<position_list_X.size(); i++){
                    if (i>0) dot = dot + "/ ";
                    dot = dot + position_list_X.get(i) + ", " + position_list_Y.get(i);
                }
                RemoteLib.getInstance().uploadCaseDot(infoSeq, dot);
            }
            if (nextCount >= 1) {
                pointImage.setVisibility(View.GONE);
                prevButton.setVisibility(View.GONE);
            }
            if (nextCount >= position_list_X.size()) completeButton.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.complete){
            saveImage();
            context.finish();
        }
    }

    /**
     * 다른 액티비티를 실행한 결과를 처리하는 메소드
     * @param requestCode 액티비티를 실행하면서 전달한 요청 코드
     * @param resultCode 실행한 액티비티가 설정한 결과 코드
     * @param data 결과 데이터
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FROM_CAMERA) {
                Picasso.get().load(imageFile).into(infoImage);

            } else if (requestCode == PICK_FROM_ALBUM && data != null) {
                Uri dataUri = data.getData();
                MyLog.d("here3" + dataUri);

                if (dataUri != null) {
                    Picasso.get().load(dataUri).into(infoImage);
                    /*
                    InputStream in = null;
                    try {
                        in = getActivity().getContentResolver().openInputStream(dataUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap image = BitmapFactory.decodeStream(in);
                    BitmapLib.getInstance().saveBitmapToFileThread(imageUploadHandler,
                            imageFile, image);
                    isSavingImage = true;
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    */
                    /*
                    final ImageView profile = new ImageView(context);
                    Picasso.with(context).load(dataUri).into(profile, new Callback() {
                        @Override
                        public void onSuccess() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {//You will get your bitmap here

                                    Bitmap innerBitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
                                    MyLog.d("here1");
                                    BitmapLib.getInstance().saveBitmapToFileThread(imageUploadHandler,
                                            imageFile, innerBitmap);
                                    MyLog.d("here2");
                                    isSavingImage = true;

                                }
                            }, 100);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    */
                    /*
                    final Target target = new Target(){
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            MyLog.d("here1");
                            BitmapLib.getInstance().saveBitmapToFileThread(imageUploadHandler,
                                    imageFile, bitmap);
                            MyLog.d("here2");
                            isSavingImage = true;
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    };
                    infoImage.setTag(target);

                     */

                    Picasso.get().load(dataUri).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            MyLog.d("here1");
                            BitmapLib.getInstance().saveBitmapToFileThread(imageUploadHandler,
                                    imageFile, bitmap);
                            MyLog.d("here2");
                            isSavingImage = true;
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            MyLog.d("here5");
                        }
                    });

                    /*
                    Picasso.with(context).load(dataUri).transform(new Transformation() {

                        @Override
                        public Bitmap transform(Bitmap bitmap) {
                            MyLog.d("here1");
                            BitmapLib.getInstance().saveBitmapToFileThread(imageUploadHandler,
                                    imageFile, bitmap);
                            isSavingImage = true;
                            return bitmap;
                        }
                        @Override
                        public String key() {
                            return "";
                        }
                    });
                    */
                }
            }
        }
    }

    /**
     * 사용자가 선택한 이미지와 입력한 메모를 ImageItem 객체에 저장한다.
     */
    private  void setImageItem() {
        /*
        String imageMemo = imageMemoEdit.getText().toString();
        if (StringLib.getInstance().isBlank(imageMemo)) {
            imageMemo = "";
        }

        imageItem.imageMemo = imageMemo;
         */
        if (nextCount == 0) imageItem.label = "main";
        else imageItem.label = "detail";
        imageItem.filename = imageFilename + ".png";
    }

    /**
     * 이미지를 서버에 업로드한다.
     */
    private void saveImage() {
        if (isSavingImage) {
            MyToast.s(context, R.string.no_image_ready);
            return;
        }
        MyLog.d(TAG, "imageFile.length() " + imageFile.length());

        if (imageFile.length() == 0) {
            MyToast.s(context, R.string.no_image_selected);
            return;
        }

        setImageItem();

        RemoteLib.getInstance().uploadCaseImage(infoSeq,
                imageItem.label, imageFile, finishHandler);
        isSavingImage = false;
    }

    /**
     * 이미지를 어떤 방식으로 선택할지에 대해 다이얼로그를 보여준다.
     * @param context 컨텍스트 객체
     */
    public void showImageDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_bestfood_image_register)
                .setSingleChoiceItems(R.array.camera_album_category, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    getImageFromCamera();
                                } else {
                                    getImageFromAlbum();
                                }

                                dialog.dismiss();
                            }
                        }).show();
    }

    Handler imageUploadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isSavingImage = false;
            setImageItem();
            Picasso.get().invalidate(RemoteService.IMAGE_URL + imageItem.filename);
        }
    };
    Handler finishHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //context.finish();
        }
    };

}
