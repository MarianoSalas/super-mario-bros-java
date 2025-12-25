package com.game.graphics;

import java.awt.image.BufferedImage;

public class Assets {
    // Arrays to save animations
    public static BufferedImage[] player_small_idle;
    public static BufferedImage[] player_small_run;
    public static BufferedImage[] player_small_jump;
    // I'll add the rest later (swim, climb, big Mario, etc.) TODO

    // Standard Dimensions
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;

    public static void init() {
        // 1. Load the whole sprite sheet
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage sheetImage = loader.loadImage("/textures/mario_sheet.png");
        SpriteSheet spriteSheet = new SpriteSheet(sheetImage);

        // 2. Initialize arrays (size based on number of frames)
        player_small_idle = new BufferedImage[1]; // 1 frame for idle
        player_small_run = new BufferedImage[3];  // 3 frames for running
        player_small_jump = new BufferedImage[1]; // 1 frame for jumping

        // 3. Crop sprites from the sheet
        /* -----------Small Mario Normal (Row 1)----------- */
        // Idle (Column 1, Row 1)
        player_small_idle[0] = spriteSheet.grabImage(1, 1, WIDTH, HEIGHT);

        // Running (Columns 2, 3, 4; Row 1)
        player_small_run[0] = spriteSheet.grabImage(2, 1, WIDTH, HEIGHT);
        player_small_run[1] = spriteSheet.grabImage(3, 1, WIDTH, HEIGHT);
        player_small_run[2] = spriteSheet.grabImage(4, 1, WIDTH, HEIGHT);

        // Jumping (Column 5, Row 1)
        player_small_jump[0] = spriteSheet.grabImage(5, 1, WIDTH, HEIGHT);
    }
}
