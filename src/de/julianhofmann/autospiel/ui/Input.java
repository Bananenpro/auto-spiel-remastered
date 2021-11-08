package de.julianhofmann.autospiel.ui;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (Game.world.isRunning() && Game.gameState == GameState.IN_GAME) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_A || keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                Game.world.getPlayer().setLeft(true);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_D || keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                Game.world.getPlayer().setRight(true);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_W || keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                Game.world.getPlayer().setUp(true);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_S || keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                Game.world.getPlayer().setDown(true);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Game.changeState(GameState.MAIN_MENU);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (Game.world.isRunning()) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_A || keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                Game.world.getPlayer().setLeft(false);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_D || keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                Game.world.getPlayer().setRight(false);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_W || keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                Game.world.getPlayer().setUp(false);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_S || keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                Game.world.getPlayer().setDown(false);
            }
        }
    }
}
