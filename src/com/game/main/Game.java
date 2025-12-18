package com.game.main;

import com.game.graphics.Window;

import java.awt.Canvas;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    //Game Constants
    private static final int MILLIS_PER_SECOND = 1_000;
    private static final int NANOS_PER_SECOND = 1_000_000_000;
    private static final double NUM_TICKS = 60.0;
    private static final String TITLE = "Super Mario Bros";

    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 720;
    //Game Variables
    private volatile boolean running;
    //Game Components
    private Thread thread;

    public Game() {
        initialize();
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

    private void initialize() {
        new Window(this.WINDOW_WIDTH, this.WINDOW_HEIGHT, this.TITLE, this);

        start();
    }

    private synchronized void start() {
        if (running) return; // Prevención extra por si se llama dos veces
        this.thread = new Thread(this);
        this.running = true;
        this.thread.start();
    }

    private synchronized void stop() {
        if (!running) return;
        try {
            this.thread.join();
            this.running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = NANOS_PER_SECOND / NUM_TICKS;
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
        stop();
    }

    private void tick() {

    }

    private void render() {
        BufferStrategy buffer = this.getBufferStrategy();

        if (buffer == null) {
            this.createBufferStrategy(3);
        }

        //Draw graphics

    }
}
