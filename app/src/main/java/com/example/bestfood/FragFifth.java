package com.example.bestfood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;


public class FragFifth extends Fragment {
    ImageView repairImage;
    TextView repairText;
    TextView stateText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.frame_5p, container, false);

        repairImage = rootView.findViewById(R.id.image);
        repairText = rootView.findViewById(R.id.text2);
        stateText = rootView.findViewById(R.id.text3);

        setImage(repairImage, CaseActivity.infoItem.repairImageFilename);
        repairText.setText(CaseActivity.infoItem.repairDescription);
        stateText.setText(CaseActivity.infoItem.repairState);
        return rootView;
    }

    private void setImage(ImageView imageView, String fileName) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.get().load(RemoteService.IMAGE_URL + fileName).into(imageView);
        }
    }
}