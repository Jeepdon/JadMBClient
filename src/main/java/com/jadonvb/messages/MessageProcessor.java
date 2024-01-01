package com.jadonvb.messages;

import com.google.gson.Gson;
import com.jadonvb.Client;
import com.jadonvb.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MessageProcessor extends Thread {

    private final Logger logger;
    private final Gson gson;
    private PrintWriter out;
    private BufferedReader in;
    private final Client client;


    public MessageProcessor(Client client, Socket socket) {
        this.logger = new Logger("JadMBClient");
        this.gson = new Gson();
        this.client = client;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException exception) {
            logger.error("Could not open in/output stream");
            return;
        }

        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                processMessage(in.readLine());
            } catch (IOException e) {
                logger.error("Could not read incoming message!");
                throw new RuntimeException(e);
            }
        }
    }

    private void processMessage(String messageString) {
        try {
            Message message = gson.fromJson(messageString, Message.class);
            for (MessageListener messageListener : client.getListeners()) {
                messageListener.getMessage(message);
            }
        } catch (IllegalStateException illegalStateException) {
            logger.error("Not in JSON");
            logger.error(messageString);
        }
    }

    public void sendMessage(Message message) throws IOException {
        out.println(gson.toJson(message));
    }
}
