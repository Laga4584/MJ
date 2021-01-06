package com.example.bestfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestfood.BuildConfig;
import com.example.bestfood.item.CaseInfoItem;
import com.example.bestfood.item.ChatItem;
import com.example.bestfood.R;
import com.example.bestfood.lib.MyLog;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_CHAT_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_CHAT_MESSAGE_RECEIVED = 0;

    private Context mcontext;
    ArrayList<ChatItem> messageList;

    public ChatMessageAdapter(Context mcontext, ArrayList<ChatItem> messageList) {
        this.mcontext = mcontext;
        this.messageList = messageList;
        MyLog.d("here list " + this.messageList.toString());
    }

    @Override
    public int getItemCount() {
        MyLog.d("here size " + this.messageList.size());
        return this.messageList.size();
    }


    public int getItemViewType(int position) {
        if (this.messageList.get(position).sending == 1){
            return VIEW_TYPE_CHAT_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_CHAT_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        MyLog.d("here viewtype " + viewType);

        if (viewType == VIEW_TYPE_CHAT_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_CHAT_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_sent, parent, false);
            return new SentMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /*
        switch (messageList.get(position).sending) {
            case 1:
                ((SentMessageHolder) holder).bind(position);
            case 0:
                ((ReceivedMessageHolder) holder).bind(position);
        }

         */
        MyLog.d("here viewtype2 " + holder.getItemViewType());
        if (holder.getItemViewType() == 1) {
            ((SentMessageHolder) holder).bind(position);
        } else if (holder.getItemViewType() == 0){
            ((ReceivedMessageHolder) holder).bind(position);
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
            timeText.setText(messageList.get(position).regDate.substring(11,13) + " : " + messageList.get(position).regDate.substring(13,15));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_received_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_received_message_time);
        }

        void bind(int position) {
            messageText.setText(messageList.get(position).message);
            timeText.setText(messageList.get(position).regDate.substring(11,13) + " : " + messageList.get(position).regDate.substring(13,15));
        }
    }


    public void addItemList(ArrayList<ChatItem> itemList) {
        this.messageList.addAll(itemList);
        notifyDataSetChanged();
        MyLog.d("here list " + this.messageList.toString());
    }



}
