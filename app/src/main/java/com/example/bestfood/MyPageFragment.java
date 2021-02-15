package com.example.bestfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


/**
 * 프로필을 설정할 수 있는 액티비티
 */
public class MyPageFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    Context context;

    CardView cardView1_1, cardView1_2, cardView1_3, cardView2_1;
    ImageView profileIcon;
    LinearLayout content1, content2;
    TextView profileIconDefault, edit, id, name, phone, postcode, address, addressDetail;

    UserItem currentItem;

    /**
     * 액티비티를 생성하고 화면을 구성한다.
     * @param savedInstanceState 액티비티가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_mypage, container, false);

        context = this.getActivity();
        //currentItem = ((App) this.getActivity().getApplication()).getUserItem();

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        content1 = view.findViewById(R.id.content1);
        content2 = view.findViewById(R.id.content2);

        //setView();

    }

    private void setView(){
        if (currentItem.seq != 0){
            content1.setVisibility(View.VISIBLE);
            content2.setVisibility(View.GONE);
            profileIcon = getView().findViewById(R.id.profile_icon);
            profileIconDefault = getView().findViewById(R.id.profile_icon_default);
            id = getView().findViewById(R.id.id);
            name = getView().findViewById(R.id.name);

            cardView1_1 = getView().findViewById(R.id.card_view_1_1);
            cardView1_2 = getView().findViewById(R.id.card_view_1_2);
            cardView1_3 = getView().findViewById(R.id.card_view_1_3);
            cardView1_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    startActivity(intent);
                }
            });
            cardView1_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, KeepRepairerActivity.class);
                    startActivity(intent);
                }
            });
            cardView1_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, KeepSampleActivity.class);
                    startActivity(intent);
                }
            });

        }else{
            content1.setVisibility(View.GONE);
            content2.setVisibility(View.VISIBLE);
            cardView2_1 = getView().findViewById(R.id.card_view_2_1);

            cardView2_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 화면이 보여질 때 호출되며 사용자 정보를 기반으로 프로필 아이콘을 설정한다.
     */
    @Override
    public void onResume() {
        super.onResume();

        currentItem = ((App) this.getActivity().getApplication()).getUserItem();
        setView();

        if (currentItem.seq != 0) {

            if (StringLib.getInstance().isBlank(currentItem.userIconFilename)) {
                char initialChar = Character.toUpperCase(currentItem.email.charAt(0));
                profileIconDefault.setText(Character.toString(initialChar));
                profileIconDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProfileIconActivity.class);
                        startActivity(intent);
                    }
                });

            } else {
                profileIconDefault.setVisibility(View.GONE);
                Picasso.get()
                        .load(RemoteService.USER_ICON_URL + currentItem.userIconFilename)
                        .into(profileIcon);
                profileIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProfileIconActivity.class);
                        startActivity(intent);
                    }
                });
            }



            id.setText(currentItem.email);
            name.setText(currentItem.name);
        }
    }
}