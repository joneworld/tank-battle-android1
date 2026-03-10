package com.tankbattle;

import android.os.Handler;
import android.os.Looper;

public class GameThread extends Thread {
    private final GameView gameView;
    private final Handler handler;
    private boolean running;
    private static final long FPS = 60;
    private static final long FRAME_TIME = 1000 / FPS;

    public GameThread(GameView gameView) {
        this.gameView = gameView;
        this.handler = new Handler(Looper.getMainLooper());
        this.running = false;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long startTime;
        long sleepTime;

        while (running) {
            startTime = System.currentTimeMillis();

            handler.post(() -> {
                gameView.updateGame();
                gameView.invalidate();
            });

            sleepTime = FRAME_TIME - (System.currentTimeMillis() - startTime);

            try {
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                } else {
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
