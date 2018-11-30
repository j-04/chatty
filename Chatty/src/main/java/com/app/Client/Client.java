package com.app.Client;

import java.net.Socket;


//Сделать из этого класса наследником THREAD и просто брать потоки IO из этого Thread
public class Client{
    private String name;
    private String ip;

    public Client(Socket socket, String name){

        this.name = name;
        this.ip = socket.getInetAddress().getHostAddress();
    }

    public String getName() {
        return name;
    }

    public String getIp(){
        return  ip;
    }
}
