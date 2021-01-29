package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.EtcLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.MyToast;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;


/**
 * 프로필을 설정할 수 있는 액티비티
 */
public class ProfileFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    Context context;

    CardView cardView;
    ImageView profileIconImage;
    TextView edit, title, id, name, phone, postcode, address, addressDetail;

    UserItem currentItem;

    /**
     * 액티비티를 생성하고 화면을 구성한다.
     * @param savedInstanceState 액티비티가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_profile, container, false);

        context = this.getActivity();
        currentItem = ((App) this.getActivity().getApplication()).getUserItem();

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardView = view.findViewById(R.id.card_view);
        DisplayMetrics metrics = ((App) this.getActivity().getApplication()).getMetrics();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        layoutParams.setMargins(metrics.widthPixels/18, metrics.heightPixels/38, metrics.widthPixels/18, metrics.heightPixels/38);
        cardView.requestLayout();
        cardView.setContentPadding(metrics.widthPixels/36, metrics.heightPixels/76, metrics.widthPixels/36, metrics.heightPixels/76);

        edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        profileIconImage = view.findViewById(R.id.profile_icon);
        title = view.findViewById(R.id.title);
        id = view.findViewById(R.id.id);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        postcode = view.findViewById(R.id.postcode);
        address = view.findViewById(R.id.address);
        addressDetail = view.findViewById(R.id.address_detail);
    }

    /**
     * 화면이 보여질 때 호출되며 사용자 정보를 기반으로 프로필 아이콘을 설정한다.
     */
    @Override
    public void onResume() {
        super.onResume();

        if (StringLib.getInstance().isBlank(currentItem.userIconFilename)) {
            Picasso.get().load(R.drawable.ic_person).into(profileIconImage);
        } else {
            Picasso.get()
                    .load(RemoteService.USER_ICON_URL + currentItem.userIconFilename)
                    .into(profileIconImage);
        }

        title.setText(currentItem.name);
        id.setText(currentItem.email);
        name.setText(currentItem.name);
        phone.setText(currentItem.phone);
        //address.setText(currentItem.);
    }
}