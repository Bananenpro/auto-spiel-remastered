package de.julianhofmann.autospiel.world;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;

public class Obstacle extends GameObject {

    BufferedImage texture;

    public Obstacle(double x, double y, int width, int height, BufferedImage texture) {
        super(x, y, width, height);
        this.texture = texture;
    }

    @Override
    public void update(double deltaTime) {
        Player player = Game.world.getPlayer();
        if (collidesWith(player)) {
            Game.gameOver = true;
            Game.changeState(GameState.MAIN_MENU);
        }

        if (y > 600) {
            Game.world.removeObject(this);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(texture, (int)Math.round(x), (int)Math.round(y), width, height, null);
    }

    public void moveDown(double amount) {
        y += amount;
    }
}
