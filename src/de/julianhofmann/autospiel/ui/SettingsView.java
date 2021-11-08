package de.julianhofmann.autospiel.ui;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.GameState;
import de.julianhofmann.autospiel.Settings;
import de.julianhofmann.autospiel.logging.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsView extends JPanel {
    private final Color buttonColor = Color.RED;
    private final Color buttonHoverColor = new Color(180, 0, 0);
    private final Color buttonClickColor = new Color(140, 0, 0);

    public SettingsView(JFrame frame) {
        setLayout(null);
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Einstellungen", JLabel.CENTER);
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

        JButton music = new JButton("Musik " + (Game.settings.isMusic() ? "AN" : "AUS"));
        music.setBounds(290, 250, 220, 30);
        music.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 28));
        music.setForeground(buttonColor);
        music.setContentAreaFilled(false);
        music.setOpaque(false);
        music.setFocusPainted(false);
        music.setBorderPainted(false);
        music.addMouseListener(buttonMouseAdapter);
        music.addActionListener((event) -> toggleMusic(music));
        add(music);

        JButton back = new JButton("Zurück");
        back.setBounds(290, 430, 220, 30);
        back.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 28));
        back.setForeground(buttonColor);
        back.setContentAreaFilled(false);
        back.setOpaque(false);
        back.setFocusPainted(false);
        back.setBorderPainted(false);
        back.addMouseListener(buttonMouseAdapter);
        back.addActionListener((event) -> Game.changeState(GameState.MAIN_MENU));
        add(back);
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

    private void toggleMusic(JButton button) {
        Game.settings.setMusic(!Game.settings.isMusic());
        button.setText("Musik " + (Game.settings.isMusic() ? "AN" : "AUS"));
    }
}
