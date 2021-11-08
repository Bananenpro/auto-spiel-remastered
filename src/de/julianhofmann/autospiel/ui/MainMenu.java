package de.julianhofmann.autospiel.ui;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {
    private final Color buttonColor = Color.RED;
    private final Color buttonHoverColor = new Color(180, 0, 0);
    private final Color buttonClickColor = new Color(140, 0, 0);

    private final JButton play;

    public MainMenu(JFrame frame) {
        setLayout(null);
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Auto Spiel Remastered", JLabel.CENTER);
        title.setBounds(0, 100, 800, 100);
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 48));
        title.setForeground(Color.MAGENTA);
        add(title);

        MouseAdapter buttonMouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JButton button = (JButton)e.getSource();
                button.setForeground(buttonClickColor);
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JButton button = (JButton)e.getSource();
                button.setForeground(buttonColor);
                super.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton)e.getSource();
                button.setForeground(buttonHoverColor);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JButton button = (JButton)e.getSource();
                button.setForeground(buttonColor);
                super.mouseExited(e);
            }
        };

        play = new JButton("Spielen");
        play.setBounds(270, 250, 260, 30);
        play.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 28));
        play.setForeground(buttonColor);
        play.setContentAreaFilled(false);
        play.setOpaque(false);
        play.setFocusPainted(false);
        play.setBorderPainted(false);
        play.addMouseListener(buttonMouseAdapter);
        play.addActionListener((event) -> Game.changeState(GameState.IN_GAME));
        add(play);

        JButton shop = new JButton("Shop");
        shop.setBounds(270, 310, 260, 30);
        shop.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 28));
        shop.setForeground(buttonColor);
        shop.setContentAreaFilled(false);
        shop.setOpaque(false);
        shop.setFocusPainted(false);
        shop.setBorderPainted(false);
        shop.addMouseListener(buttonMouseAdapter);
        shop.addActionListener((event) -> Game.changeState(GameState.SHOP));
        add(shop);

        JButton settings = new JButton("Einstellungen");
        settings.setBounds(270, 370, 260, 30);
        settings.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 28));
        settings.setForeground(buttonColor);
        settings.setContentAreaFilled(false);
        settings.setOpaque(false);
        settings.setFocusPainted(false);
        settings.setBorderPainted(false);
        settings.addMouseListener(buttonMouseAdapter);
        settings.addActionListener((event) -> Game.changeState(GameState.SETTINGS));
        add(settings);

        JButton exit = new JButton("Beenden");
        exit.setBounds(270, 430, 260, 30);
        exit.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 28));
        exit.setForeground(buttonColor);
        exit.setContentAreaFilled(false);
        exit.setOpaque(false);
        exit.setFocusPainted(false);
        exit.setBorderPainted(false);
        exit.addMouseListener(buttonMouseAdapter);
        exit.addActionListener((event) -> {
            frame.dispose();
            Game.exit();
        });

        add(exit);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 200, 200);

        Game.world.draw(g2d);
    }

    public JButton getPlayButton() {
        return play;
    }
}
