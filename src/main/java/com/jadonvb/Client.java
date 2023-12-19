package com.jadonvb;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

@Mojo(name = "client")
public class Client extends AbstractMojo {

    private Socket clientSocket;
    private MessageProcessor messageHandler;
    private final Logger logger;
    private final String IP = "192.168.2.17";
    private final int PORT = 6989;
    private ArrayList<MessageListener> listeners;
    private ServerType serverType;


    public Client(ServerType serverType) {
        listeners = new ArrayList<>();
        this.serverType = serverType;
        logger = new Logger("JadMBClient");
        if (startConnection()) {
            messageHandler = new MessageProcessor(this,clientSocket);

            sendInitialMessage();

            logger.log("Started up correctly!");
        } else {
            logger.error("Message handler could not start correctly!");
            logger.error("Shutting down");
            System.exit(400);
        }
    }

    private void sendInitialMessage() {
        Message message = new Message();
        message.setType(MessageTypes.INITIAL_MESSAGE);
        ArrayList<String> arguments = new ArrayList<>();
        if (serverType.equals(ServerType.PROXY)) {
            message.setSender("velocity");
            arguments.add("velocity");
        } else {
            message.setSender(getIP());
            arguments.add(getIP());
        }

        message.setReceiver("MB");

        message.setArguments(arguments);

        sendMessage(message);
    }

    public String getConnected() {
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(clientSocket.getInetAddress()));
        stringBuilder.delete(0,1);

        return stringBuilder.toString();
    }

    public String getIP() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
            String string = String.valueOf(socket.getLocalAddress());
            socket.close();

            StringBuilder stringBuilder = new StringBuilder(string);
            stringBuilder.delete(0,1);

            return stringBuilder.toString();
        } catch (IOException ignore) {}
        return null;
    }

    private boolean startConnection() {
        try {
            clientSocket = new Socket(IP,PORT);
        } catch (IOException e) {
            logger.error("Could not connect to server!");
            return false;
        }
        return true;
    }

    public void sendMessage(Message message) {
        try {
            messageHandler.sendMessage(message);
            //log(message);
        } catch (IOException e) {
            logger.error("Could not send message!");
        }
    }

    private void log(Message message) {
        logger.log("New message: ");
        logger.log("Sender: " + message.getSender());
        logger.log("Receiver: " + message.getReceiver());
        logger.log(message.getType().toString());
        try {
            logger.log(message.getArguments().get(0));
        } catch (NullPointerException ignored){}
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
