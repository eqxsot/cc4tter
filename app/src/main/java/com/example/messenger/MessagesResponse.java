package com.example.messenger;

import java.util.ArrayList;

public class MessagesResponse {
    public String[] names;
    public String[] messages;


    public String[] getMessage() {
        return messages;
    }

    public String[] getName() {
        return names;
    }

    public void setMessage(String[] message) {
        this.messages = message;
    }

    public void setName(String[] name) {
        this.names = name;
    }
}
