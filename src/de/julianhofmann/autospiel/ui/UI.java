package de.julianhofmann.autospiel.ui;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.logging.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class UI {
    private final JFrame frame;

    private final GameView gameView;
    private final MainMenu mainMenu;
    private final ShopView shopView;
    private final SettingsView settingsView;

    public UI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            Log.error(e.getMessage());
        }

        frame = new JFrame();
        initFrame();

        gameView = new GameView();
        mainMenu = new MainMenu(frame);
        shopView = new ShopView(frame);
        settingsView = new SettingsView(frame);

        frame.setContentPane(mainMenu);
        frame.setVisible(true);
        frame.requestFocus();
        Log.info("Window created!");
    }

    private void initFrame() {
        frame.setTitle("Auto Spiel Remastered");
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(new Input());
        try {
            frame.setIconImage(ImageIO.read(new File("rsc/icon.png")));
        } catch (IOException e) {
            Log.warn("Cannot load icon!");
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                frame.dispose();
                Game.exit();
            }
        });
    }


    public JPanel getPanel() {
        return (JPanel)frame.getContentPane();
    }

    public void setPanel(JPanel panel) {
        if (panel == mainMenu) {
            mainMenu.getPlayButton().setText(Game.gameOver ? "Spielen" : "Weiter");
        }
        frame.setContentPane(panel);
        frame.revalidate();
    }

    public GameView getGameView() {
        return gameView;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public ShopView getShopView() {
        return shopView;
    }

    public SettingsView getSettingsView() {
        return settingsView;
    }

    public JFrame getFrame() {
        return frame;
    }
}
