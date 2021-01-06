package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;


public class FragSample extends Fragment {
    public static final String INFO_SEQ = "INFO_SEQ";
    private final String TAG = this.getClass().getSimpleName();

    int infoSeq;
    ImageView sampleImage;

    public static FragSample newInstance(int infoSeq) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO_SEQ, Parcels.wrap(infoSeq));

        FragSample fragment = new FragSample();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            infoSeq = Parcels.unwrap(getArguments().getParcelable(INFO_SEQ));
        }

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.frag_sample_1, container, false);

        sampleImage = rootView.findViewById(R.id.image);

        if (infoSeq == 0){
            setImage(sampleImage, SampleActivity.infoItem.beforeImageFilename);
        }else{
            setImage(sampleImage, SampleActivity.infoItem.afterImageFilename);
        }
        return rootView;
    }

    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.get().load(RemoteService.SAMPLE_URL + fileName).into(imageView);
        }
    }
}