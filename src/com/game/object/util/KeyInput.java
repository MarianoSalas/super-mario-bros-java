package com.game.object.util;

import com.game.object.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private final Handler handler;

    /* Logic: We track the physical state of the keys (W, A, S, D).
     * true = pressed, false = released.
     * [0]=W (Up), [1]=A (Left), [2]=S (Down), [3]=D (Right) */
    private final boolean [] keyDown = new boolean[4];

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // Optimization: Direct access to Player [O(1)] instead of iterating the list [O(n)].
        Player player = this.handler.getPlayer();
        // Safety check: If the player is dead or hasn't spawned, we ignore input.
        if (player == null) return;

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            player.jump();
            keyDown[0] = true;
        }

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            player.setVelXByDirection(-1);
            keyDown[1] = true;
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            // Usually used for crouching or entering pipes
            // player.setVelY(5);
            keyDown[2] = true;
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            player.setVelXByDirection(1);
            keyDown[3] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        Player player = this.handler.getPlayer();

        if (player == null) return;

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            keyDown[0] = false;
            /* GAMEPLAY MECHANIC: Variable Jump Height
             * If the player releases the jump button while still moving up,
             * we cut the upward velocity significantly to create a "short hop". */
            if (player.getVelY() < 0) {
                player.setVelY(player.getVelY() * 0.5f);
            }
        }

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            keyDown[1] = false;
            // Logic: If we release 'Left', but 'Right' is still held down,
            // we should start moving Right instantly instead of stopping.
            if (keyDown[3]) {
                player.setVelXByDirection(1);
            }
            else {
                player.setVelXByDirection(0);
            }
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) { keyDown[2] = false; }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            keyDown[3] = false;
            // Logic: If we release 'Right', but 'Left' is still held down, move left.
            if (keyDown[1]) {
                player.setVelXByDirection(-1);
            }
            else {
                player.setVelXByDirection(0);
            }
        }
    }
}
