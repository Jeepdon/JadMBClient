package com.jadonvb.misc;

import com.jadonvb.Client;
import com.jadonvb.Logger;
import com.jadonvb.enums.MessageType;
import com.jadonvb.messages.Message;

public class ShutdownHook extends Thread {

    private Client client;

    public ShutdownHook(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        Logger logger = new Logger("JadMBClient");

        Message message = new Message();
        message.setType(MessageType.UNREGISTER);
        message.setSender(client.getName());
        message.setReceiver("MB");

        client.sendMessage(message);
        logger.log("Successfully send shutdown message!");
        logger.log("Shutting down");
    }
}