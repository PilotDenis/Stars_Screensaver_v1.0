package com.ball;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        // Run GUI codes in Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StarsFrame(); // Let the constructor do the job
            }
        });
    }
}