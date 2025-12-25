package com.game.graphics;

import java.awt.image.BufferedImage;

public class Animation {
    private int speed;
    private int frames; // Index of current frame.
    private int index = 0;
    private int count = 0;

    private BufferedImage[] images;
    private BufferedImage currentImage;

    // Constructor: receives speed (ms per frame) and the array of images.
    public Animation(int speed, BufferedImage... args) {
        this.speed = speed;
        this.images = new BufferedImage[args.length];
        for (int i = 0; i < args.length; i++) {
            this.images[i] = args[i];
        }
        this.frames = args.length;
    }

    public void runAnimation() {
        index++;
        if (index > speed) {
            index = 0;
            nextFrame();
        }
    }

    private void nextFrame() {
        for (int i = 0; i < frames; i++) {
            if (count == i) {
                currentImage = images[i];
            }
        }
        count++;
        if (count >= frames) {
            count = 0;
        }
    }

    public BufferedImage getCurrentFrame() {
        // Safety check to avoid null pointer if runAnimation hasn't been called yet.
        if (currentImage == null) { return images[0]; }
        return currentImage;
    }
}
