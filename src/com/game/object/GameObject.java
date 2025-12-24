package com.game.object;

import com.game.object.util.ObjectID;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
    protected float x, y;
    protected ObjectID id;
    protected float velX, velY;
    protected float width, height;
    protected final int scale;

    // OPTIMIZATION: We create the rectangle ONCE in memory.
    protected final Rectangle bounds;

    public GameObject(float x, float y, ObjectID id, float width, float height, int scale) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.width = width * scale;
        this.height = height * scale;
        this.scale = scale;
        // Initialization: We create the object once in memory.
        // We initialize it with 0,0,0,0 because the specific size will be set by the subclass.
        this.bounds = new Rectangle(0, 0, 0, 0);
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    // Used for collision detection. Caution: Implementations should avoid 'new Rectangle()' if possible.
    public abstract Rectangle getBounds();

    public void applyGravity(float gravity) { this.velY += gravity; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setID(ObjectID id) { this.id = id; }
    public void setVelX(float velX) { this.velX = velX; }
    public void setVelY(float velY) { this.velY = velY; }
    public void setWidth(float width) { this.width = width; }
    public void setHeight(float height) { this.height = height; }

    public float getX() { return this.x; }
    public float getY() { return y; }
    public ObjectID getID() { return this.id; }
    public float getVelX() { return this.velX; }
    public float getVelY() { return this.velY; }
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
}