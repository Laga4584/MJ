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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bestfood.custom.CustomView;
import com.example.bestfood.item.CaptureItem;
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

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class CameraDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = this.getClass().getSimpleName();
    public static final String CAPTURE_ITEM = "CAPTURE_ITEM";

    Activity context;
    int count;
    CaptureItem captureItem;

    ImageView infoImage;
    CustomView pointImage;
    TextView description;
    EditText imageDescription;

    ImageButton backButton, prevButton;
    TextView doneButton, sequence;
    LinearLayout description2;
    FrameLayout textbox;

    ArrayList<Integer> position_list_X;
    ArrayList<Integer> position_list_Y;


    @SuppressLint("ClickableViewAccessibility")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_detail);

        count = getIntent().getIntExtra("count", 0);
        //captureItem = Parcels.unwrap(getIntent().getParcelableExtra("CAPTURE_ITEM"));
        captureItem = CameraActivity.captureItemList.get(count);
        
        context = this;

        position_list_X = new ArrayList<Integer>();
        position_list_Y = new ArrayList<Integer>();

        infoImage = (ImageView)findViewById(R.id.image);
        pointImage = (CustomView)findViewById(R.id.dot);
        description = (TextView)findViewById(R.id.description);
        imageDescription = findViewById(R.id.image_description);

        backButton = findViewById(R.id.button_back);
        doneButton = findViewById(R.id.button_done);
        prevButton = findViewById(R.id.button_prev);

        description2 = findViewById(R.id.description2);
        textbox = findViewById(R.id.textbox);
        sequence = findViewById(R.id.sequence);

        if (count == 0) {
            textbox.setVisibility(View.GONE);
            sequence.setVisibility(View.GONE);
        } else {
            description2.setVisibility(View.GONE);
            description.setText("정확한 위치에 도트 스티커를 붙이고 설명해주세요 (100자 이하)");
            //String sequenceText = Integer.toString(count);
            sequence.setText(String.valueOf(count));
        }

        backButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

        infoImage.setImageBitmap(captureItem.bitmap);

        pointImage.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //scrollView.setVisibility(View.INVISIBLE);
                //scrollView2.setVisibility(View.VISIBLE);
                //pointImage.X = (int) motionEvent.getX();
                //pointImage.Y = (int) motionEvent.getY();
                if(count == 0 || position_list_X.size() < 1) {
                    MyLog.d("here position X " + motionEvent.getX());
                    MyLog.d("here position Y " + motionEvent.getY());
                    position_list_X.add((int) motionEvent.getX());
                    position_list_Y.add((int) motionEvent.getY());
                    pointImage.position_list_X = position_list_X;
                    pointImage.position_list_Y = position_list_Y;
                    pointImage.invalidate();
                    //pointImage.touched = true;
                }
                return false;
            }
        });

        if (captureItem.positionX != null){
            pointImage.position_list_X = captureItem.positionX;
            pointImage.position_list_Y = captureItem.positionY;
            position_list_X = captureItem.positionX;
            position_list_Y = captureItem.positionY;
            pointImage.invalidate();
        }
    }

    /**
     * 클릭이벤트를 처리한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_back){
            finish();
        } else if (v.getId() == R.id.button_prev) {
            position_list_X.remove(position_list_X.size()-1);
            position_list_Y.remove(position_list_Y.size()-1);
            pointImage.position_list_X = position_list_X;
            pointImage.position_list_Y = position_list_Y;
            pointImage.invalidate();
        } else if (v.getId() == R.id.button_done){
            captureItem.positionX = position_list_X;
            captureItem.positionY = position_list_Y;
            captureItem.description = imageDescription.getText().toString();
            if (count == 0){
                for (int i=0; i<captureItem.positionX.size(); i++) {
                    CameraActivity.captureItemList.add(new CaptureItem());
                }
            }
            CameraActivity.captureItemList.set(count, captureItem);
            Intent intentR = new Intent();
            //intentR.putExtra("CAPTURE_ITEM", Parcels.wrap(captureItem)); //사용자에게 입력받은값 넣기
            setResult(Activity.RESULT_OK, intentR);
            finish();
        }
    }

}
