package dadm.scaffold.space;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.sound.GameEvent;

public class ShotBird extends Asteroid{

    private long currentMillis;
    private int bulletsSpawned;


    private final GameController gameController;
    List<BulletEnemy> bullets = new ArrayList<BulletEnemy>();

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static long TIME_BETWEEN_BULLETS = 800;

    private long timeSinceLastFire;

    public ShotBird(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.pajaroderecha,100,70,30,0);
        this.speed = 200d * pixelFactor/1000d;
        this.gameController = gameController;

    }


    @Override
    public void init(GameEngine gameEngine) {
        // They initialize in a 0 degrees angle
        double angle = gameEngine.random.nextDouble()*Math.PI/3d-Math.PI/6d;
        speedY = 0.02;
        speedX = 0.6;
        // initialize in the central 50% of the screen horizontally
        positionX = 0-400;
        // They initialize outside of the screen vertically
        positionY =gameEngine.height/10;
        rotationSpeed = 0 ;
        rotation = 0;

        currentMillis = 0;
        bulletsSpawned = 0;
        initBulletPool(gameEngine);
    }

    //@Override
    //public void startGame() {
    //}


    public void removeObject(GameEngine gameEngine) {
     //   Return to the pool

       gameEngine.removeGameObject(this);
        this.setBitmap(R.drawable.pajaroderecha);
        gameController.returnToPool(this);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine)         {
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;
        rotation += rotationSpeed * elapsedMillis;
        if (positionX > gameEngine.width+100) {

            setBitmap(R.drawable.pajaroizquierda);
            speedX = speedX*(-1);
        }
        else if (positionX < 0-400) {
            setBitmap(R.drawable.pajaroderecha);
            speedX = speedX*(-1);
        }
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            gameController.returnToPool(this);
        }

        currentMillis += elapsedMillis;

        TIME_BETWEEN_BULLETS = new Random().nextInt((2000 - 1000) + 1) + 1000;
        long waveTimestamp = bulletsSpawned*TIME_BETWEEN_BULLETS;

        if (currentMillis > waveTimestamp && gameController.mState != GameController.GameControllerState.StoppingWave) {
        //DISPARO

            BulletEnemy bullet = getBullet(gameEngine);
            if (bullet == null) {
                return;
            }
            bullet.init(this, positionX + 100/2, positionY+100/2);
            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
            //gameEngine.onGameEvent(GameEvent.LaserFired);


        bulletsSpawned++;
        }

    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }

    private void initBulletPool(GameEngine gameEngine) {
        for (int i=0; i<1000; i++) {
            bullets.add(new BulletEnemy(gameEngine));
        }
    }

    private BulletEnemy getBullet(GameEngine gameEngine) {
        if (bullets.isEmpty()) {
            return null;
        }
        BulletEnemy aux  =bullets.remove(0);
        gameController.enemyBullets.add(aux);
        return aux;
    }

    void releaseBullet(BulletEnemy bullet) {
        //bullets.add(bullet);
        gameController.enemyBullets.remove(bullet);


    }





}
