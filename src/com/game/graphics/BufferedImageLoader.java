package com.game.graphics;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class BufferedImageLoader {
    public BufferedImage loadImage(String path) {
        BufferedImage image = null;
        // Load image from the specified path
        URL url = getClass().getResource(path);

        if (url == null) {
            System.err.println("Error: Source not found at path: " + path);
            System.exit(1);
        }

        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }
}
