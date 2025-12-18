package com.game.graphics;

import com.game.main.Game;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Window {

    public Window(int width, int height, String title, Game game) {
        Dimension gameSize = new Dimension(width, height);
        game.setPreferredSize(gameSize);
        game.setMaximumSize(gameSize);
        game.setMinimumSize(gameSize);

        JFrame frame = new JFrame(title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        //First add the game to the frame, then pack it to fit the preferred size
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Optionally, request focus for the game canvas
        game.requestFocus();
    }
}
