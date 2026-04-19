package com.example.projecttwodundivedantam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private final String currentUser;

    public ChatAdapter(Context context, List<ChatMessage> messages, String currentUser) {
        super(context, 0, messages);
        this.currentUser = currentUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_chat_message, parent, false);
        }

        ChatMessage message = getItem(position);

        TextView textSender  = convertView.findViewById(R.id.textMessageSender);
        TextView textContent = convertView.findViewById(R.id.textMessageContent);
        TextView textTime    = convertView.findViewById(R.id.textMessageTime);
        LinearLayout bubble  = convertView.findViewById(R.id.bubbleContainer);

        textSender.setText(message.getSender());
        textContent.setText(message.getContent());
        textTime.setText(message.getTime());

        // Current user messages appear on the right in a different color
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        if (message.isCurrentUser()) {
            bubble.setBackgroundColor(0xFF1A3A1A);
            textSender.setTextColor(0xFF66BB6A);
            params.gravity = android.view.Gravity.END;
        } else {
            bubble.setBackgroundColor(0xFF2A2A2A);
            textSender.setTextColor(0xFFAAAAAA);
            params.gravity = android.view.Gravity.START;
        }

        bubble.setLayoutParams(params);

        // Align whole row
        LinearLayout row = (LinearLayout) convertView;
        row.setGravity(message.isCurrentUser()
                ? android.view.Gravity.END
                : android.view.Gravity.START);

        return convertView;
    }
}