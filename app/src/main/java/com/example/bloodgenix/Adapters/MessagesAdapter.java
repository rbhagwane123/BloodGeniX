package com.example.bloodgenix.Adapters;

import static com.example.bloodgenix.activities.ChatActivity.receiverImg;
import static com.example.bloodgenix.activities.ChatActivity.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodgenix.Models.Messages;
import com.example.bloodgenix.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;
    String senderID;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList, String senderID) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
//            Toast.makeText(context, "inside onCreateView send", Toast.LENGTH_SHORT).show();
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent, false);
            return new SenderViewsHolder(view);

        }
        else{
//            Toast.makeText(context, "inside onCreateView receive", Toast.LENGTH_SHORT).show();
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item, parent, false);
            return new ReceiverViewsHolder(view);
        }
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages= messagesArrayList.get(position);

        if (holder.getClass() == SenderViewsHolder.class){


            SenderViewsHolder viewsHolder = (SenderViewsHolder) holder;
            viewsHolder.senderMessageTxt.setText(messages.getMessage());

//            Toast.makeText(context, "inside onBindView send", Toast.LENGTH_SHORT).show();
            Glide.with(context).load(senderImg).into(viewsHolder.senderProfileImg);
        }else{
            ReceiverViewsHolder viewsHolder = (ReceiverViewsHolder) holder;
            viewsHolder.senderMessageTxt.setText(messages.getMessage());


//            Toast.makeText(context, "inside onBindView receive", Toast.LENGTH_SHORT).show();
            Glide.with(context).load(receiverImg).into(viewsHolder.senderProfileImg);
        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (senderID.equals(messages.getSenderID()))
        {
//            Toast.makeText(context, "inside getItemView send", Toast.LENGTH_SHORT).show();
            return ITEM_SEND;
        }else{
//            Toast.makeText(context, "inside getItemView receive", Toast.LENGTH_SHORT).show();
            return ITEM_RECEIVE;
        }
    }

    class SenderViewsHolder extends RecyclerView.ViewHolder{
        CircleImageView senderProfileImg;
        TextView senderMessageTxt;
        public SenderViewsHolder(@NonNull View itemView) {
            super(itemView);

            senderProfileImg = itemView.findViewById(R.id.ProfileImg);
            senderMessageTxt = itemView.findViewById(R.id.MessageTxt);
        }
    }

    class ReceiverViewsHolder extends RecyclerView.ViewHolder{
        CircleImageView senderProfileImg;
        TextView senderMessageTxt;
        public ReceiverViewsHolder(@NonNull View itemView) {
            super(itemView);
            senderProfileImg = itemView.findViewById(R.id.ProfileImg);
            senderMessageTxt = itemView.findViewById(R.id.MessageTxt);
        }
    }
}
