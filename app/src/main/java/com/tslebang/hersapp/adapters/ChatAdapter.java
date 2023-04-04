package com.tslebang.hersapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.tslebang.hersapp.R;
import com.tslebang.hersapp.models.AllMethods;
import com.tslebang.hersapp.models.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterViewHolder> {

    Context context;
    List<Message> messageList;
    DatabaseReference messageDb;

    public ChatAdapter(Context context, List<Message> messageList, DatabaseReference messageDb) {
        this.context = context;
        this.messageList = messageList;
        this.messageDb = messageDb;
    }

    public class ChatAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv;
        ImageButton mDelete;
        LinearLayout linearl;

        public ChatAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            mDelete = itemView.findViewById(R.id.deleteBtn);
            linearl = itemView.findViewById(R.id.lmessage);

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageDb.child(messageList.get(getAbsoluteAdapterPosition()).getKey()).removeValue();

                }
            });


        }
    }
    @NonNull
    @Override
    public ChatAdapter.ChatAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item,parent,false);
        return new ChatAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatAdapterViewHolder holder, int position) {

        Message message = messageList.get(position);
        if (message.getName().equals(AllMethods.name)){
            holder.titleTv.setText("You: "+ message.getMessage());
            holder.titleTv.setGravity(Gravity.START);
            holder.linearl.setBackgroundColor(Color.parseColor("black"));
        }else{
            holder.titleTv.setText(message.getName()+":"+message.getMessage());
            holder.mDelete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();

    }
}
