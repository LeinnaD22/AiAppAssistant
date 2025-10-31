package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context context;
    ArrayList<Message> messages;
    public MessageAdapter(List<Message> messages){
        this.context = context;
        this.messages = (ArrayList<Message>) messages;
    }

    public void userInsert(String enteredValue, int positon){
        messages.add(new Message("Me", enteredValue, System.currentTimeMillis(), true));
       // notifyItemInserted(positon - 1);

    }

    public void aiInsert(String resultText, int position){
        messages.add(new Message("Gemini", resultText, System.currentTimeMillis(), false));
        //notifyItemInserted(position - 1);
    }
    

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on viewType (e.g., isMine)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(messages.get(position).isMine() == true){
        holder.leftChatLayout.setVisibility(View.GONE);
        holder.rightChatLayout.setVisibility(View.VISIBLE);
        holder.rightChatTextView.setText(messages.get(position).getContent());
        }
        else {
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatTextView.setText(messages.get(position).getContent());
        }
        
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextView, rightChatTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);

        }
    }
}