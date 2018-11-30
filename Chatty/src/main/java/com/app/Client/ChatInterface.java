package com.app.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ChatInterface extends JFrame {

    private final int WIDTH = 880;
    private final int HEIGHT = 550;

    private JTextArea textArea;
    private JTextField textField;

    private Socket socket;
    private Client client;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ChatInterface(Socket socket, Client client){
        this.socket = socket;
        this.client = client;
        setupWindow();
    }

    public void startMenu(){
        setupStreams();
    }

    private void setupStreams(){
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(client.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        listenForInput();

    }

    private void listenForInput(){

        new Thread(() -> {
            String text;
            while(socket.isConnected()){
                try {
                    text = dataInputStream.readUTF();
                    textArea.append(text + "\n");
                } catch (IOException e) {
                    break;
                }
            }
        }).start();
    }

    private void setupWindow(){

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle(client.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{16, 857, 7};
        gbl_contentPane.columnWidths = new int[]{16, 827, 30, 7};
        gbl_contentPane.rowHeights = new int[]{35, 475, 40};
        gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
        gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(textArea);

        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.insets = new Insets(0, 0, 5, 5);
        scrollConstraints.fill = GridBagConstraints.BOTH;
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;
        scrollConstraints.gridwidth = 3;
        scrollConstraints.gridheight = 2;
        scrollConstraints.insets = new Insets(0, 7, 0, 0);
        contentPane.add(scroll, scrollConstraints);

        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    sendMessage();
                }
            }
        });

        GridBagConstraints gbc_txtMessage = new GridBagConstraints();
        gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
        gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtMessage.gridx = 0;
        gbc_txtMessage.gridy = 2;
        gbc_txtMessage.gridwidth = 2;
        contentPane.add(textField, gbc_txtMessage);
        textField.setColumns(10);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(e -> sendMessage());

        GridBagConstraints gbc_btnSend = new GridBagConstraints();
        gbc_btnSend.insets = new Insets(0, 0, 0, 5);
        gbc_btnSend.gridx = 2;
        gbc_btnSend.gridy = 2;
        contentPane.add(btnSend, gbc_btnSend);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    System.out.println("Socket is closed!");
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        setVisible(true);

        textField.requestFocusInWindow();
    }

    private void sendMessage(){
        try {
            if(!textField.getText().equals("")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MM.yyyy HH:mm:ss");
                String message = "[" + simpleDateFormat.format(new Date()) + "] " + client.getName() + ": " + textField.getText();
                dataOutputStream.writeUTF(message);
                textArea.append(message + "\n");
                textField.setText("");
            }
        } catch (IOException e1) {
            return;
        }
    }
}
