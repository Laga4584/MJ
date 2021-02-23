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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.App;
import com.example.bestfood.BuildConfig;
import com.example.bestfood.item.CaseItem;
import com.example.bestfood.item.ChatItem;
import com.example.bestfood.R;
import com.example.bestfood.lib.MyLog;
import com.example.bestfood.lib.StringLib;
import com.example.bestfood.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_CHAT_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_CHAT_MESSAGE_RECEIVED = 0;
    private static final int VIEW_TYPE_CHAT_IMAGE_SENT = 3;
    private static final int VIEW_TYPE_CHAT_IMAGE_RECEIVED = 2;

    private Context mcontext;
    private ArrayList<ChatItem> messageList;
    private CaseItem caseItem;

    public ChatMessageAdapter(Context mcontext, ArrayList<ChatItem> messageList) {
        this.mcontext = mcontext;
        this.messageList = messageList;

        caseItem = ((App) mcontext.getApplicationContext()).getCaseItem();
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
            //setImage(profileImage, profileImage);
            //nameText.setText(caseItem.repairerName);
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
            //setImage(profileImage, profileimage);
            //nameText.setText(caseItem.repairerName);
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
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //getResizedBitmap(bitmap, 10);
        imageView.setImageBitmap(bitmap);
        /*if (StringLib.getInstance().isBlank(fileName)) {
            Picasso.get().load(R.drawable.bg_bestfood_drawer).into(imageView);
        } else {
            Picasso.get().load(path).into(imageView);
        }*/
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
