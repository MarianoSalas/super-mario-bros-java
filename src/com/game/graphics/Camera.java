package com.game.graphics;

import com.game.object.GameObject;

public class Camera {
    private int x, y;

    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick(GameObject player) {
        this.x = (int) -player.getX();
    }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
}