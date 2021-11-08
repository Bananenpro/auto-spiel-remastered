package de.julianhofmann.autospiel.world;

import java.awt.image.BufferedImage;

public class Biome {
    private final String name;
    private final double friction;
    private final BufferedImage background;
    private final BufferedImage obstacle;
    private final BufferedImage coin;

    public Biome(String name, double friction, BufferedImage background, BufferedImage obstacle, BufferedImage coin) {
        this.name = name;
        this.friction = friction;
        this.background = background;
        this.obstacle = obstacle;
        this.coin = coin;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public BufferedImage getObstacle() {
        return obstacle;
    }

    public double getFriction() {
        return friction;
    }

    public BufferedImage getCoin() {
        return coin;
    }
}
