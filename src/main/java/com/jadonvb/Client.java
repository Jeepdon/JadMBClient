package com.jadonvb;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Mojo(name = "client")
public class Client extends AbstractMojo {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final Logger logger;
    private final String name;

    private final String IP = "0";
    private final int PORT = 6989;


    public Client(String name) {
        this.name = name;
        logger = new Logger("JadMBClient");
        if (startConnection()) {
            new MessageHandler(clientSocket, name);
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


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {}
}
