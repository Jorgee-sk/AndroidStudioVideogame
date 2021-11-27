package dadm.scaffold.space;

import android.app.Activity;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;
import dadm.scaffold.sound.GameEvent;

public class GameController extends GameObject {

    private int TIME_BETWEEN_ENEMIES = 2000 *10000;
    private static final double STOPPING_WAVE_WAITING_TIME = 2000;
    private static final double WAITING_TIME = 500;
    private long currentMillis;

    private int powerUpSpawned;
    private int TIME_BETWEEN_POWERUP = 10000;

    public static GameControllerState mState;
    public static double mWaitingTime;


    private List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private int enemiesSpawned;
    private final int INITIAL_LIFES = 4;
    int mNumLifes = 0;
    GameEngine gameEngine;

    List<BulletEnemy> enemyBullets = new ArrayList<BulletEnemy>();

    public GameController(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        // We initialize the pool of items now
        for (int i=0; i<20; i++) {
            asteroidPool.add(new KamikazeBird(this, gameEngine));
            asteroidPool.add(new ShotBird(this, gameEngine));
        }

    }

    public enum GameControllerState {
        StoppingWave,
        SpawningEnemies,
        PlacingSpaceship,
        Waiting,
        GameOver;
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        enemiesSpawned = 0;
        powerUpSpawned = 0;
        mWaitingTime = 0;
        for (int i=0; i<INITIAL_LIFES; i++) {
            gameEngine.onGameEvent(GameEvent.LifeAdded);
        }
        mState = GameControllerState.StoppingWave;


        ShotBird shotBird = new ShotBird(this, gameEngine);
        shotBird.init(gameEngine);
        gameEngine.addGameObject(shotBird);


    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

        /*long waveTimestamp = enemiesSpawned * TIME_BETWEEN_ENEMIES;
        currentMillis += elapsedMillis;
        if (currentMillis > waveTimestamp) {
            // Spawn a new enemy
            Asteroid a = asteroidPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            enemiesSpawned++;
            return;
        }*/
        TIME_BETWEEN_ENEMIES -= 15;

        if (mState == GameControllerState.SpawningEnemies) {
            currentMillis += elapsedMillis;


            //SPAWN POWERUP
            long PowerUpTimestamp = powerUpSpawned * TIME_BETWEEN_POWERUP;
            if (currentMillis > PowerUpTimestamp) {
                Log.i("tagbtwn", String.valueOf(TIME_BETWEEN_POWERUP));
                PowerUp powerUp = new PowerUp(this, gameEngine);
                powerUp.init(gameEngine);
                gameEngine.addGameObject(powerUp);

                powerUpSpawned++;
                return;
            }




            //SPAWN ENEMIGOS
            long waveTimestamp = enemiesSpawned * TIME_BETWEEN_ENEMIES /10000;
            if (currentMillis > waveTimestamp) {

                // Spawn a new enemy
                Asteroid a = asteroidPool.remove(0);
                a.init(gameEngine);
                gameEngine.addGameObject(a);
                enemiesSpawned++;
                return;
            }



        }
        else if (mState == GameControllerState.StoppingWave) {
            mWaitingTime += elapsedMillis;
            if (mWaitingTime > STOPPING_WAVE_WAITING_TIME) {
                mState = GameControllerState.PlacingSpaceship;
            }
        }
        else if (mState == GameControllerState.PlacingSpaceship) {
            if (mNumLifes == 0) {
                gameEngine.onGameEvent(GameEvent.GameOver);
            }
            else {
                mNumLifes--;
                gameEngine.onGameEvent(GameEvent.LifeLost);
                //gameEngine.mainActivity.settings3 = gameEngine.mainActivity.getSharedPreferences("soundManager",0);
                SpaceShipPlayer newLife = new SpaceShipPlayer(this,gameEngine,gameEngine.mainActivity.skin);
                gameEngine.addGameObject(newLife);
                newLife.startGame();
                // We wait to start spawning more enemies
                mState = GameControllerState.Waiting;
                mWaitingTime = 0;
            }
        }
        else if (mState == GameControllerState.Waiting) {
            mWaitingTime += elapsedMillis;
            if (mWaitingTime > WAITING_TIME) {
                mState = GameControllerState.SpawningEnemies;
            }
        }

    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(Asteroid asteroid) {
        asteroidPool.add(asteroid);
    }

    @Override
    public void onGameEvent (GameEvent gameEvent) {

        if (gameEvent == GameEvent.SpaceshipHit) {
            mState = GameController.GameControllerState.StoppingWave;
            mWaitingTime = 0;
        }
        else if (gameEvent == GameEvent.GameOver) {
            mState = GameController.GameControllerState.GameOver;
        }
        else if (gameEvent == GameEvent.LifeAdded) {
            mNumLifes++;
        }
    }

}
