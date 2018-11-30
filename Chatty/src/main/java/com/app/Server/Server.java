package com.app.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Server {

    private LinkedList<ServerUser> users = new LinkedList<>();
    private LinkedList<String> messages = new LinkedList<>();

    public void startServer(){

        System.out.println("СЕРВЕР ЗАПУЩЕН...");

        ServerSocket serverSocket = null;

        try {
           serverSocket = new ServerSocket(7777);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                System.out.println("ОЖИДАНИЕ НОВОГО ПОЛЬЗОВАТЕЛЯ...\n");
                Socket socket = serverSocket.accept();
                System.out.println("ПОДКЛЮЧИЛСЯ НОВЫЙ ПОЛЬЗОВАТЕЛЬ!\n");
                new Thread(() -> userManager(socket)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void userManager(Socket socket){

        ServerUser serverUser = new ServerUser(socket);
        users.add(serverUser);

       try {
            String name = serverUser.getDataInputStream().readUTF();
            serverUser.setName(name);
       } catch (IOException e) {
            e.printStackTrace();
       }

        messages.stream().forEach(m -> {
            try {
                serverUser.getDataOutputStream().writeUTF(m);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        boolean connect = true;
        while(connect){
            String message = "";
            try {
                message = serverUser.getDataInputStream().readUTF();
            } catch (IOException e) {
                connect = false;
            }

            if(!message.equals("")) {

                if (messages.size() != 10) {
                    messages.add(message);
                } else {
                    messages.remove(0);
                    messages.add(message);
                }

                for (ServerUser m : users) {
                    try {
                        if (!m.equals(serverUser))
                            m.getDataOutputStream().writeUTF(message);
                    } catch (IOException e) {
                        connect = false;
                    }
                }
            }
        }

        try {
            serverUser.getDataInputStream().close();
            serverUser.getDataOutputStream().close();
            serverUser.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("ПОЛЬЗОВАТЕЛЬ " + serverUser.getName() + " " + serverUser.getSocket().getInetAddress().getHostAddress() + " отключился.");
        users.remove(serverUser);
        System.out.println("КОЛИЧЕСТВО ПОЛЬЗОВАТЕЛЕЙ В ЧАТЕ: " + users.size() + "\n");

        users.stream().forEach(user->{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MM.yyyy HH:mm:ss");
            String message = "[" + simpleDateFormat.format(new Date()) + "] " + serverUser.getName() + " "  + serverUser.getSocket().getInetAddress().getHostAddress() + " disconnected!";
            try {
                user.getDataOutputStream().writeUTF(message);
            } catch (IOException e) {
               e.printStackTrace();
            }
       });

    }
}
