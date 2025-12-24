package com.game.graphics;

import com.game.object.GameObject;

public class Camera {
    private float x, y;
    private final int screenWidth;
    private final float halfScreenWidth;
    private final int levelWidth;
    private GameObject player;

    public Camera(float x, float y, int screenWidth, int levelWidth, GameObject player) {
        this.x = x;
        this.y = y;
        this.screenWidth = screenWidth;
        this.halfScreenWidth = screenWidth * 0.5f;
        this.levelWidth = levelWidth;
        this.player = player;
    }

    public void tick() {
        if (this.player == null) { return; }
        // Calculate target position (centering the player)
        float targetX = player.getX() - this.halfScreenWidth + (player.getWidth() * 0.5f);
        // Rule: Camera only moves forward (Right)
        if (targetX > this.x) {
            this.x = targetX;
        }
        // Clamp: Left bound
        if (this.x < 0) { this.x = 0; }
        // Clamp: Right bound (Level end)
        if (this.x > this.levelWidth - this.screenWidth) {
            this.x = this.levelWidth - this.screenWidth;
        }

        this.y = 0; // For now, we keep Y static. Future: Implement vertical camera movement if needed.
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setPlayer(GameObject player) { this.player = player; }
    public float getX() { return this.x; }
    public float getY() { return this.y; }
}