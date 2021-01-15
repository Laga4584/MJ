package com.example.bestfood.lib;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bestfood.BestFoodInfoActivity;
import com.example.bestfood.CaseActivity;
import com.example.bestfood.ChatActivity;
import com.example.bestfood.DetailActivity;
import com.example.bestfood.ImageActivity;
import com.example.bestfood.ProfileActivity;
import com.example.bestfood.RepairerActivity;
import com.example.bestfood.SampleActivity;

/**
 * 액티비티나 프래그먼트 실행 라이브러리
 */
public class GoLib {
    public final String TAG = GoLib.class.getSimpleName();
    private volatile static GoLib instance;

    public static GoLib getInstance() {
        if (instance == null) {
            synchronized (GoLib.class) {
                if (instance == null) {
                    instance = new GoLib();
                }
            }
        }
        return instance;
    }

    /**
     * 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     * @param containerViewId 프래그먼트를 보여줄 컨테이너 뷰 아이디
     * @param fragment 프래그먼트
     */
    public void goFragment(FragmentManager fragmentManager, int containerViewId,
                           Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    /**
     * 뒤로가기를 할 수 있는 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     * @param containerViewId 프래그먼트를 보여줄 컨테이너 뷰 아이디
     * @param fragment 프래그먼트
     */
    public void goFragmentBack(FragmentManager fragmentManager, int containerViewId,
                               Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 이전 프래그먼트를 보여준다.
     * @param fragmentManager 프래그먼트 매니저
     */
    public void goBackFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }

    /**
     * 프로파일 액티비티를 실행한다.
     * @param context 컨텍스트
     */
    public void goProfileActivity(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 맛집 정보 액티비티를 실행한다.
     * @param context 컨텍스트
     * @param infoSeq 맛집 정보 일련번호
     */
    public void goBestFoodInfoActivity(Context context, int infoSeq) {
        Intent intent = new Intent(context, BestFoodInfoActivity.class);
        intent.putExtra(BestFoodInfoActivity.INFO_SEQ, infoSeq);
        context.startActivity(intent);
    }

    public void goRepairerActivity(Context context, int infoSeq) {
        Intent intent = new Intent(context, RepairerActivity.class);
        intent.putExtra(RepairerActivity.INFO_SEQ, infoSeq);
        context.startActivity(intent);
    }

    public void goCaseActivity(Context context, int infoSeq) {
        Intent intent = new Intent(context, CaseActivity.class);
        intent.putExtra(CaseActivity.INFO_SEQ, infoSeq);
        context.startActivity(intent);
    }

    public void goChatActivity(Context context, int repairerSeq) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.REPAIRER_SEQ, repairerSeq);
        context.startActivity(intent);
    }

    public void goImageActivity(Context context, int infoSeq) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(ImageActivity.INFO_SEQ, infoSeq);
        context.startActivity(intent);
    }

    public void goSampleActivity(Context context, int infoSeq) {
        MyLog.d(TAG, "here intent");
        Intent intent = new Intent(context, SampleActivity.class);
        MyLog.d(TAG, "here intent2");
        intent.putExtra(SampleActivity.INFO_SEQ, infoSeq);
        MyLog.d(TAG, "here intent3");
        context.startActivity(intent);
    }

    public void goDetailActivity(Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        //intent.putExtra(ImageActivity.INFO_SEQ, infoSeq);
        context.startActivity(intent);
    }
}
