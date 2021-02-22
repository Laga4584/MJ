package com.example.bestfood;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bestfood.adapter.CaptureListAdapter;
import com.example.bestfood.custom.EndlessRecyclerViewScrollListener;
import com.example.bestfood.item.CaptureItem;
import com.example.bestfood.lib.BitmapLib;
import com.example.bestfood.lib.FileLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.RemoteLib;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraActivity extends AppCompatActivity {
    public static final int REQUEST_CAMERA_CODE = 100;
    public static final int REQUEST_CODE = 0;
    public static final String CAPTURE_ITEM = "CAPTURE_ITEM";
    public static final String PACKAGE = "package:";

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    ///To make the photo appear vertically
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    Context context;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ImageView iv_show;
    private ImageButton captureButton, flashButton, exitButton;
    private CameraManager mCameraManager;//Camera Manager
    private Handler childHandler, mainHandler;
    private String mCameraID;//Camera Id 0 is the last 1 is the former
    String cameraId;
    private ImageReader mImageReader;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraDevice mCameraDevice;
    int rotation;
    CaptureRequest.Builder previewRequestBuilder;
    boolean flashOn = false;

    RecyclerView captureList;
    CaptureListAdapter infoListAdapter;
    LinearLayoutManager layoutManager;
    int listTypeValue = 1;
    public static ArrayList<CaptureItem> captureItemList;

    LinearLayout rectangles;
    TextView description, complete;
    int count = 0;
    public static CaptureItem captureItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = this;
        captureItemList = new ArrayList<CaptureItem>();
        //captureItemList = CaseFragment1.captureItemList;
        try {
            initVIew();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        initListener();

        rectangles = findViewById(R.id.rectangles);
        description = findViewById(R.id.description);

        //편집하기 일 경우
        if (captureItemList.size() > 0){

        }else {
            captureItemList.add(new CaptureItem());
        }

    }
    /**
     * Camera creation monitor
     */
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {//Open the camera
            mCameraDevice = camera;
            //Open preview
            takePreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {//Close the camera
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {//An error occurred
            Toast.makeText(context, "camera failed to open", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * Initialization
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initVIew() throws CameraAccessException {

        captureList = findViewById(R.id.list);
        setRecyclerView();
        ArrayList<CaptureItem> list = new ArrayList<CaptureItem>();
        CaptureItem item = new CaptureItem();
        list.add(item);
        infoListAdapter.addItemList(list);

        //iv_show = (ImageView) findViewById(R.id.image);
        //mSurfaceView
        mSurfaceView = (SurfaceView) findViewById(R.id.previewFrame);
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        cameraId = mCameraManager.getCameraIdList()[0];
        setLayoutWH();
        captureButton = findViewById(R.id.button);
        flashButton = findViewById(R.id.button_flash);
        exitButton = findViewById(R.id.button_exit);
        complete = findViewById(R.id.complete);
        complete.setVisibility(View.GONE);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);
        // mSurfaceView adds a callback
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) { //SurfaceView creation
                // Initialize Camera
                initCamera2();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView destroyed
                // Release Camera resources
                if (null != mCameraDevice) {
                    mCameraDevice.close();
                    mCameraDevice = null;
                }
            }
        });


        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.d("here flash " + flashOn);
                if (!flashOn) {
                    previewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                    previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                    flashOn = true;
                } else {
                    previewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                    previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                    flashOn = false;
                }

                try {
                    mCameraCaptureSession.setRepeatingRequest(previewRequestBuilder.build(), null, childHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * Initialize Camera2
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initCamera2() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;//Back camera
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() { //You can process the temporary photo taken here, for example, write local
            @Override
            public void onImageAvailable(ImageReader reader) {

                //mCameraDevice.close();
                // Get photo data
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);//Save the byte array from the buffer
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    Matrix matrix = new Matrix();
                    MyLog.d("here rotation 2 " + rotation);
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    //iv_show.setImageBitmap(rotatedBitmap);
                    CaptureItem newItem = new CaptureItem();
                    newItem.bitmap = rotatedBitmap;
                    captureItemList.set(count, newItem);
                    Intent intent = new Intent(context, CameraDetailActivity.class);
                    intent.putExtra("count", count);
                    startActivityForResult(intent, count);

                }
                image.close();
            }
        }, mainHandler);
                 // Get camera management

        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //Request WRITE_EXTERNAL_STORAGE permission
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_CODE);
                //return;
            } else {
                                 // Open the camera
                //mCameraManager.setTorchMode(cameraId, true);
                mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == count) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            //CaptureItem sendItem = Parcels.unwrap(data.getParcelableExtra(CAPTURE_ITEM));

            infoListAdapter.clearItemList();
            infoListAdapter.addItemList(captureItemList);
            count += 1;
            if (count != 0) {
                rectangles.setVisibility(View.GONE);
                description.setText("자, 다음은 수선이 필요한 부위를 좀 더 자세히 촬영해주세요");
            }
            if (count == captureItemList.size()-1){
                complete.setVisibility(View.VISIBLE);
                complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CaseFragment1.captureItemList = captureItemList;
                        Intent intentR = new Intent();
                        setResult(Activity.RESULT_OK, intentR);
                        finish();
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                try {
                    mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            } else {
                // Permission Denied
            }
        }
    }

    /**
     * Start preview
     */
    private void takePreview() {
        try {
            // Create the CaptureRequest.Builder needed for the preview
            previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                         // Surface of the SurfaceView as the target of CaptureRequest.Builder
            previewRequestBuilder.addTarget(mSurfaceHolder.getSurface());
            // Create a CameraCaptureSession that is responsible for managing the processing of preview requests and photo requests
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() // ③
            {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (null == mCameraDevice) return;
                    // When the camera is ready, start displaying the preview
                    mCameraCaptureSession = cameraCaptureSession;
                    try {
                        // auto focus
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        // turn on the flash
                        previewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                        //previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        // show preview
                        CaptureRequest previewRequest = previewRequestBuilder.build();
                        mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(context, "Configuration failed", Toast.LENGTH_SHORT).show();
                }
            }, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * taking pictures
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takePicture() {
        if (mCameraDevice == null) return;
        // Create a CaptureRequest.Builder for taking photos
        final CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                         // The imageReader surface as the target of CaptureRequest.Builder
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // auto focus
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // automatic exposure
            //captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);

            // Get the phone direction
            rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            MyLog.d("here rotation 1 " + rotation);
            // Calculate the direction of the photo based on the device orientation
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            //photograph
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mCameraCaptureSession.capture(mCaptureRequest, null, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }
    private void setLayoutWH() throws CameraAccessException {
        /*
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int mScreenWidth = displayMetrics.widthPixels;
        int mScreenHeight = displayMetrics.heightPixels;
        int width = mSurfaceView.getLayoutParams().width;
        int height = mSurfaceView.getLayoutParams().height;
        int optimalSize[] = getOptimalDimensions(mScreenWidth, mScreenHeight, width, height);

        best_size= mCameraManager.getCameraCharacteristics(mCameraID).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        ViewGroup.LayoutParams params =  (ViewGroup.MarginLayoutParams) mSurfaceView.getLayoutParams();
        //params.width = optimalSize[0];
        //params.height = optimalSize[1];
        params.width = 300;
        params.height = 300;
        mSurfaceView.setLayoutParams(params);

         */

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);


        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        MyLog.d("here map" + map);
        //Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
        //String sizeString = largest_1.getHeight() + "*" + largest_1.getWidth();
        Size largest = Size.parseSize("3024*3024");
        MyLog.d("here largest" + largest);
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        int rotatedPreviewWidth = metrics.densityDpi*300/160;
        int rotatedPreviewHeight = metrics.densityDpi*500/160;
        int maxPreviewWidth = displaySize.x;
        int maxPreviewHeight = displaySize.y;
        MyLog.d("here rotatedPreviewWidth" + rotatedPreviewWidth);
        MyLog.d("here rotatedPreviewHeight" + rotatedPreviewHeight);
        MyLog.d("here maxPreviewWidth" + maxPreviewWidth);
        MyLog.d("here maxPreviewHeight" + maxPreviewHeight);

        MyLog.d("here map.getOutputsizes" + Arrays.toString(map.getOutputSizes(SurfaceTexture.class)));
        Size mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                maxPreviewHeight, largest);

        MyLog.d("here mPreviewSize" + mPreviewSize);

        LinearLayout.LayoutParams params =  (LinearLayout.LayoutParams) mSurfaceView.getLayoutParams();
        MyLog.d("here params " + params.width);
        MyLog.d("here params " + params.height);
        //params.width = mPreviewSize.getWidth();
        //params.height = mPreviewSize.getHeight();
        //params.width = 1024;
        //params.height = 1024;
        //mSurfaceView.setLayoutParams(params);
        //mSurfaceView.requestLayout();
        //params =  (LinearLayout.LayoutParams) mSurfaceView.getLayoutParams();
        //MyLog.d("here params " + params.width);
        //MyLog.d("here params " + params.height);


        // 화면 방향에 따라 뷰를 그릴때, 가로 세로의 크기를 정한다.
        int orientation = getResources().getConfiguration().orientation;
        MyLog.d("here orientation " + orientation);
        MyLog.d("here orientation2 " +  Configuration.ORIENTATION_LANDSCAPE);

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            params.width = mPreviewSize.getWidth();
            params.height = mPreviewSize.getHeight();
            mSurfaceView.setLayoutParams(params);
        }else{
            //params.width = mPreviewSize.getHeight();
            //params.height = mPreviewSize.getWidth();
            params.width = metrics.widthPixels;
            params.height = metrics.widthPixels;
            mSurfaceView.setLayoutParams(params);
        }



    }

    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }
        MyLog.d("here bigEnough " + bigEnough);
        MyLog.d("here notBigEnough " + notBigEnough);

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e("Camera2", "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * 정보를 스태거드그리드레이아웃으로 보여주도록 설정한다.
     * @param row 스태거드그리드레이아웃에 사용할 열의 개수
     */
    private void setLayoutManager(int row) {
        layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        captureList.setLayoutManager(layoutManager);
    }

    /**
     * 리사이클러뷰를 설정하고 스크롤 리스너를 추가한다.
     */
    private void setRecyclerView() {
        setLayoutManager(listTypeValue);

        infoListAdapter = new CaptureListAdapter(context,
                R.layout.row_capture_list, new ArrayList<CaptureItem>());
        captureList.setAdapter(infoListAdapter);
    }

}
