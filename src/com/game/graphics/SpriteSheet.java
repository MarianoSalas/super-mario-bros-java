package com.game.graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage sheet;

    public SpriteSheet(BufferedImage sheet) {
        this.sheet = sheet;
    }

    /**
     * Crops a portion of the sprite sheet.
     * @param col Column (1, 2, 3, ...) (1-indexed)
     * @param row Row (1, 2, 3, ...) (1-indexed)
     * @param width Sprite width (16 or 32)
     * @param height Sprite height (16 or 32)
     * @return The cropped sprite as a BufferedImage.
     */
    public BufferedImage grabImage(int col, int row, int width, int height) {
        /* Math to convert grid position to pixel position.
         * We subtract 1 because the grid is 1-indexed but pixel positions are 0-indexed.
         * (col * 16) - 16 is equivalent to (col - 1) * 16 */
        int x = (col * 16) - 16;
        int y = (row * 16) - 16;
        return sheet.getSubimage(x, y, width, height);
    }
}
