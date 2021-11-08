package de.julianhofmann.autospiel;

import de.julianhofmann.autospiel.logging.Log;
import de.julianhofmann.autospiel.logging.LogLevel;
import de.julianhofmann.autospiel.ui.UI;
import de.julianhofmann.autospiel.world.World;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;

public class Game {
    public static GameLoop gameLoop;
    public static UI ui;
    public static World world;
    public static Settings settings;
    public static Config config;
    public static GameState gameState = GameState.MAIN_MENU;
    public static boolean gameOver;

    public static final String MENU_MUSIC = "rsc/audio/music/menu.wav";
    public static final String IN_GAME_MUSIC = "rsc/audio/music/in_game.wav";

    private static int highscore;
    private static int money;
    private static int speedUpgrades;
    private static int moneyUpgrades;

    public static void main(String[] args) {
        config = new Config();

        Log.logLevel = config.getLogLevel();
        Log.info("Starting game...");

        settings = new Settings();

        load();

        Log.trace("Initializing game loop...");
        gameLoop = new GameLoop();
        gameLoop.start();

        Log.trace("Creating world...");
        world = new World(800, 600);

        Log.trace("Initializing ui...");
        ui = new UI();

        gameOver = true;

        startMusic();
    }

    public static void startMusic() {
        if (gameState == GameState.IN_GAME) {
            AudioManager.loopClip(IN_GAME_MUSIC);
        } else {
            AudioManager.loopClip(MENU_MUSIC);
        }
    }

    public static void stopMusic() {
        AudioManager.stopClip(MENU_MUSIC);
        AudioManager.stopClip(IN_GAME_MUSIC);
    }

    public static void update(double deltaTime) {
        if (gameState == GameState.IN_GAME) {
            world.update(deltaTime);
        }
    }

    public static void exit() {
        setHighscore(world.getPlayer().getScore());
        world.stop();
        gameLoop.stop();
        save();
        Log.info("Shutdown complete!");
        System.exit(0);
    }

    public static void load() {
        Log.info("Loading game...");
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(new FileReader("rsc/savegame/savegame.json"));
            highscore = (int) (long) object.get("highscore");
            money = (int) (long) object.get("money");
            speedUpgrades = (int) (long) object.get("speed_upgrades");
            moneyUpgrades = (int) (long) object.get("money_upgrades");
        } catch (IOException e) {
            Log.error("Cannot load savegame file 'rsc/savegame/savegame.json'!");
        } catch (ParseException e) {
            Log.error("Syntax error in 'rsc/savegame/savegame.json'!");
        } catch (NullPointerException e) {
            Log.error("Missing key in 'rsc/savegame/savegame.json'!");
        }
    }

    public static void save() {
        Log.info("Saving game...");
        JSONObject object = new JSONObject();
        object.put("highscore", highscore);
        object.put("money", money);
        object.put("speed_upgrades", speedUpgrades);
        object.put("money_upgrades", moneyUpgrades);
        String json = object.toJSONString();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("rsc/savegame/savegame.json"));
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.error("Cannot save game!");
        }
    }

    public static void changeState(GameState state) {
        Log.info("Changing game state to '" + state + "'...");
        switch (state) {
            case MAIN_MENU:
                ui.setPanel(ui.getMainMenu());
                if (gameState == GameState.IN_GAME) {
                    AudioManager.stopClip(IN_GAME_MUSIC);
                    AudioManager.loopClip(MENU_MUSIC);
                }
                gameState = GameState.MAIN_MENU;
                if (gameOver)
                    world.stop();
                else world.pause();
                break;
            case IN_GAME:
                ui.setPanel(ui.getGameView());
                gameState = GameState.IN_GAME;
                world.start();
                AudioManager.stopClip(MENU_MUSIC);
                AudioManager.loopClip(IN_GAME_MUSIC);
                break;
            case SETTINGS:
                ui.setPanel(ui.getSettingsView());
                gameState = GameState.SETTINGS;
                break;
            case SHOP:
                ui.setPanel(ui.getShopView());
                gameState = GameState.SHOP;
                break;
            default:
                Log.error("Cannot resolve game state '"+state+"'!");
        }
        Game.ui.getFrame().requestFocus();
    }

    public static int getHighscore() {
        return highscore;
    }

    public static void setHighscore(int newHighscore) {
        if (newHighscore > highscore) {
            highscore = newHighscore;
        }
    }

    public static int getMoney() {
        return money;
    }

    public static void addMoney(int amount) {
        money += amount;
    }

    public static int getSpeedUpgrades() {
        return speedUpgrades;
    }

    public static void addSpeedUpgrades(int count) {
        speedUpgrades += count;
    }

    public static int getMoneyUpgrades() {
        return moneyUpgrades;
    }

    public static void addMoneyUpgrades(int count) {
        moneyUpgrades += count;
    }
}
