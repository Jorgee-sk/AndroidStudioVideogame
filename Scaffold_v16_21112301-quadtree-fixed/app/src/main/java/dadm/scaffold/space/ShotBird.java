package dadm.scaffold.space;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.sound.GameEvent;

public class ShotBird extends Asteroid{

    private long currentMillis;
    private int bulletsSpawned;


    private final GameController gameController;

    private static final int INITIAL_BULLET_POOL_AMOUNT = 6;
    private static final long TIME_BETWEEN_BULLETS = 250;
    List<BulletEnemy> bullets = new ArrayList<BulletEnemy>();
    private long timeSinceLastFire;

    public ShotBird(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine, R.drawable.abe2,50,100);
        this.speed = 200d * pixelFactor/1000d;
        this.gameController = gameController;

    }


    @Override
    public void init(GameEngine gameEngine) {
        // They initialize in a 0 degrees angle
        double angle = gameEngine.random.nextDouble()*Math.PI/3d-Math.PI/6d;
        speedY = 0.06;
        speedX = 0.6;
        // initialize in the central 50% of the screen horizontally
        positionX = 0-400;
        // They initialize outside of the screen vertically
        positionY =gameEngine.height/10;
        rotationSpeed = 0 ;
        rotation = 90;

        currentMillis = 0;
        bulletsSpawned = 0;
        initBulletPool(gameEngine);
    }

    //@Override
    //public void startGame() {
    //}


    //public void removeObject(GameEngine gameEngine) {
    //    // Return to the pool
    //    gameEngine.removeGameObject(this);
    //    gameController.returnToPool(this);
    //}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine)         {
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;
        rotation += rotationSpeed * elapsedMillis;
        if (positionX > gameEngine.width+100) {
            rotation = 275;
            speedX = speedX*(-1);
        }
        else if (positionX < 0-400) {
            rotation = 90;
            speedX = speedX*(-1);
        }
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            gameController.returnToPool(this);
        }

        currentMillis += elapsedMillis;

        long waveTimestamp = bulletsSpawned*TIME_BETWEEN_BULLETS;

        if (currentMillis > waveTimestamp) {
        //DISPARO

            BulletEnemy bullet = getBullet();
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
        for (int i=0; i<INITIAL_BULLET_POOL_AMOUNT; i++) {
            bullets.add(new BulletEnemy(gameEngine));
        }
    }

    private BulletEnemy getBullet() {
        if (bullets.isEmpty()) {
            return null;
        }
        return bullets.remove(0);
    }

    void releaseBullet(BulletEnemy bullet) {
        bullets.add(bullet);
    }



    private void checkFiring(long elapsedMillis, GameEngine gameEngine) {
        if (gameEngine.theInputController.isFiring && timeSinceLastFire > TIME_BETWEEN_BULLETS) {
            BulletEnemy bullet = getBullet();
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
        }
    }


}