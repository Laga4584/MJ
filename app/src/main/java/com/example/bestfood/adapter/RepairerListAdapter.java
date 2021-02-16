package com.example.bestfood.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bestfood.App;
import com.example.bestfood.R;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.GoLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 명장 리스트의 아이템을 처리하는 어댑터
 */
public class RepairerListAdapter extends RecyclerView.Adapter<RepairerListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private int resource;
    private ArrayList<RepairerItem> itemList;
    private UserItem userItem;

    /**
     * 어댑터 생성자
     * @param context 컨텍스트 객체
     * @param resource 아이템을 보여주기 위해 사용할 리소스 아이디
     * @param itemList 아이템 리스트
     */
    public RepairerListAdapter(Context context, int resource, ArrayList<RepairerItem> itemList) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;

        userItem = ((App) context.getApplicationContext()).getUserItem();
    }

    /**
     * 특정 아이템의 변경사항을 적용하기 위해 기본 아이템을 새로운 아이템으로 변경한다.
     * @param newItem 새로운 아이템
     */
    public void setItem(RepairerItem newItem) {
        for (int i=0; i < itemList.size(); i++) {
            RepairerItem item = itemList.get(i);

            if (item.seq == newItem.seq) {
                itemList.set(i, newItem);
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * 현재 아이템 리스트에 새로운 아이템 리스트를 추가한다.
     * @param itemList 새로운 아이템 리스트
     */
    public void addItemList(ArrayList<RepairerItem> itemList) {
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void clearItemList() {
        this.itemList.clear();
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
        final RepairerItem item = itemList.get(position);
        MyLog.d(TAG, "getView " + item);

        String nameString = item.name + " 명장";
        holder.nameText.setText(nameString);
        String infoString = "완료 " + item.caseCount + " | 평점" + item.score;
        holder.infoText.setText(infoString);
        String productString = item.product + " 분야";
        holder.productText.setText(productString);

        setImage(holder.profileIcon, item.profileImgFilename);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoLib.getInstance().goRepairerActivity(context, item.seq);
            }
        });


    }

    /**
     * 이미지를 설정한다.
     * @param imageView  이미지를 설정할 뷰
     * @param fileName 이미지 파일이름
     */
    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.get().load(RemoteService.USER_ICON_URL + fileName).into(imageView);
        }
    }


    /**
     * 아이템을 보여주기 위한 뷰홀더 클래스
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileIcon;
        TextView nameText;
        TextView infoText;
        TextView productText;

        public ViewHolder(View itemView) {
            super(itemView);

            profileIcon = itemView.findViewById(R.id.icon_profile);
            nameText = itemView.findViewById(R.id.text_name);
            infoText = itemView.findViewById(R.id.text_info);
            productText = itemView.findViewById(R.id.text_product);

            /*
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            ViewGroup.LayoutParams params = image.getLayoutParams();
            params.width = metrics.widthPixels/4;
            params.height = metrics.widthPixels/4;
            image.setLayoutParams(params);

             */
        }
    }
}
