package de.julianhofmann.autospiel.ui;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.GameState;
import de.julianhofmann.autospiel.logging.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShopView extends JPanel {
    private final Color buttonColor = Color.RED;
    private final Color buttonHoverColor = new Color(180, 0, 0);
    private final Color buttonClickColor = new Color(140, 0, 0);

    private final JButton moneyUpgrade;
    private final JButton speedUpgrade;

    public ShopView(JFrame frame) {
        setLayout(null);
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Shop", JLabel.CENTER);
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

        moneyUpgrade = new JButton("Mehr Geld (" + (int)(50 * Math.pow(2, Game.getMoneyUpgrades()))+ " Geld)");
        moneyUpgrade.setBounds(175, 250, 450, 30);
        moneyUpgrade.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 28));
        moneyUpgrade.setForeground(buttonColor);
        moneyUpgrade.setContentAreaFilled(false);
        moneyUpgrade.setOpaque(false);
        moneyUpgrade.setFocusPainted(false);
        moneyUpgrade.setBorderPainted(false);
        moneyUpgrade.addMouseListener(buttonMouseAdapter);
        moneyUpgrade.addActionListener((event) -> moneyUpgrade());
        add(moneyUpgrade);

        speedUpgrade = new JButton("Mehr SPEED (" + (int)(50 * Math.pow(2, Game.getSpeedUpgrades()))+ " Geld)");
        speedUpgrade.setBounds(175, 310, 450, 30);
        speedUpgrade.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 28));
        speedUpgrade.setForeground(buttonColor);
        speedUpgrade.setContentAreaFilled(false);
        speedUpgrade.setOpaque(false);
        speedUpgrade.setFocusPainted(false);
        speedUpgrade.setBorderPainted(false);
        speedUpgrade.addMouseListener(buttonMouseAdapter);
        speedUpgrade.addActionListener((event) -> speedUpgrade());
        add(speedUpgrade);

        JButton back = new JButton("Zurück");
        back.setBounds(275, 430, 250, 30);
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

    private void moneyUpgrade() {
        int cost = 50 * (int)Math.pow(2, Game.getMoneyUpgrades());
        if (Game.getMoney() >= cost) {
            Game.addMoneyUpgrades(1);
            Game.addMoney(-cost);
            moneyUpgrade.setText("Mehr Geld (" + (int)(50 * Math.pow(2, Game.getMoneyUpgrades()))+ " Geld)");
        }
    }

    private void speedUpgrade() {
        int cost = 50 * (int)Math.pow(2, Game.getSpeedUpgrades());
        if (Game.getMoney() >= cost) {
            Game.addSpeedUpgrades(1);
            Game.addMoney(-cost);
            speedUpgrade.setText("Mehr SPEED (" + (int)(50 * Math.pow(2, Game.getSpeedUpgrades()))+ " Geld)");
        }
    }
}
