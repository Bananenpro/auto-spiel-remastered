package de.julianhofmann.autospiel.world;

import de.julianhofmann.autospiel.Game;

import java.awt.image.BufferedImage;

public class Coin extends Obstacle {
    public Coin(double x, double y, int width, int height, BufferedImage texture) {
        super(x, y, width, height, texture);
    }

    @Override
    public void update(double deltaTime) {
        Player player = Game.world.getPlayer();
        if (collidesWith(player)) {
            Game.addMoney(1 + Game.getMoneyUpgrades());
            Game.world.removeObject(this);
        }

        if (y > 600) {
            Game.world.removeObject(this);
        }
    }
}
