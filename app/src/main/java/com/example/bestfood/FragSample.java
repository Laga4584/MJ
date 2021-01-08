package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bestfood.custom.CustomView;
import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class FragSample extends Fragment {
    public static final String INFO_SEQ = "INFO_SEQ";
    public static final String TYPE = "TYPE";
    private final String TAG = this.getClass().getSimpleName();

    int infoSeq;
    int type;
    ImageView sampleImage;
    CustomView dotImage;

    public static FragSample newInstance(int infoSeq, int type) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO_SEQ, Parcels.wrap(infoSeq));
        bundle.putParcelable(TYPE, Parcels.wrap(type));

        FragSample fragment = new FragSample();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            infoSeq = Parcels.unwrap(getArguments().getParcelable(INFO_SEQ));
            type = Parcels.unwrap(getArguments().getParcelable(TYPE));
        }

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.frag_sample_1, container, false);

        sampleImage = rootView.findViewById(R.id.image);
        dotImage = rootView.findViewById(R.id.dot);

        if (type == 0){
            if (infoSeq == 0){
                setImage(sampleImage, SampleActivity.infoItem.beforeImageFilename);
                setDot(SampleActivity.infoItem.dot);
            }else{
                setImage(sampleImage, SampleActivity.infoItem.afterImageFilename);
            }
        }else{
            if (infoSeq == 0){
                setImage(sampleImage, CaseActivity.infoItem.imageFilename);
                setDot(CaseActivity.infoItem.dot);
            }else{
                setImage(sampleImage, DetailActivity.images.get(infoSeq-1).filename);
            }
        }


        return rootView;
    }

    private void setImage(ImageView imageView, String fileName) {

        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            if (type == 0) Picasso.get().load(RemoteService.SAMPLE_URL + fileName).into(imageView);
            else Picasso.get().load(RemoteService.IMAGE_URL + fileName).into(imageView);
        }
    }

    private void setDot(String dot){
        MyLog.d("here dot" + dot.toString());
        ArrayList position_list_X = new ArrayList();
        ArrayList position_list_Y = new ArrayList();
        String[] positions = dot.split("/ ");
        for (String position : positions) {
            position_list_X.add(Integer.parseInt(position.split(", ")[0]));
            position_list_Y.add(Integer.parseInt(position.split(", ")[1]));
        }
        dotImage.position_list_X = position_list_X;
        dotImage.position_list_Y = position_list_Y;
        dotImage.invalidate();
    }
}