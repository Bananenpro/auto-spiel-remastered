package de.julianhofmann.autospiel;

import de.julianhofmann.autospiel.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    private boolean music = true;

    public Settings() {
        load();
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
        if (music) {
            Game.startMusic();
        } else {
            Game.stopMusic();
        }
        save();
    }

    public void save() {
        Log.info("Saving settings...");
        JSONObject object = new JSONObject();
        object.put("music", music);
        String json = object.toJSONString();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("rsc/settings/user_settings.json"));
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.error("Cannot save settings!");
        }
    }

    public void load() {
        Log.info("Loading settings...");
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(new FileReader("rsc/settings/user_settings.json"));
            music = (boolean) object.get("music");
        } catch (IOException e) {
            Log.error("Cannot load settings file 'rsc/settings/user_settings.json'!");
        } catch (ParseException e) {
            Log.error("Syntax error in 'rsc/settings/user_settings.json'!");
        } catch (NullPointerException e) {
            Log.error("Missing key in 'rsc/settings/user_settings.json'!");
        }
    }
}
