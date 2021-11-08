package de.julianhofmann.autospiel.world;

import java.awt.*;

public class GameObject {
    protected double x;
    protected double y;
    protected int width;
    protected int height;


    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean collidesWith(GameObject otherObject) {
        return new Rectangle((int)Math.round(x), (int)Math.round(y), width, height).intersects(otherObject.getX(), otherObject.getY(), otherObject.getWidth(), otherObject.getHeight());
    }

    public void update(double deltaTime) {}

    public void draw(Graphics2D g2d) {}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
