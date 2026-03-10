package com.tankbattle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends View {
    
    private static final int GRID_SIZE = 20;
    private static final int COLS = 40;
    private static final int ROWS = 30;
    
    private Tank playerTank;
    private List<Tank> enemyTanks;
    private List<Bullet> bullets;
    private List<Rect> walls;
    private Random random;
    
    private float touchX, touchY;
    private boolean isTouching;
    
    public GameView(Context context) {
        super(context);
        initGame();
    }
    
    private void initGame() {
        playerTank = new Tank(COLS / 2, ROWS - 2, true);
        enemyTanks = new ArrayList<>();
        bullets = new ArrayList<>();
        walls = new ArrayList<>();
        random = new Random();
        
        // Create enemy tanks
        for (int i = 0; i < 5; i++) {
            enemyTanks.add(new Tank(random.nextInt(COLS - 4) + 2, random.nextInt(5), false));
        }
        
        // Create walls
        for (int i = 0; i < 20; i++) {
            int wx = random.nextInt(COLS - 2) + 1;
            int wy = random.nextInt(ROWS - 6) + 3;
            walls.add(new Rect(wx * GRID_SIZE, wy * GRID_SIZE, (wx + 1) * GRID_SIZE, (wy + 1) * GRID_SIZE));
        }
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isTouching = true;
                touchX = x;
                touchY = y;
                updatePlayerDirection();
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                break;
        }
        return true;
    }
    
    private void updatePlayerDirection() {
        float dx = touchX - (playerTank.x * GRID_SIZE + GRID_SIZE / 2);
        float dy = touchY - (playerTank.y * GRID_SIZE + GRID_SIZE / 2);
        
        if (Math.abs(dx) > Math.abs(dy)) {
            playerTank.direction = dx > 0 ? 1 : 3; // Right : Left
        } else {
            playerTank.direction = dy > 0 ? 2 : 0; // Down : Up
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#2C3E50"));
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);
        
        // Draw walls
        Paint wallPaint = new Paint();
        wallPaint.setColor(Color.parseColor("#95A5A6"));
        for (Rect wall : walls) {
            canvas.drawRect(wall, wallPaint);
        }
        
        // Draw bullets
        Paint bulletPaint = new Paint();
        bulletPaint.setColor(Color.parseColor("#F39C12"));
        for (Bullet bullet : bullets) {
            canvas.drawRect(
                bullet.x * GRID_SIZE + GRID_SIZE / 4,
                bullet.y * GRID_SIZE + GRID_SIZE / 4,
                bullet.x * GRID_SIZE + GRID_SIZE * 3 / 4,
                bullet.y * GRID_SIZE + GRID_SIZE * 3 / 4,
                bulletPaint
            );
        }
        
        // Draw player tank
        Paint playerPaint = new Paint();
        playerPaint.setColor(Color.parseColor("#3498DB"));
        drawTank(canvas, playerTank, playerPaint);
        
        // Draw enemy tanks
        Paint enemyPaint = new Paint();
        enemyPaint.setColor(Color.parseColor("#E74C3C"));
        for (Tank tank : enemyTanks) {
            drawTank(canvas, tank, enemyPaint);
        }
        
        // Draw score info
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        canvas.drawText("敌军: " + enemyTanks.size(), 20, 50, textPaint);
    }
    
    private void drawTank(Canvas canvas, Tank tank, Paint paint) {
        int x = tank.x * GRID_SIZE;
        int y = tank.y * GRID_SIZE;
        int s = GRID_SIZE;
        
        // Body
        canvas.drawRect(x + 2, y + 2, x + s - 2, y + s - 2, paint);
        
        // Turret
        Paint turretPaint = new Paint(paint);
        turretPaint.setColor(Color.BLACK);
        canvas.drawCircle(x + s / 2, y + s / 2, s / 4, turretPaint);
        
        // Barrel
        Paint barrelPaint = new Paint(paint);
        barrelPaint.setColor(Color.BLACK);
        switch (tank.direction) {
            case 0: // Up
                canvas.drawRect(x + s / 2 - 2, y, x + s / 2 + 2, y + s / 2, barrelPaint);
                break;
            case 1: // Right
                canvas.drawRect(x + s / 2, y + s / 2 - 2, x + s, y + s / 2 + 2, barrelPaint);
                break;
            case 2: // Down
                canvas.drawRect(x + s / 2 - 2, y + s / 2, x + s / 2 + 2, y + s, barrelPaint);
                break;
            case 3: // Left
                canvas.drawRect(x, y + s / 2 - 2, x + s / 2, y + s / 2 + 2, barrelPaint);
                break;
        }
    }
    
    public void updateGame() {
        // Move player tank
        if (isTouching) {
            moveTank(playerTank);
        }
        
        // Fire player bullets
        if (random.nextInt(30) == 0 && isTouching) {
            bullets.add(new Bullet(playerTank.x, playerTank.y, playerTank.direction, true));
        }
        
        // Move enemy tanks
        for (Tank tank : enemyTanks) {
            if (random.nextInt(20) == 0) {
                tank.direction = random.nextInt(4);
            }
            moveTank(tank);
            
            // Enemy fire
            if (random.nextInt(50) == 0) {
                bullets.add(new Bullet(tank.x, tank.y, tank.direction, false));
            }
        }
        
        // Move bullets
        List<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            moveBullet(bullet);
            
            // Check wall collision
            for (Rect wall : walls) {
                Rect bulletRect = new Rect(
                    bullet.x * GRID_SIZE + GRID_SIZE / 4,
                    bullet.y * GRID_SIZE + GRID_SIZE / 4,
                    bullet.x * GRID_SIZE + GRID_SIZE * 3 / 4,
                    bullet.y * GRID_SIZE + GRID_SIZE * 3 / 4
                );
                if (Rect.intersects(bulletRect, wall)) {
                    toRemove.add(bullet);
                    break;
                }
            }
            
            // Check tank collisions
            if (!toRemove.contains(bullet)) {
                if (bullet.isPlayer) {
                    for (Tank tank : enemyTanks) {
                        if (Math.abs(tank.x - bullet.x) < 1 && Math.abs(tank.y - bullet.y) < 1) {
                            toRemove.add(bullet);
                            tank.alive = false;
                            break;
                        }
                    }
                } else {
                    if (Math.abs(playerTank.x - bullet.x) < 1 && Math.abs(playerTank.y - bullet.y) < 1) {
                        toRemove.add(bullet);
                        playerTank.alive = false;
                    }
                }
            }
            
            // Check out of bounds
            if (!toRemove.contains(bullet) && (bullet.x < 0 || bullet.x >= COLS || bullet.y < 0 || bullet.y >= ROWS)) {
                toRemove.add(bullet);
            }
        }
        
        bullets.removeAll(toRemove);
        enemyTanks.removeIf(tank -> !tank.alive);
        
        // Check win condition
        if (enemyTanks.isEmpty()) {
            // Spawn new enemies
            for (int i = 0; i < 5; i++) {
                enemyTanks.add(new Tank(random.nextInt(COLS - 4) + 2, random.nextInt(5), false));
            }
        }
        
        // Check lose condition
        if (!playerTank.alive) {
            initGame();
        }
    }
    
    private void moveTank(Tank tank) {
        int newX = tank.x;
        int newY = tank.y;
        
        switch (tank.direction) {
            case 0: newY--; break;
            case 1: newX++; break;
            case 2: newY++; break;
            case 3: newX--; break;
        }
        
        if (newX >= 0 && newX < COLS && newY >= 0 && newY < ROWS) {
            // Check wall collision
            Rect newRect = new Rect(newX * GRID_SIZE, newY * GRID_SIZE, (newX + 1) * GRID_SIZE, (newY + 1) * GRID_SIZE);
            boolean collision = false;
            for (Rect wall : walls) {
                if (Rect.intersects(newRect, wall)) {
                    collision = true;
                    break;
                }
            }
            
            if (!collision) {
                tank.x = newX;
                tank.y = newY;
            }
        }
    }
    
    private void moveBullet(Bullet bullet) {
        switch (bullet.direction) {
            case 0: bullet.y--; break;
            case 1: bullet.x++; break;
            case 2: bullet.y++; break;
            case 3: bullet.x--; break;
        }
    }
    
    class Tank {
        int x, y;
        int direction;
        boolean isPlayer;
        boolean alive;
        
        Tank(int x, int y, boolean isPlayer) {
            this.x = x;
            this.y = y;
            this.isPlayer = isPlayer;
            this.alive = true;
            this.direction = isPlayer ? 0 : random.nextInt(4);
        }
    }
    
    class Bullet {
        int x, y;
        int direction;
        boolean isPlayer;
        
        Bullet(int x, int y, int direction, boolean isPlayer) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.isPlayer = isPlayer;
            
            // Offset bullet based on direction
            switch (direction) {
                case 0: this.y--; break;
                case 1: this.x++; break;
                case 2: this.y++; break;
                case 3: this.x--; break;
            }
        }
    }
}
