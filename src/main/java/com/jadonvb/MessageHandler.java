package com.jadonvb;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MessageHandler extends Thread {

    private final Logger logger;
    private final Socket clientSocket;
    private final Gson gson;
    private PrintWriter out;
    private BufferedReader in;


    public MessageHandler(Socket socket, String name) {
        this.logger = new Logger("JadMBClient");
        this.gson = new Gson();
        clientSocket = socket;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sendMessage(name);
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

                Message message = new Message();
                message.setType(MessageTypes.INFO);
                message.setSender("hoihoi");
                message.setRequest(false);
                ArrayList<String> args = new ArrayList<>();
                args.add("hehehe");
                args.add("hoihoi");
                message.setArguments(args);


                sendMessage(gson.toJson(message));
                Thread.sleep(100);
            } catch (IOException e) {
                logger.error("Could not send message");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(String msg) throws IOException {
        out.println(msg);
    }
}
