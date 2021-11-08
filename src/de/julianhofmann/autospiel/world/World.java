package de.julianhofmann.autospiel.world;

import de.julianhofmann.autospiel.Game;
import de.julianhofmann.autospiel.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class World {

    private final int BACKGROUND_SPEED_UP_DELAY;
    private final int BACKGROUND_CHANGE_DELAY;
    private final int OBSTACLE_SPAWN_DELAY;
    private final int COIN_SPAWN_DELAY;

    private final ArrayList<Biome> biomes;
    private final Random random;
    private final int width, height;

    private Biome currentBiome;
    private Biome nextBiome;
    private BufferedImage background1, background2;
    private boolean started, paused;
    private Player player;
    private double background1Y, background2Y;
    private int backgroundSpeed;
    private double backgroundSpeedUpTimer;
    private double backgroundChangeTimer;
    private double obstacleSpawnTimer;
    private double coinSpawnTimer;

    private final ArrayList<Obstacle> objects;
    private final ArrayList<Obstacle> toBeRemovedObjects;
    private final EnemyCar enemyCar;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        random = new Random();

        BACKGROUND_CHANGE_DELAY = Game.config.getBackgroundChangeDelay();
        BACKGROUND_SPEED_UP_DELAY = Game.config.getBackgroundSpeedUpDelay();
        OBSTACLE_SPAWN_DELAY = Game.config.getObstacleSpawnDelay();
        COIN_SPAWN_DELAY = Game.config.getCoinSpawnDelay();

        objects = new ArrayList<>();
        toBeRemovedObjects = new ArrayList<>();
        enemyCar = new EnemyCar(random.nextDouble()*355 + 100, -height*30, 45, 75);

        Log.trace("Loading biomes...");

        biomes = new ArrayList<>();

        File biomesDir = new File("rsc/biomes/");
        File[] files = biomesDir.listFiles(file -> file.getName().endsWith(".json"));
        if (files != null) {
            Log.trace("Found " + files.length + " biomes.");
            for (File file : files) {
                try {
                    FileReader fileReader = new FileReader(file.getPath());
                    JSONParser parser = new JSONParser();
                    JSONObject obj = (JSONObject) parser.parse(fileReader);
                    String name = (String) obj.get("name");

                    Log.trace("Loading biome '"+name+"'...");

                    double friction = (Double) obj.get("friction");
                    BufferedImage background;
                    BufferedImage obstacle;
                    BufferedImage coin;

                    try {
                        background = ImageIO.read(new File((String) obj.get("background")));
                    } catch (IOException e) {
                        Log.error("Cannot find background image'" + obj.get("background") + "'!");
                        fileReader.close();
                        continue;
                    }

                    try {
                        obstacle = ImageIO.read(new File((String) obj.get("obstacle")));
                    } catch (IOException e) {
                        Log.error("Cannot find obstacle image'" + obj.get("obstacle") + "'!");
                        fileReader.close();
                        continue;
                    }

                    try {
                        coin = ImageIO.read(new File((String) obj.get("coin")));
                    } catch (IOException e) {
                        Log.error("Cannot find coin image'" + obj.get("coin") + "'!");
                        fileReader.close();
                        continue;
                    }

                    biomes.add(new Biome(name, friction, background, obstacle, coin));
                    fileReader.close();
                } catch (IOException e) {
                    Log.error("Cannot read biome file '"+file.getPath()+"'!");
                } catch (ParseException e) {
                    Log.error("Syntax error in file '"+file.getPath()+"'!");
                } catch (NullPointerException e) {
                    Log.error("Missing key in biome file '"+file.getPath()+"'!");
                }
            }
            if (biomes.isEmpty()) {
                Log.fatal("No biomes loaded!");
                Game.exit();
            }
        } else {
            Log.fatal("Cannot find biome files!");
            Game.exit();
            return;
        }

        player = new Player(width / 2.0 - 45.0 / 2.0, height - height / 3.0, 45, 75);
    }

    public void start() {
        if (!paused) {
            currentBiome = biomes.get(random.nextInt(biomes.size()));
            nextBiome = currentBiome;
            background1 = currentBiome.getBackground();
            background2 = currentBiome.getBackground();
            background1Y = 0;
            background2Y = -height;
            backgroundSpeed = 5;
            backgroundSpeedUpTimer = BACKGROUND_SPEED_UP_DELAY;
            backgroundChangeTimer = BACKGROUND_CHANGE_DELAY;
            obstacleSpawnTimer = OBSTACLE_SPAWN_DELAY;
            coinSpawnTimer = COIN_SPAWN_DELAY;
            player.setX(width / 2.0 - 45.0 / 2.0);
            player.setY(height - height / 3.0);
            player.resetMovement();
            player.resetScore();
            player.setSpeedX(0);
            player.setSpeedY(0);
            objects.clear();
            enemyCar.setX(random.nextDouble() * 355 + 100);
            enemyCar.setY(-height * 30);
        }
        started = true;
        paused = false;
        Game.gameOver = false;
    }

    public void pause() {
        player.resetMovement();
        paused = true;
    }

    public void stop() {
        player.resetMovement();
        Game.setHighscore(player.getScore());
        started = false;
    }

    public void draw(Graphics2D g2d) {
        if (isRunning()) {
            g2d.drawImage(background1, 0, (int) background1Y, width, height, null);
            g2d.drawImage(background2, 0, (int) background2Y, width, height, null);
            player.draw(g2d);
            for (Obstacle obstacle : objects) {
                obstacle.draw(g2d);
            }
            enemyCar.draw(g2d);
            g2d.setFont(new Font(g2d.getFont().getFontName(), Font.BOLD, 18));
        } else {
            g2d.setFont(new Font(g2d.getFont().getFontName(), Font.PLAIN, 18));
        }
        g2d.setColor(Color.RED);
        g2d.drawString("Score: " + player.getScore(), 5, 25);
        g2d.drawString("Highscore: " + Game.getHighscore(), 5, 50);
        g2d.drawString("Geld: " + Game.getMoney(), 5, 75);
        g2d.drawString("Speedmulti.: x" + (Game.getSpeedUpgrades() + 1), 5, 100);
        g2d.drawString("Geldmulti.: x" + (Game.getMoneyUpgrades() + 1), 5, 125);
    }

    public void update(double deltaTime) {
        if (started) {
            backgroundSpeedUpTimer -= deltaTime;
            if (backgroundSpeedUpTimer <= 0) {
                backgroundSpeed++;
                backgroundSpeedUpTimer += BACKGROUND_SPEED_UP_DELAY;
            }

            backgroundChangeTimer -= deltaTime;
            if (backgroundChangeTimer <= 0) {
                nextBiome = biomes.get(random.nextInt(biomes.size()));
                backgroundChangeTimer += BACKGROUND_CHANGE_DELAY;
            }


            moveBackground(deltaTime);
            moveObstacles(deltaTime);
            moveEnemyCar(deltaTime);
            player.update(deltaTime);

            obstacleSpawnTimer -= deltaTime;
            if (obstacleSpawnTimer <= 0) {
                objects.add(new Obstacle(random.nextInt(640-100)+100, random.nextInt(200)-260, 60, 45, nextBiome.getObstacle()));
                obstacleSpawnTimer += OBSTACLE_SPAWN_DELAY;
            }

            coinSpawnTimer -= deltaTime;
            if (coinSpawnTimer <= 0) {
                objects.add(new Coin(random.nextInt(640-100)+100, random.nextInt(200)-260, 50, 50, nextBiome.getCoin()));
                coinSpawnTimer += COIN_SPAWN_DELAY;
            }

            for (Obstacle object : objects) {
                object.update(deltaTime);
            }

            for (Obstacle object : toBeRemovedObjects) {
                objects.remove(object);
            }

            toBeRemovedObjects.clear();

            enemyCar.update(deltaTime);

            if (currentBiome != nextBiome) {
                if (nextBiome.getBackground() == background1 && background1Y + 640 > player.getY() || nextBiome.getBackground() == background2 && background2Y + 640 > player.getY()) {
                    currentBiome = nextBiome;
                }
            }
        }
    }

    private void moveEnemyCar(double deltaTime) {
        enemyCar.moveDown(backgroundSpeed * 2 * deltaTime / 15);
        if (enemyCar.getY() > 600) {
            enemyCar.setY(-height * (random.nextDouble()*20+10));
            enemyCar.setX(random.nextDouble()*355 + 100);
        }
    }

    private void moveObstacles(double deltaTime) {
        for (Obstacle object : objects) {
            object.moveDown(backgroundSpeed * deltaTime / 15);
        }
    }

    private void moveBackground(double deltaTime) {
        background1Y += backgroundSpeed * deltaTime / 15;
        background2Y += backgroundSpeed * deltaTime / 15;

        if (background1Y >= height) {
            background1Y = background2Y - height;
            if (nextBiome.getBackground() != background1) {
                background1 = nextBiome.getBackground();
            }
        } else if (background2Y >= height) {
            background2Y = background1Y - height;
            if (nextBiome.getBackground() != background2) {
                background2 = nextBiome.getBackground();
            }
        }
    }

    public void removeObject(Obstacle object) {
        toBeRemovedObjects.add(object);
    }

    public Biome getCurrentBiome() {
        return currentBiome;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isRunning() {
        return started && !paused;
    }
}
