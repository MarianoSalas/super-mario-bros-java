package com.game.graphics;

import com.game.main.Game;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Window {
    private JFrame frame;
    private Dimension size;

    public Window(int width, int height, String title, Game game) {
        this.size = new Dimension(width, height);
        this.frame = new JFrame(title);

        this.frame.setPreferredSize(this.size);
        this.frame.setMaximumSize(this.size);
        this.frame.setMinimumSize(this.size);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.add(game);
        this.frame.setVisible(true);
    }
}
