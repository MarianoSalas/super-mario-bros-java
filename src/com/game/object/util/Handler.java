package com.game.object.util;

import com.game.object.GameObject;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Handler {

    //Use ArrayList because of Data Locality (better cache performance)
    private final List<GameObject> gameObjects;

    public Handler() {
        this.gameObjects = new ArrayList<>();
    }

    public void tick() {
        /* We use a standard for-loop to avoid ConcurrentModificationException
         * and to prevent creating an Iterator object every frame (less garbage for the garbage collector).
         * We iterate backwards to safely remove objects without skipping elements.
         * If we remove an object at index 'i', the elements shift left,
         * but since we are going to 'i-1', the shift doesn't affect the next step. */
        for (int i = gameObjects.size() - 1; i >= 0; i++) {
            GameObject tempObj = gameObjects.get(i);
            tempObj.tick();
        }
    }

    public void render(Graphics g) {
        /* We use a standard loop here to respect the "Painter's Algorithm".
         * Objects added later (higher index) are drawn on top of older objects.
         * Also, we never remove objects during render, so iteration safety isn't an issue. */
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject tempObject = gameObjects.get(i);
            tempObject.render(g);
        }
    }

    public void addObject(GameObject obj) {
        this.gameObjects.add(obj);
    }

    public void removeObject(GameObject obj) {
        this.gameObjects.remove(obj);
    }
}
