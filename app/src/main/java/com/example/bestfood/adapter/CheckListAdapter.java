package com.example.bestfood.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bestfood.Constant;
import com.example.bestfood.App;
import com.example.bestfood.R;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.CheckItem;
import com.example.bestfood.item.UserItem;
import com.example.bestfood.lib.GoLib;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * 알림 리스트의 아이템을 처리하는 어댑터
 */
public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private int resource;
    private ArrayList<CheckItem> itemList;
    private UserItem userItem;
    private CaseItem caseItem;

    private SparseBooleanArray mSelectedItems;
    public CheckItem selectedItem;


    /**
     * 어댑터 생성자
     * @param context 컨텍스트 객체
     * @param resource 아이템을 보여주기 위해 사용할 리소스 아이디
     * @param itemList 아이템 리스트
     */
    public CheckListAdapter(Context context, int resource, ArrayList<CheckItem> itemList, CaseItem caseItem) {
        this.context = context;
        this.resource = resource;
        this.itemList = itemList;
        this.caseItem = caseItem;

        mSelectedItems = new SparseBooleanArray(itemList.size());
        userItem = ((App) context.getApplicationContext()).getUserItem();
    }

    /**
     * 특정 아이템의 변경사항을 적용하기 위해 기본 아이템을 새로운 아이템으로 변경한다.
     * @param newItem 새로운 아이템
     */
    public void setItem(CheckItem newItem) {
        for (int i=0; i < itemList.size(); i++) {
            CheckItem item = itemList.get(i);

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
    public void addItemList(ArrayList<CheckItem> itemList) {
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CheckItem item = itemList.get(position);
        MyLog.d(TAG, "getView " + item);

        if(mSelectedItems.get(position)){
            holder.itemView.setSelected(true);
        }else{
            holder.itemView.setSelected(false);
        }

        setImage(holder.profileIcon, item.profileImgFilename);

        String nameText = item.name + " 명장";
        holder.name.setText(nameText);
        String infoText = "[" + caseItem.brand + "] " + caseItem.product + " " + caseItem.service + " 외 " + caseItem.dotCount + " 건";
        holder.info.setText(infoText);
        String titleText = "완료 " + item.caseCount + " | 평점 " + item.score + " | " + item.product + " 분야";
        holder.title.setText(titleText);
        holder.description.setText(item.description);
        String priceText = "예상 견적 " + item.price + "원 | " + item.time + "일";
        holder.price.setText(priceText);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItem = item;
                mSelectedItems = new SparseBooleanArray(itemList.size());
                mSelectedItems.put(position, true);
                notifyDataSetChanged();
            }
        });

    }

    /**
     * 이미지를 설정한다.
     * @param imageView  이미지를 설정할 뷰
     * @param fileName 이미지 파일이름
     */
    private void setImage(ImageView imageView, String fileName) {
        if (fileName=="default") {
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
        TextView name, info, title, description, price;

        public ViewHolder(View itemView) {
            super(itemView);

            profileIcon = (ImageView) itemView.findViewById(R.id.profile_icon);
            name = (TextView) itemView.findViewById(R.id.name);
            info = (TextView) itemView.findViewById(R.id.info);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }
}
