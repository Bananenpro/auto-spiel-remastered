package de.julianhofmann.autospiel.world;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.logging.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends GameObject {

    private int score;
    private double acceleration;
    private double speedX;
    private double speedY;
    private int maxSpeed;
    private boolean up, down, left, right;
    private double timeSinceScoreUp;

    private BufferedImage carOn;
    private BufferedImage carOff;
    private BufferedImage carBoost;

    public Player(double x, double y, int width, int height) {
        super(x, y, width, height);

        Log.trace("Loading player...");

        try {
            carOn = ImageIO.read(new File("rsc/sprites/car/car_on.png"));
            carOff = ImageIO.read(new File("rsc/sprites/car/car_off.png"));
            carBoost = ImageIO.read(new File("rsc/sprites/car/car_boost.png"));
        } catch (IOException e) {
            Log.fatal("Cannot find player car sprites!");
            Game.exit();
            return;
        }

        score = 0;
        maxSpeed = 5 + Game.getSpeedUpgrades();
        acceleration = .5 + Game.getSpeedUpgrades() / 2.0;
        timeSinceScoreUp = 0;
    }

    @Override
    public void update(double deltaTime) {
        timeSinceScoreUp += deltaTime;
        if (timeSinceScoreUp >= 1000) {
            score++;
            timeSinceScoreUp -= 1000;
        }

        if (up) {
            speedY -= acceleration * deltaTime / 15;
        } else if (speedY < 0) {
            speedY += Game.world.getCurrentBiome().getFriction() * deltaTime / 15;
            if (speedY > 0) speedY = 0;
        }

        if (down) {
            speedY += acceleration * deltaTime / 15;
        } else if (speedY > 0) {
            speedY -= Game.world.getCurrentBiome().getFriction() * deltaTime / 15;
            if (speedY < 0) speedY = 0;
        }

        if (left) {
            speedX = -maxSpeed;
        } else if (speedX < 0) {
            speedX += Game.world.getCurrentBiome().getFriction() * deltaTime / 15;
            if (speedX > 0) speedX = 0;
        }

        if (right) {
            speedX = maxSpeed;
        } else if (speedX > 0) {
            speedX -= Game.world.getCurrentBiome().getFriction() * deltaTime / 15;
            if (speedX < 0) speedX = 0;
        }

        if (left && right) speedX = 0;

        if (speedX > maxSpeed) speedX = maxSpeed;
        else if (speedX < -maxSpeed) speedX = -maxSpeed;

        if (speedY > maxSpeed) speedY = maxSpeed;
        else if (speedY < -maxSpeed) speedY = -maxSpeed;

        x += speedX * deltaTime / 15;
        y += speedY * deltaTime / 15;

        if (x < 100) {
            x = 100;
            speedX = 0;
        } else if (x > 655) {
            x = 655;
            speedX = 0;
        }

        if (y < 0) {
            y = 0;
            speedY = 0;
        } else if (y > 510) {
            y = 510;
            speedY = 0;
        }
    }

    public void resetMovement() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void resetScore() {
        Game.setHighscore(score);
        score = 0;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (up) {
            g2d.drawImage(carBoost, (int)Math.round(x), (int)Math.floor(y), width, height, null);
        } else if (down) {
            g2d.drawImage(carOff, (int)Math.round(x), (int)Math.round(y), width, height, null);
        } else {
            g2d.drawImage(carOn, (int)Math.round(x), (int)Math.round(y), width, height, null);
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }
}
