package com.example.projecttwodundivedantam;

public class ChatMessage {
    private String sender;
    private String content;
    private String time;
    private boolean isCurrentUser;

    public ChatMessage(String sender, String content, String time, boolean isCurrentUser) {
        this.sender        = sender;
        this.content       = content;
        this.time          = time;
        this.isCurrentUser = isCurrentUser;
    }

    public String getSender()       { return sender; }
    public String getContent()      { return content; }
    public String getTime()         { return time; }
    public boolean isCurrentUser()  { return isCurrentUser; }
}