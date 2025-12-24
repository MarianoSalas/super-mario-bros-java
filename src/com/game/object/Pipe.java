package com.game.object;

import com.game.object.util.ObjectID;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public class Pipe extends GameObject {

    private boolean enterable;

    public Pipe(float x, float y, float width, float height, int scale, boolean enterable) {
        super(x, y, ObjectID.PIPE, width, height, scale);
        this.enterable = enterable;
        // width and height already scaled (in GameObject constructor).
        this.bounds.setBounds((int) this.x, (int) this.y, (int) this.width, (int) this.height);
    }

    @Override
    public void tick() {
        // Static object.
    }

    @Override
    public void render(Graphics g) {
        // Visual placeholder: Classic green pipe.
        g.setColor(new Color(0, 180, 0));
        g.fillRect((int)x, (int)y, (int)width, (int)height);

        // Border to distinguish edges.
        g.setColor(Color.BLACK);
        g.drawRect((int)x, (int)y, (int)width, (int)height);
    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }

    public boolean isEnterable() { return this.enterable; }
}
