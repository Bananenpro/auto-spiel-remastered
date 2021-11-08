package de.julianhofmann.autospiel;

import de.julianhofmann.autospiel.logging.Log;
import de.julianhofmann.autospiel.logging.LogLevel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Config {

    private int backgroundChangeDelay = 12000;
    private int backgroundSpeedUpDelay = 15000;
    private int obstacleSpawnDelay = 400;
    private int coinSpawnDelay = 2000;
    private LogLevel logLevel = LogLevel.TRACE;

    public Config() {
        load();
    }

    public void load() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(new FileReader("rsc/settings/config.json"));
            backgroundChangeDelay = (int) (long) object.get("background_change_delay");
            backgroundSpeedUpDelay = (int) (long) object.get("background_speedup_delay");
            obstacleSpawnDelay = (int) (long) object.get("obstacle_spawn_delay");
            coinSpawnDelay = (int) (long) object.get("coin_spawn_delay");
            try {
                logLevel = LogLevel.valueOf((String) object.get("log_level"));
            } catch (IllegalArgumentException e) {
                Log.error("Unknown log level!");
            }
        } catch (IOException e) {
            Log.error("Cannot load config file 'rsc/settings/config.json'!");
        } catch (ParseException e) {
            Log.error("Syntax error in 'rsc/settings/config.json'!");
        } catch (NullPointerException e) {
            Log.error("Missing key in 'rsc/settings/config.json'!");
        }
    }

    public int getBackgroundChangeDelay() {
        return backgroundChangeDelay;
    }

    public int getBackgroundSpeedUpDelay() {
        return backgroundSpeedUpDelay;
    }

    public int getObstacleSpawnDelay() {
        return obstacleSpawnDelay;
    }

    public int getCoinSpawnDelay() {
        return coinSpawnDelay;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }
}
