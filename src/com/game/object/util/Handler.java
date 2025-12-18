package com.game.object.util;

import com.game.object.GameObject;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

public class Handler {
    private List<GameObject> gameObjects;

    public Handler() {
        this.gameObjects = new LinkedList<>();
    }

    public void tick() {
        for (GameObject obj : gameObjects) {
            obj.tick();
        }
    }

    public void render(Graphics g) {
        for (GameObject obj : gameObjects) {
            obj.render(g);
        }
    }

    public void addObject(GameObject obj) {
        this.gameObjects.add(obj);
    }

    public void removeObject(GameObject obj) {
        this.gameObjects.remove(obj);
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }
}
