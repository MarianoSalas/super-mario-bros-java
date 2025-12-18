package com.game.graphics;

import com.game.core.ShutdownHandler;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window {

    public Window(int width, int height, String title, Canvas canvas, ShutdownHandler shutdownHandler) {
        Dimension gameSize = new Dimension(width, height);
        canvas.setPreferredSize(gameSize);
        canvas.setMaximumSize(gameSize);
        canvas.setMinimumSize(gameSize);

        JFrame frame = new JFrame(title);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        //First add the game to the frame, then pack it to fit the preferred size
        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Optionally, request focus for the game canvas
        canvas.requestFocus();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window is closing. Performing shutdown tasks...");
                shutdownHandler.onShutdown(); // Stop game thread
                frame.dispose(); // Kill window
                System.exit(0); // Force kill JVM (optional but ensures full cleanup)
            }
        });
    }
}
