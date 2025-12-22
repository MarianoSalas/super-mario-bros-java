package com.game.object;

import com.game.object.util.Handler;
import com.game.object.util.ObjectID;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Player extends GameObject {

    private static final float PLAYER_WIDTH = 16;
    private static final float PLAYER_HEIGHT = 32;
    private static final int WIDTH_OFFSET = 5;
    private static final int HEIGHT_OFFSET = 10;

    // Bit flags definitions (powers of 2).
    private static final int STATE_JUMPING = 1; // Binary: 0001
    private static final int STATE_FALLING = 2; // Binary: 0010
    // private static final int STATE_RUNNING = 4; // Binary: 0100 (Future use)
    // Single integer to hold all boolean states using bitwise operations.
    private int stateFlags = 0;

    private static final boolean DEBUG_BOUNDS = true;

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
        // Apply movement.
        this.x += this.velX;
        this.y += this.velY;
        // Apply gravity.
        applyGravity(0.3f);
        // Update collision bounds.
        updateBounds();
        // Handle collisions.
        collision();
    }

    @Override
    public void render(Graphics g) {
        // Debug: Drawing a simple colored rectangle to visualize the player.
        // Once we have sprites/textures, this will be replaced.
        g.setColor(Color.BLUE);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
        if (DEBUG_BOUNDS) { showBounds(g); }
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    public Rectangle getBoundsTop() {
        return this.boundsTop;
    }

    public Rectangle getBoundsLeft() {
        return this.boundsLeft;
    }

    public Rectangle getBoundsRight() {
        return this.boundsRight;
    }

    private void showBounds(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.ORANGE);
        g2d.draw(getBounds());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsTop());
    }

    private void updateBounds() {
        int ix = (int) x;
        int iy = (int) y;
        int iw = (int) width;
        int ih = (int) height;

        this.bounds.setBounds(ix + iw / 4, iy + ih / 2, iw / 2, ih / 2);
        this.boundsTop.setBounds(ix + iw / 4, iy, iw / 2, ih / 2);
        this.boundsRight.setBounds(ix + iw - WIDTH_OFFSET, iy + WIDTH_OFFSET, WIDTH_OFFSET, ih - HEIGHT_OFFSET);
        this.boundsLeft.setBounds(ix, iy + WIDTH_OFFSET, WIDTH_OFFSET, ih - HEIGHT_OFFSET);
    }

    public boolean isJumping() { return (this.stateFlags & STATE_JUMPING) != 0; }

    public void setJumping(boolean active) {
        if (active) {
            this.stateFlags |= STATE_JUMPING;
        } else {
            this.stateFlags &= ~STATE_JUMPING;
        }
    }

    public boolean isFalling() { return (this.stateFlags & STATE_FALLING) != 0; }

    public void setFalling(boolean active) {
        if (active) {
            this.stateFlags |= STATE_FALLING;
        } else {
            this.stateFlags &= ~STATE_FALLING;
        }
    }

    /* OPTIMIZATION: Check if Player is grounded (neither jumping nor falling).
     * This allows checking 2 (or more) states in a SINGLE CPU instruction. */
    public boolean isGrounded() {
        // We check if both bits (1 and 2) are 0.
        return (stateFlags & (STATE_JUMPING | STATE_FALLING)) == 0;
    }

    private void collision() {
        List<GameObject> gameObjectList = this.handler.getGameObjects();
        int gameObjectCount = gameObjectList.size();

        for (int i = 0; i < gameObjectCount; i++) {
            GameObject tempObject = gameObjectList.get(i);

            if (tempObject.id == ObjectID.BLOCK || tempObject.id == ObjectID.PIPE) {
                if (this.bounds.intersects(tempObject.bounds)) {
                    this.y = tempObject.y - this.height;
                    this.velY = 0;
                    this.stateFlags &= ~(STATE_JUMPING | STATE_FALLING); // Clear jumping and falling states.
                    updateBounds();
                    continue;
                }

                if (this.boundsTop.intersects(tempObject.bounds)) {
                    this.y = tempObject.y + this.height;
                    this.velY = 0;
                    updateBounds();
                    continue;
                }

                if (this.boundsRight.intersects(tempObject.bounds)) {
                    this.x = tempObject.x - this.width;
                    updateBounds();
                }

                if (this.boundsLeft.intersects(tempObject.bounds)) {
                    this.x = tempObject.x + tempObject.width;
                    updateBounds();
                }
            }
        }
    }
}
