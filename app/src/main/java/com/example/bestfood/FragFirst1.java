package com.example.bestfood;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.GoLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;

import org.parceler.Parcels;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragFirst1 extends Fragment implements View.OnClickListener{
    public static final String INFO_ITEM = "INFO_ITEM";
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    CaseInfoItem infoItem;

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    EditText descriptionEdit;

    Button cameraButton;
    Button nextButton;

    String[] services = {"수선", "염색"};
    String[] products = {"핸드백", "지갑"};
    String[] brands = {"브랜드1", "브랜드2"};

    /**
     * FoodInfoItem 객체를 인자로 저장하는
     * BestFoodRegisterInputFragment 인스턴스를 생성해서 반환한다.
     * @param infoItem 맛집 정보를 저장하는 객체
     * @return BestFoodRegisterInputFragment 인스턴스
     */
    public static FragFirst1 newInstance(CaseInfoItem infoItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO_ITEM, Parcels.wrap(infoItem));

        FragFirst1 fragment = new FragFirst1();
        fragment.setArguments(bundle);

        return fragment;
    }
    /**
     * 프래그먼트가 생성될 때 호출되며 인자에 저장된 FoodInfoItem를
     * BestFoodRegisterActivity에 currentItem를 저장한다.
     * @param savedInstanceState 프래그먼트가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //infoItem = new CaseInfoItem();

        if (getArguments() != null) {
            infoItem = Parcels.unwrap(getArguments().getParcelable(INFO_ITEM));
            if (infoItem.seq != 0) {
                CaseActivity.infoItem = infoItem;
            }
            MyLog.d(TAG, "infoItem " + infoItem);
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = this.getActivity();
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.frame_1p_1, container, false);

        return rootView;
    }

    /**
     * onCreateView() 메소드 뒤에 호출되며 맛집 정보를 입력할 뷰들을 생성한다.
     * @param view onCreateView() 메소드에 의해 반환된 뷰
     * @param savedInstanceState null이 아니라면 이전에 저장된 상태를 가진 객체
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                infoItem.service = services[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, products);
        adapter2.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                infoItem.product = products[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner3 = (Spinner) view.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, brands);
        adapter3.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                infoItem.brand = brands[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        descriptionEdit = (EditText) view.findViewById(R.id.input5);
        descriptionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                infoItem.description = descriptionEdit.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        cameraButton = view.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(this);


        FragFirst.currentItem = infoItem;

        nextButton = (Button) view.findViewById(R.id.button);
        nextButton.setOnClickListener(this);
    }


    /**
     * 클릭이벤트를 처리한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.camera_button) {
            MyLog.d(TAG, "onClick button ");
            goNextPage();
            /*
            GoLib.getInstance().goFragment(getActivity().getSupportFragmentManager(),
                    R.id.content_main, BestFoodRegisterImageFragment.newInstance(infoItem));

             */
        } else if (v.getId() == R.id.button) {

            FragFirst.viewPager2.setCurrentItem(1);
            insertCaseInfo();
            CaseActivity.infoItem = infoItem;
            /*
            MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.ll_fragment);
            mainFragment.changeFragmentTextView("호호호");

*/
            //((CaseActivity)getActivity()).notifyToSlideToRespectivePage(0);

            //insertCaseInfo();
           // MyLog.d(TAG, "button clicked");


        }
    }

    /**
     * 사용자가 입력한 정보를 서버에 저장한다.
     */
    private void insertCaseInfo() {
        MyLog.d(TAG, infoItem.toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertCaseInfo(infoItem);
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
                        //등록 실패
                    } else {
                        infoItem.seq = seq;
                        //goNextPage();
                    }
                } else { // 등록 실패
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

    private void goNextPage() {
        MyLog.d(TAG, "onClick button go");
        GoLib.getInstance().goImageActivity(context, infoItem.seq);
    }


}