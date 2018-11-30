package com.app.Server;

import java.io.*;
import java.net.Socket;

public class ServerUser {

    private String name;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private int reConnectCounter = 10;

    public ServerUser(Socket socket){
        this.socket = socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public String getName() {
        return name;
    }

    public int getReConnectCounter() {
        return reConnectCounter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReConnectCounter(int reConnectCounter) {
        this.reConnectCounter = reConnectCounter;
    }

}
