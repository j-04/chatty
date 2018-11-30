package com.app.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;


public class EnterInterface extends JFrame {

    private final int WIDTH = 320;
    private final int HEIGHT = 480;

    private JButton button = null;
    private JTextField userName_Field = null;
    private JTextField ip_Field = null;
    private JTextField port_Field = null;

    private JLabel userName_Label = null;
    private JLabel ip_Label = null;
    private JLabel port_Label = null;

    public EnterInterface(){

    }

    public void startMenu(){
        setupWindow();
    }

    private void setupWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setTitle("Вход");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation((width-WIDTH)/2, (height - HEIGHT)/2);
        setResizable(false);
        setLayout(null);
        setVisible(true);

        button = new JButton("Зайти на сервер");
        button.setSize(140,30);
        button.setLocation((WIDTH - button.getSize().width)/2, HEIGHT - HEIGHT/4-20);
        button.addActionListener(e -> {
            String ip = ip_Field.getText();
            int port;
            try {
                port = Integer.parseInt(port_Field.getText());
            }catch(NumberFormatException ex){
                System.out.println("Неверный формат поля port!");
                return;
            }
            Socket socket;
            try {
                socket = new Socket(ip, port);
            } catch (IOException e1) {
                System.err.println("Ошибка подключения к серверу!");
                return;
            }
            Client client = new Client(socket, userName_Field.getText());
            dispose();
            Socket finalSocket = socket;

            ChatInterface chatInterface = new ChatInterface(finalSocket, client);
            chatInterface.startMenu();


        });
        button.setVisible(true);
        add(button);

        userName_Field = new JTextField();
        userName_Field.setSize(140,30);
        userName_Field.setLocation((WIDTH - userName_Field.getSize().width)/2, button.getY()-280);
        userName_Field.setVisible(true);
        add(userName_Field);

        ip_Field = new JTextField();
        ip_Field.setSize(140,30);
        ip_Field.setLocation((WIDTH - ip_Field.getSize().width)/2, button.getY()-190);
        ip_Field.setVisible(true);
        add(ip_Field);

        port_Field = new JTextField();
        port_Field.setSize(140,30);
        port_Field.setLocation((WIDTH - port_Field.getSize().width)/2, button.getY()-100);
        add(port_Field);

        userName_Label = new JLabel("Введите свое имя:");
        userName_Label.setSize(140,30);
        userName_Label.setLocation((WIDTH - userName_Label.getSize().width)/2, button.getY()-325);
        add(userName_Label);

        ip_Label = new JLabel("Введите ip(ex: 192.168.0.11):");
        ip_Label.setSize(170,30);
        ip_Label.setLocation((WIDTH - ip_Field.getSize().width)/2, button.getY()-235);
        add(ip_Label);

        port_Label = new JLabel("Введите порт(ex: 7777):");
        port_Label.setSize(150,30);
        port_Label.setLocation((WIDTH - port_Field.getSize().width)/2, button.getY()-145);
        add(port_Label);
    }
}
