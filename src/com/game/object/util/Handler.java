package com.game.object.util;

import com.game.object.GameObject;
import com.game.object.Player;

import java.awt.Graphics;

import java.util.ArrayList;
import java.util.List;

public class Handler {

    // Use ArrayList because of Data Locality (better cache performance)
    private final List<GameObject> gameObjects;
    // We store a specific reference to Player to avoid O(n) searches during input/camera logic.
    private Player player;

    public Handler() {
        this.gameObjects = new ArrayList<>();
    }

    public void tick() {
        /* We use a standard for-loop to avoid ConcurrentModificationException
         * and to prevent creating an Iterator object every frame (less garbage for the garbage collector).
         * We iterate backwards to safely remove objects without skipping elements.
         * If we remove an object at index 'i', the elements shift left,
         * but since we are going to 'i-1', the shift doesn't affect the next step. */
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
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

        // CORRECCIÓN: Si el objeto es un Player, guardamos la referencia rápida.
        if (obj.getID() == ObjectID.PLAYER) {
            this.player = (Player) obj;
        }
    }

    public void removeObject(GameObject obj) {
        this.gameObjects.remove(obj);

        // Safety check: If we are removing the object that happens to be the player,
        // we must also nullify the direct reference to avoid keeping a "ghost" player.
        if (this.player == obj) {
            this.player = null;
        }
    }

    public List<GameObject> getGameObjects() { return this.gameObjects; }

    public boolean setPlayer(Player player) {
        boolean toReturn;

        if (this.player != null) {
            toReturn = false;
        }
        else {
            addObject(player);
            this.player = player;
            toReturn = true;
        }

        return toReturn;
    }

    public boolean removePlayer() {
         boolean toReturn;

        if (this.player == null) {
            toReturn = false;
        }
        else {
            removeObject(this.player);
            this.player = null;
            toReturn = true;
        }
        return toReturn;
    }

    public Player getPlayer() {
        return this.player;
    }
}
