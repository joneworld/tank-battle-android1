package com.tankbattle;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    private GameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        gameView = findViewById(R.id.gameView);
        gameThread = new GameThread(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameThread != null) {
            gameThread.setRunning(true);
            gameThread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameThread != null) {
            boolean retry = true;
            gameThread.setRunning(false);
            while (retry) {
                try {
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gameThread = new GameThread(gameView);
        }
    }
}
