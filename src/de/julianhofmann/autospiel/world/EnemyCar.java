package de.julianhofmann.autospiel.world;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.GameState;
import de.julianhofmann.autospiel.logging.Log;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EnemyCar extends GameObject {

    BufferedImage enemyCar;

    public EnemyCar(double x, double y, int width, int height) {
        super(x, y, width, height);
        try {
            enemyCar = ImageIO.read(new File("rsc/sprites/car/car_enemy.png"));
        } catch (IOException e) {
            Log.fatal("Cannot load 'rsc/sprites/car/car_enemy.png'!");
            Game.exit();
        }
    }

    @Override
    public void update(double deltaTime) {
        Player player = Game.world.getPlayer();
        if (collidesWith(player)) {
            Game.gameOver = true;
            Game.changeState(GameState.MAIN_MENU);
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.drawImage(enemyCar, (int)Math.round(x), (int)Math.round(y), width, height, null);
    }

    public void moveDown(double amount) {
        y += amount;
    }
}
