package com.game.main;

import com.game.core.ShutdownHandler;
import com.game.graphics.Camera;
import com.game.graphics.Window;
import com.game.object.Block;
import com.game.object.Player;
import com.game.object.util.Handler;
import com.game.object.util.KeyInput;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.image.BufferStrategy;
import java.io.Serial;

public class Game extends Canvas implements Runnable, ShutdownHandler {

    // Constants
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int MILLIS_PER_SECOND = 1_000;
    private static final int NANOS_PER_SECOND = 1_000_000_000;
    private static final int NUM_TICKS = 60;
    private static final String TITLE = "Super Mario Bros";

    // Window Dimensions
    public static final int WINDOW_WIDTH = 960;
    public static final int WINDOW_HEIGHT = 720;

    // World Constants
    private static final int BLOCK_SIZE = 32;

    // Game Variables
    private volatile boolean running;
    private String fpsString = "FPS: 0"; // Cache to avoid creating new strings every frame.

    // Game Components
    private Thread thread;
    private final Handler handler;
    private final Camera camera;

    public Game() {
        // Initialize Core Components
        this.handler = new Handler();
        this.camera = new Camera(0, 0, WINDOW_WIDTH, 3000, null);

        // Setup Input
        this.addKeyListener(new KeyInput(this.handler));

        // Initialize Level
        initLevel();
    }

    private void initLevel() {
        // Create Floor (30 blocks wide)
        for (int i = 0; i < 30; i++) {
            this.handler.addObject(new Block(i * BLOCK_SIZE, 500, BLOCK_SIZE, BLOCK_SIZE, 1));
        }

        // Create Floating Platforms
        this.handler.addObject(new Block(300, 355, BLOCK_SIZE, BLOCK_SIZE, 1));
        this.handler.addObject(new Block(332, 355, BLOCK_SIZE, BLOCK_SIZE, 1));
        this.handler.addObject(new Block(364, 355, BLOCK_SIZE, BLOCK_SIZE, 1));

        // Create Wall
        this.handler.addObject(new Block(600, 468, BLOCK_SIZE, BLOCK_SIZE, 1));
        this.handler.addObject(new Block(600, 436, BLOCK_SIZE, BLOCK_SIZE, 1));

        // Create Player
        Player player = new Player(100, 100, 2, this.handler);
        player.setCamera(this.camera);

        // Link Camera to Player
        this.camera.setPlayer(player);

        this.handler.addObject(player);
    }
    /* ------------------------------------------MAIN METHOD------------------------------------------ */
    public static void main(String[] args) {
        Game game = new Game();
        new Window(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, Game.TITLE, game, game);
        game.start();
    }

    private synchronized void start() {
        if (running) return; // Extra prevention in case of multiple calls.
        this.running = true;
        this.thread = new Thread(this, "Game-Thread");
        this.thread.start();
        this.requestFocus();
    }

    private synchronized void stop() {
        if (!running) return;
        this.running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = NANOS_PER_SECOND / (double) NUM_TICKS;
        double delta = 0;

        long timer = System.currentTimeMillis();
        int frames = 0; //Render count
        int updates = 0; //Tick count

        while (this.running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            boolean shouldRender = false;

            while (delta >= 1) {
                tick();
                updates++;
                delta--;
                shouldRender = true;
            }

            if (shouldRender) {
                render();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (System.currentTimeMillis() - timer >= MILLIS_PER_SECOND) {
                this.fpsString = "FPS: " + frames + " | TPS: " + updates;
                timer += MILLIS_PER_SECOND;
                //System.out.println(this.fpsString);
                frames = 0;
                updates = 0;
            }
        }
    }

    private void tick() {
        this.handler.tick();
        this.camera.tick();
    }

    private void render() {
        BufferStrategy buffer = this.getBufferStrategy();

        if (buffer == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = buffer.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        // Background (clean screen)
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        // World Rendering
        g2d.translate(-this.camera.getX(), -this.camera.getY());
        this.handler.render(g);
        g2d.translate(this.camera.getX(), this.camera.getY());
        // UI Rendering
        drawUI(g);
        //Clean for next frame
        g.dispose();
        buffer.show();
    }

    private void drawUI(Graphics g) {
        // Shadow
        g.setColor(Color.BLACK);
        g.drawString(this.fpsString, 11, 21);
        // Text
        g.setColor(Color.GREEN);
        g.drawString(this.fpsString, 10, 20);
    }

    @Override
    public void onShutdown() {
        stop();
    }
}
