package com.game.object;

import com.game.object.util.ObjectID;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block extends GameObject {

    public Block(float x, float y, float width, float height, int scale) {
        super(x, y, ObjectID.BLOCK, width, height, scale);

        // Optimization: Since standard blocks don't move, we calculate the hitbox once.
        // This avoids redundant calculations every tick.
        this.bounds.setBounds((int) x, (int) y, (int) width * scale, (int) height * scale);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        // 1. Fill the block with a solid color (e.g., White)
        g.setColor(Color.WHITE);
        g.fillRect((int)x, (int)y, (int)width, (int)height);

        // 2. Draw the outline (e.g., Red) to visualize boundaries
        g.setColor(Color.RED);
        g.drawRect((int)x, (int)y, (int)width, (int)height);
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }
}
