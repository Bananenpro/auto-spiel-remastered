package de.julianhofmann.autospiel;

import de.julianhofmann.autospiel.logging.Log;

public class GameLoop {
    private final Thread thread;

    private final double TARGET_FPS = 144;

    private int fps = (int)TARGET_FPS;

    private boolean running = false;

    public GameLoop() {
        thread = new Thread(() -> {
            Log.info("Starting game loop...");

            Log.info("Game loop started successfully!");

            int frames = 0;
            long time = 0;
            double deltaTime = 1000d/144d;
            while (running) {
                long startTime = System.nanoTime();

                Game.update(deltaTime);
                if (Game.ui != null) {
                    Game.ui.getPanel().repaint();
                }

                long delay = Math.round(((1000d/TARGET_FPS) - ((double)(System.nanoTime()-startTime) / 1000000d)));
                try {
                    if (delay > 0) Thread.sleep(delay);
                } catch (InterruptedException e) {
                    Log.error(e.getMessage());
                }

                deltaTime = (double)(System.nanoTime()-startTime) / 1000000d;

                frames++;
                time += (System.nanoTime() - startTime);

                if (time >= 1000000000L) {
                    fps = frames;
                    frames = 0;
                    time -= 1000000000L;
                }
            }

            Log.info("Game loop stopped successfully!");
        });
    }

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        Log.info("Stopping game loop...");
        Log.trace("Waiting for game loop to finish...");
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.error(e.getMessage());
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int getFps() {
        return fps;
    }
}
