package com.game.main;

import com.game.core.ShutdownHandler;
import com.game.graphics.Window;
import com.game.object.Block;
import com.game.object.Player;
import com.game.object.util.Handler;
import com.game.object.util.KeyInput;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable, ShutdownHandler {

    //Game Constants
    private static final int MILLIS_PER_SECOND = 1_000;
    private static final int NANOS_PER_SECOND = 1_000_000_000;
    private static final int NUM_TICKS = 60;
    private static final String TITLE = "Super Mario Bros";

    public static final int WINDOW_WIDTH = 960;
    public static final int WINDOW_HEIGHT = 720;

    //Game Variables
    private volatile boolean running;
    private int currentFPS = 0;

    //Game Components
    private Thread thread;
    private final Handler handler;

    public Game() {
        this.handler = new Handler();
        this.addKeyListener(new KeyInput(this.handler));

        // We create a row of blocks at Y = 500
        // 32 pixels wide * 20 blocks = 640 pixels of floor
        for (int i = 0; i < 30; i++) {
            this.handler.addObject(new Block(i * 32, 500, 32, 32, 1));
        }
        // Create a floating platform to test Jumping logic
        this.handler.addObject(new Block(300, 400, 32, 32, 1));
        this.handler.addObject(new Block(332, 400, 32, 32, 1));
        this.handler.addObject(new Block(364, 400, 32, 32, 1));

        // Create a wall to test Side Collision
        this.handler.addObject(new Block(600, 468, 32, 32, 1));
        this.handler.addObject(new Block(600, 436, 32, 32, 1));

        // Add Player AFTER blocks so he is rendered in front of them
        this.handler.addObject(new Player(100, 100, 2, this.handler));
    }

    public static void main(String[] args) {
        Game game = new Game();

        new Window(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, Game.TITLE, game, game);

        game.start();
    }

    private synchronized void start() {
        if (running) return; // Extra prevention in case of multiple calls.
        this.thread = new Thread(this, "Game-Thread");
        this.running = true;
        this.thread.start();
    }

    private synchronized void stop() {
        if (!running) return;
        try {
            this.running = false;
            this.thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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

            if (this.running && shouldRender) {
                render();
                frames++;
            }

            if (this.running && !shouldRender) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (System.currentTimeMillis() - timer >= MILLIS_PER_SECOND) {
                this.currentFPS = frames;
                timer += MILLIS_PER_SECOND;
                System.out.println("FPS: " + frames + " | TPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    private void tick() {
        this.handler.tick();
    }

    private void render() {
        BufferStrategy buffer = this.getBufferStrategy();

        if (buffer == null) {
            this.createBufferStrategy(3);
            return;
        }

        //Draw g
        Graphics g = buffer.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        this.handler.render(g);
        drawFPS(g);
        //Clean for next frame
        g.dispose();
        buffer.show();
    }

    private void drawFPS(Graphics g) {
        // OPTIMIZACIÓN VISUAL: "Sombra" negra para que se lea sobre fondo blanco
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + currentFPS, 11, 21); // Un pixel desplazado

        // Texto principal (verde flúo o blanco para contraste)
        g.setColor(Color.GREEN);
        g.drawString("FPS: " + currentFPS, 10, 20);
    }

    @Override
    public void onShutdown() {
        stop();
    }
}
