package com.jadonvb.messages;

import com.jadonvb.enums.MessageType;

import java.util.ArrayList;

public class Message {

    private String sender;
    private String receiver;
    private MessageType type;
    private ArrayList<String> arguments;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setArguments(ArrayList<String> message) {
        this.arguments = message;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public MessageType getType() {
        return type;
    }
}
