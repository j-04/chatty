package com.app;

import com.app.Client.EnterInterface;

import javax.swing.*;

public class MainClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            EnterInterface enterInterface = new EnterInterface();
            enterInterface.startMenu();
        });

    }
}
