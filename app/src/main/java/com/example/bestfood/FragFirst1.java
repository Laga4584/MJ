package com.example.bestfood;

import android.content.Context;
import android.location.Address;
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
import com.example.bestfood.item.FoodInfoItem;
import com.example.bestfood.item.GeoItem;
import com.example.bestfood.lib.GeoLib;
import com.example.bestfood.lib.GoLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;

import org.parceler.Parcels;


public class FragFirst1 extends Fragment implements View.OnClickListener{
    public static final String INFO_ITEM = "INFO_ITEM";
    private final String TAG = this.getClass().getSimpleName();

    Context context;
    CaseInfoItem infoItem;
    Address address;

    Spinner spinner1;
    Spinner spinner2;
    EditText descriptionEdit;
    TextView currentLength;

    String[] services = {"수선", "염색"};
    String[] products = {"핸드백", "지갑"};
    /**
     * FoodInfoItem 객체를 인자로 저장하는
     * BestFoodRegisterInputFragment 인스턴스를 생성해서 반환한다.
     * @param infoItem 맛집 정보를 저장하는 객체
     * @return BestFoodRegisterInputFragment 인스턴스
     */
    public static BestFoodRegisterInputFragment newInstance(FoodInfoItem infoItem) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO_ITEM, Parcels.wrap(infoItem));

        BestFoodRegisterInputFragment fragment = new BestFoodRegisterInputFragment();
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

        if (getArguments() != null) {
            infoItem = Parcels.unwrap(getArguments().getParcelable(INFO_ITEM));
            if (infoItem.seq != 0) {
                CaseActivity.currentItem = infoItem;
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


        descriptionEdit = (EditText) view.findViewById(R.id.input4);
        descriptionEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentLength.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button cameraButton = (Button) view.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(this);

        Button nextButton = (Button) view.findViewById(R.id.button);
        nextButton.setOnClickListener(this);
    }
    /**
     * 클릭이벤트를 처리한다.
     * @param v 클릭한 뷰에 대한 정보
     */
    @Override
    public void onClick(View v) {
        infoItem.description = descriptionEdit.getText().toString();
        MyLog.d(TAG, "onClick imageItem " + infoItem);

        if (v.getId() == R.id.input4) {
            /*
            GoLib.getInstance().goFragment(getActivity().getSupportFragmentManager(),
                    R.id.content_main, BestFoodRegisterImageFragment.newInstance(infoItem));

             */
        } else if (v.getId() == R.id.button) {
            /*
            MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.ll_fragment);
            mainFragment.changeFragmentTextView("호호호");
*/



        }
    }
    /**
     * 프래그먼트로 이동한다.
     */
    private void goNextPage() {
        GoLib.getInstance().goFragmentBack(getActivity().getSupportFragmentManager(),
                R.id.content_main, BestFoodRegisterImageFragment.newInstance(infoItem.seq));
    }

}