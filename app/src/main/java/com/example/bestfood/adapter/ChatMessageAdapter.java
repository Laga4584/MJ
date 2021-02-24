package com.example.bestfood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.App;
import com.example.bestfood.BuildConfig;
import com.example.bestfood.ChatActivity;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.ChatItem;
import com.example.bestfood.R;
import com.example.bestfood.item.RepairerItem;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.example.bestfood.remote.ServiceGenerator;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bestfood.CaseActivity.INFO_SEQ;
import static com.example.bestfood.ChatActivity.REPAIRER_SEQ;

public class ChatMessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_CHAT_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_CHAT_MESSAGE_RECEIVED = 0;
    private static final int VIEW_TYPE_CHAT_IMAGE_SENT = 3;
    private static final int VIEW_TYPE_CHAT_IMAGE_RECEIVED = 2;

    private Context mcontext;
    private ArrayList<ChatItem> messageList;

    private String repairerName;
    private String repairerImage;

    int repairerSeq;
    public static RepairerItem repairerItem;

    public ChatMessageAdapter(Context mcontext, ArrayList<ChatItem> messageList) {
        this.mcontext = mcontext;
        this.messageList = messageList;

        repairerSeq = ChatActivity.currentRepairerSeq;
        selectRepairerInfo(repairerSeq);
    }

    private void selectRepairerInfo(int repairerSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<RepairerItem> call = remoteService.selectRepairerInfo(repairerSeq);

        call.enqueue(new Callback<RepairerItem>() {
            @Override
            public void onResponse(Call<RepairerItem> call, Response<RepairerItem> response) {
                RepairerItem item = response.body();
                if (response.isSuccessful() && item != null && item.seq > 0) {
                    repairerName = item.name;
                    repairerImage = item.profileImgFilename;
                } else {
                    repairerName = item.name;
                    repairerImage = item.profileImgFilename;
                }
            }

            @Override
            public void onFailure(Call<RepairerItem> call, Throwable t) {
                Log.d("TAG", "no internet connectivity");
                Log.d("TAG", t.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.messageList.size();
    }


    public int getItemViewType(int position) {
        if (this.messageList.get(position).sending == 1) {
            return VIEW_TYPE_CHAT_MESSAGE_SENT;
        } else if (this.messageList.get(position).sending == 0) {
            return VIEW_TYPE_CHAT_MESSAGE_RECEIVED;
        } else if (this.messageList.get(position).sending == 3) {
            return VIEW_TYPE_CHAT_IMAGE_SENT;
        } else {
            return VIEW_TYPE_CHAT_IMAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_CHAT_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_CHAT_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        } else if (viewType == VIEW_TYPE_CHAT_IMAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_sent, parent, false);
            return new SentImageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_image_received, parent, false);
            return new ReceivedImageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            ((SentMessageHolder) holder).bind(position);
        } else if (holder.getItemViewType() == 0) {
            ((ReceivedMessageHolder) holder).bind(position);
        } else if (holder.getItemViewType() == 3) {
            ((SentImageHolder) holder).bind(position);
        } else if (holder.getItemViewType() == 2) {
            ((ReceivedImageHolder) holder).bind(position);
        }
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_sent_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_sent_message_time);
        }

        void bind(int position) {
            messageText.setText(messageList.get(position).message);
            timeText.setText(messageList.get(position).regDate.substring(11,13) + ":" + messageList.get(position).regDate.substring(14,16));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameText, messageText, timeText;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = (ImageView) itemView.findViewById(R.id.image_chat_profile_other);
            nameText = (TextView) itemView.findViewById(R.id.text_chat_user_other);
            messageText = (TextView) itemView.findViewById(R.id.text_received_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_received_message_time);
        }

        void bind(int position) {
            setProfileImage(profileImage, repairerImage, 1);
            nameText.setText(repairerName);
            messageText.setText(messageList.get(position).message);
            timeText.setText(messageList.get(position).regDate.substring(11,13) + ":" + messageList.get(position).regDate.substring(14,16));
        }
    }

    private class SentImageHolder extends RecyclerView.ViewHolder {
        ImageView messageImage;
        TextView timeText;

        public SentImageHolder(@NonNull View itemView) {
            super(itemView);

            messageImage = (ImageView) itemView.findViewById(R.id.image_sent_message);
            timeText = (TextView) itemView.findViewById(R.id.text_sent_message_time);
        }

        void bind(int position) {
            Log.d("checking imageview", messageList.get(position).message);
            setImage(messageImage, messageList.get(position).message);
            timeText.setText(messageList.get(position).regDate.substring(11,13) + ":" + messageList.get(position).regDate.substring(14,16));
        }
    }

    private class ReceivedImageHolder extends RecyclerView.ViewHolder {
        ImageView profileImage, messageImage;
        TextView nameText, timeText;

        public ReceivedImageHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = (ImageView) itemView.findViewById(R.id.image_chat_profile_other);
            nameText = (TextView) itemView.findViewById(R.id.text_chat_user_other);
            messageImage = (ImageView) itemView.findViewById(R.id.image_received_message);
            timeText = (TextView) itemView.findViewById(R.id.text_received_message_time);
        }

        void bind(int position) {
            setProfileImage(profileImage, repairerImage, 1);
            nameText.setText(repairerName);
            setImage(messageImage, messageList.get(position).message);
            timeText.setText(messageList.get(position).regDate.substring(11,13) + ":" + messageList.get(position).regDate.substring(14,16));
        }
    }


    public void addItemList(ArrayList<ChatItem> itemList) {
        this.messageList.addAll(itemList);
        notifyDataSetChanged();
    }

    private void setImage(ImageView imageView, String fileName) {
        String path = RemoteService.IMAGE_URL + fileName + ".png";
        Uri uri = Uri.parse(path);
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.get().load(uri).into(imageView);
        }
    }

    private void setProfileImage(ImageView imageView, String fileName, int path) {
        if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            if (path == 0) Picasso.get().load(RemoteService.IMAGE_URL + fileName).into(imageView);
            else Picasso.get().load(RemoteService.USER_ICON_URL + fileName).into(imageView);
        }
    }
}
