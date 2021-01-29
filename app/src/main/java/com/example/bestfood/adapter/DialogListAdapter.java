package com.example.bestfood.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bestfood.Constant;
import com.example.bestfood.App;
import com.example.bestfood.R;
import com.example.bestfood.item.NoticeItem;
import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.GoLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 다이얼로그의 아이템을 처리하는 어댑터
 */
public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private int resource;
    private ArrayList<String> itemList;
    private UserItem userItem;
    DisplayMetrics metrics;

    /**
     * 어댑터 생성자
     * @param context 컨텍스트 객체
     * @param resource 아이템을 보여주기 위해 사용할 리소스 아이디
     * @param itemList 아이템 리스트
     */
    public DialogListAdapter(Context context, int resource, ArrayList<String> itemList) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;

        userItem = ((App) context.getApplicationContext()).getUserItem();
    }

    /**
     * 특정 아이템의 변경사항을 적용하기 위해 기본 아이템을 새로운 아이템으로 변경한다.
     * @param newItem 새로운 아이템
     */
    /*
    public void setItem(String newItem) {
        for (int i=0; i < itemList.size(); i++) {
            String item = itemList.get(i);

            if (item == newItem) {
                itemList.set(i, newItem);
                notifyItemChanged(i);
                break;
            }
        }
    }

     */

    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     * @param itemList 새로운 아이템 리스트
     */
    public void addItemList(ArrayList<String> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 아이템 크기를 반환한다.
     * @return 아이템 크기
     */
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    /**
     * 뷰홀더(ViewHolder)를 생성하기 위해 자동으로 호출된다.
     * @param parent 부모 뷰그룹
     * @param viewType 새로운 뷰의 뷰타입
     * @return 뷰홀더 객체
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new ViewHolder(v);
    }

    /**
     * 뷰홀더(ViewHolder)와 아이템을 리스트 위치에 따라 연동한다.
     * @param holder 뷰홀더 객체
     * @param position 리스트 위치
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String item = itemList.get(position);
        MyLog.d(TAG, "getView " + item);

        final ViewHolder currentHolder = holder;
        holder.textView.setText(item);

        /*
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //currentHolder.textView.setHeight(metrics.heightPixels/19);
                MyLog.d("here clicked");
            }
        });

         */

        MyLog.d("here getTop" + holder.itemView.getTop());
        MyLog.d("here getBottom" + holder.itemView.getBottom());

    }

    /**
     * 아이템을 보여주기 위한 뷰홀더 클래스
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            metrics = ((App) context.getApplicationContext()).getMetrics();
            textView = (TextView) itemView.findViewById(R.id.textItem);
            //textView.setHeight(metrics.heightPixels/38);
        }
    }
}
