package com.game.main;

import com.game.core.ShutdownHandler;
import com.game.graphics.Window;
import com.game.object.util.Handler;

import java.awt.*;
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

    //Game Components
    private Thread thread;
    private Handler handler;

    public Game() {
        this.handler = new Handler();
    }

    public static void main(String[] args) {
        Game game = new Game();

        new Window(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT, Game.TITLE, game, game);

        game.start();
    }

    private synchronized void start() {
        if (running) return; // Prevención extra por si se llama dos veces
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

            if (System.currentTimeMillis() - timer > MILLIS_PER_SECOND) {
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

        //Clean for next frame
        g.dispose();
        buffer.show();
    }

    @Override
    public void onShutdown() {
        stop();
    }
}
