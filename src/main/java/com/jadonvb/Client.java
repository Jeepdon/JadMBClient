package com.jadonvb;

import com.jadonvb.enums.MessageType;
import com.jadonvb.enums.ServerType;
import com.jadonvb.messages.Message;
import com.jadonvb.messages.MessageListener;
import com.jadonvb.messages.MessageProcessor;
import com.jadonvb.misc.ShutdownHook;
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
    private final ArrayList<MessageListener> listeners;
    private final ServerType serverType;
    private String name;


    public Client(ServerType serverType) {

        this.serverType = serverType;

        listeners = new ArrayList<>();
        logger = new Logger("JadMBClient");

        if (startConnection()) {
            messageHandler = new MessageProcessor(this,clientSocket);

            if (serverType.equals(ServerType.PROXY)) {
                name = "velocity";
            } else {
                name = getIP();
            }

            sendInitialMessage();

            Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));

            logger.log("Started up correctly!");
        } else {
            logger.error("Message handler could not start correctly!");
            logger.error("Shutting down");
            System.exit(400);
        }
    }

    private void sendInitialMessage() {

        Message message = new Message();
        message.setType(MessageType.INITIAL_MESSAGE);
        message.setReceiver("MB");

        // Arguments
        ArrayList<String> arguments = new ArrayList<>();
        message.setSender(getName());
        arguments.add(getName());
        arguments.add(serverType.toString());
        message.setArguments(arguments);

        sendMessage(message);
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

    public void sendMessage(Message message) {
        try {
            messageHandler.sendMessage(message);
        } catch (IOException e) {
            logger.error("Could not send message!");
        }
    }

    public void addMessageListener(MessageListener messageListener) {
        if (!listeners.contains(messageListener)) {
            listeners.add(messageListener);
        }
    }

    public ArrayList<MessageListener> getListeners() {
        return listeners;
    }

    public String getName() {
        return name;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {}
}
