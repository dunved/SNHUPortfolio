package com.example.projecttwodundivedantam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends ComponentActivity {

    private SharedPreferences prefs;
    private String username;
    private List<ChatMessage> messages;
    private ChatAdapter adapter;
    private ListView listViewMessages;
    private EditText editChatMessage;

    private static final String CHAT_KEY = "gym_chat_messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra("username");
        if (username == null) username = "Member";

        prefs            = getSharedPreferences("AnacondaPrefs", MODE_PRIVATE);
        listViewMessages = findViewById(R.id.listViewMessages);
        editChatMessage  = findViewById(R.id.editChatMessage);
        Button btnSend   = findViewById(R.id.btnSendMessage);
        Button btnBack   = findViewById(R.id.btnBackFromChat);

        messages = new ArrayList<>();
        adapter  = new ChatAdapter(this, messages, username);
        listViewMessages.setAdapter(adapter);

        loadMessages();

        btnSend.setOnClickListener(v -> {
            String text = editChatMessage.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please type a message.", Toast.LENGTH_SHORT).show();
                return;
            }
            sendMessage(text);
            editChatMessage.setText("");
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void sendMessage(String text) {
        String time = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        try {
            String existing = prefs.getString(CHAT_KEY, "[]");
            JSONArray arr   = new JSONArray(existing);
            JSONObject msg  = new JSONObject();
            msg.put("sender",  username);
            msg.put("content", text);
            msg.put("time",    time);
            arr.put(msg);
            prefs.edit().putString(CHAT_KEY, arr.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        messages.add(new ChatMessage(username, text, time, true));
        adapter.notifyDataSetChanged();
        listViewMessages.setSelection(adapter.getCount() - 1);
    }

    private void loadMessages() {
        messages.clear();
        try {
            String existing = prefs.getString(CHAT_KEY, "[]");
            JSONArray arr   = new JSONArray(existing);
            String time     = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

            for (int i = 0; i < arr.length(); i++) {
                JSONObject msg = arr.getJSONObject(i);
                String sender  = msg.getString("sender");
                String content = msg.getString("content");
                String msgTime = msg.optString("time", time);
                boolean isMe   = sender.equals(username);
                messages.add(new ChatMessage(sender, content, msgTime, isMe));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
        if (!messages.isEmpty()) {
            listViewMessages.setSelection(adapter.getCount() - 1);
        }
    }
}