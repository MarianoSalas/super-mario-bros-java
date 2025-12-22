package com.game.object;

import com.game.object.util.ObjectID;

import java.awt.Rectangle;
import java.awt.Graphics;

public class Pipe extends GameObject {

    private boolean enterable;

    public Pipe(float x, float y, float width, float height, int scale, boolean enterable) {
        super(x, y, ObjectID.PIPE, width, height, scale);
        this.enterable = enterable;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public Rectangle getBounds() {
        return this.bounds;
    }
}
