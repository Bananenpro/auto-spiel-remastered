package de.julianhofmann.autospiel;

import de.julianhofmann.autospiel.logging.Log;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AudioManager {

    private static final HashMap<String, Clip> clips = new HashMap<>();

    public static void loopClip(String path) {
        if (Game.settings.isMusic()) {
            try {
                stopClip(path);
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
                clip.open(inputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clips.put(path, clip);
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                Log.error("Unable to start clip '" + path + "'!");
            }
        }
    }

    public static void stopClip(String path) {
        if (clips.containsKey(path)) {
            clips.get(path).stop();
            clips.remove(path);
        }
    }

    public static void playClip(String path) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
            clip.open(inputStream);
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            Log.error("Unable to start clip '" + path + "'!");
        }
    }
}
