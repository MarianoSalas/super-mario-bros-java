package com.game.object;

import com.game.object.util.Handler;
import com.game.object.util.ObjectID;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends GameObject {

    private static final float PLAYER_WIDTH = 16;
    private static final float PLAYER_HEIGHT = 32;

    private final Handler handler;

    private final Rectangle boundsTop;
    private final Rectangle boundsLeft;
    private final Rectangle boundsRight;

    public Player(float x, float y, int scale, Handler handler) {
        super(x, y, ObjectID.PLAYER, PLAYER_WIDTH, PLAYER_HEIGHT, scale);
        this.handler = handler;
        this.boundsTop = new Rectangle(0, 0, 0, 0);
        this.boundsLeft = new Rectangle(0, 0, 0, 0);
        this.boundsRight = new Rectangle(0, 0, 0, 0);
    }

    @Override
    public void tick() {
        this.x =+ this.velX;
        this.y =+ this.velY;
        applyGravity(0.5f);
    }

    @Override
    public void render(Graphics g) {
        // Debug: Drawing a simple colored rectangle to visualize the player.
        // Once we have sprites/textures, this will be replaced.
        g.setColor(Color.BLUE);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
        showBounds(g);
    }

    @Override
    public Rectangle getBounds() {
        this.bounds.setBounds((int) x + 5, (int) y, (int) width - 10, (int) height);
        return this.bounds;
    }

    public Rectangle getBoundsTop() {
        this.boundsTop.setBounds((int) x + 10, (int) y, (int) width - 20, (int) height / 2);
        return this.boundsTop;
    }

    public Rectangle getBoundsLeft() {
        this.boundsLeft.setBounds((int) x, (int) y + 5, (int) 5, (int) height - 10);
        return this.boundsLeft;
    }

    public Rectangle getBoundsRight() {
        this.boundsRight.setBounds((int) (x + width - 5), (int) y + 5, (int) 5, (int) height - 10);
        return this.boundsRight;
    }

    private void showBounds(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.RED);
        g2d.draw(getBounds());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsTop());
    }
}
