package com.game.graphics;

import com.game.object.util.PlayerState;

import java.util.Map;
import java.util.HashMap;

import java.awt.image.BufferedImage;

public class AnimationManager {
    private final Map<PlayerState, Animation> animations;

    public AnimationManager() {
        this.animations = new HashMap<>();
    }

    public void addAnimation(PlayerState state, Animation animation) {
        this.animations.put(state, animation);
    }

    public void update() {
        // Update all animations (ticks the frames)
        for (Animation animation : animations.values()) {
            animation.runAnimation();
        }
    }

    public BufferedImage getFrame(PlayerState state) {
        // Return the current frame of the requested state
        Animation animation = animations.get(state);
        if (animation != null) { return animation.getCurrentFrame(); }
        return null; // Handle error or return default sprite.
    }
}
