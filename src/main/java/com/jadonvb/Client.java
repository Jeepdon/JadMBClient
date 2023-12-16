package com.jadonvb;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

@Mojo(name = "client")
public class Client extends AbstractMojo {

    private Socket clientSocket;
    private MessageProcessor messageHandler;
    private final Logger logger;
    private final String IP = "0";
    private final int PORT = 6989;
    private ArrayList<MessageListener> listeners;


    public Client() {
        listeners = new ArrayList<>();
        logger = new Logger("JadMBClient");
        if (startConnection()) {
            messageHandler = new MessageProcessor(this,clientSocket);
        } else {
            logger.error("Message handler could not start correctly!");
            logger.error("Shutting down");
            System.exit(400);
        }
    }

    public boolean startConnection() {
        try {
            clientSocket = new Socket(IP,PORT);
        } catch (IOException e) {
            logger.error("Could not connect to server!");
            return false;
        }
        return true;
    }

    public void sendMessage(Message message) throws IOException {
        messageHandler.sendMessage(message);
    }

    public void addMessageListener(MessageListener messageListener) {
        if (!listeners.contains(messageListener)) {
            listeners.add(messageListener);
        }
    }

    public ArrayList<MessageListener> getListeners() {
        return listeners;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {}
}
