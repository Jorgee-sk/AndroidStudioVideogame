package dadm.scaffold.space;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;

public class SpaceShipPlayer extends Sprite {

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 800;
    List<Bullet> bullets = new ArrayList<Bullet>();
    private long timeSinceLastFire;
    double speedFactor;
    private long lastFrameChangeTime = 0;
    private int frameLengthInMillisecond = 500;
    private int nextResourceIntegerId = 0;

    private int maxX;
    private int maxY;

    private GameController gameController;

    private int resource;


    public SpaceShipPlayer(GameController gameController,GameEngine gameEngine, int resource){

        super(gameEngine,resource,85,80,30,+25);
        this.resource = resource;
        nextResourceIntegerId = resource;
        speedFactor = pixelFactor * 100d / 400d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;

        this.gameController = gameController;

        initBulletPool(gameEngine);
    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new Bullet(gameEngine));
        }
    }

    private Bullet getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    void releaseBullet(Bullet bullet) {
        bullets.add(bullet);
    }


    @Override
    public void startGame() {
        positionX = maxX / 2;
        positionY = maxY / 1.2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        checkFiring(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }
    }

    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {

        if(gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS){
            Bullet bullet = getBullet();
            Bullet bullet2 = getBullet();
            if (bullet == null || bullet2 == null) {
                return;
            }
            bullet.init(this, positionX + width/4.5, positionY);
            bullet2.init(this, positionX + width/1, positionY);
            gameEngine.addGameObject(bullet);
            gameEngine.addGameObject(bullet2);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }

        /*
        if (gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            Bullet bullet = getBullet();
            if (bullet == null) {
                return;
            }
            bullet.init(this, positionX + width/2, positionY);
            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }*/
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            gameEngine.removeGameObject(this);
            //gameEngine.stopGame();
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }
        if (otherObject instanceof BulletEnemy) {
            gameEngine.removeGameObject(this);
            //gameEngine.stopGame();
            BulletEnemy a = (BulletEnemy) otherObject;
            a.removeObject(gameEngine);
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
        }

        if (otherObject instanceof BulletEnemy || otherObject instanceof Asteroid) {

            int auxSize = gameController.enemyBullets.size();
            for(int i = 0;i < auxSize;i++){



                    gameController.enemyBullets.get(0).removeObject(gameEngine);



            }


        }




    }

    @Override
    public void onDraw(Canvas canvas) {
        long time = System.currentTimeMillis();
            if (time > lastFrameChangeTime + frameLengthInMillisecond) {
                lastFrameChangeTime = time;
                super.setBitmap(nextResourceIntegerId);
                if (nextResourceIntegerId == resource) {
                    nextResourceIntegerId = resource;
                } else {
                    nextResourceIntegerId = resource;
                }
            }
        super.onDraw(canvas);
    }

}
