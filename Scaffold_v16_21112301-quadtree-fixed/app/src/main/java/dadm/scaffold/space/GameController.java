package dadm.scaffold.space;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;
import dadm.scaffold.sound.GameEvent;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ENEMIES = 500;
    private long currentMillis;
    public static GameControllerState mState;
    public static double mWaitingTime;


    private List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private int enemiesSpawned;
    private final int INITIAL_LIFES = 3;
    int mNumLives = 0;
    GameEngine gameEngine;

    public GameController(GameEngine gameEngine) {
        // We initialize the pool of items now
        for (int i=0; i<10; i++) {
            asteroidPool.add(new KamikazeBird(this, gameEngine));
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
        mWaitingTime = 0;
        for (int i=0; i<INITIAL_LIFES; i++) {
            gameEngine.onGameEvent(GameEvent.LifeAdded);
        }
        mState = GameControllerState.PlacingSpaceship;

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentMillis += elapsedMillis;

        long waveTimestamp = enemiesSpawned*TIME_BETWEEN_ENEMIES;
        if (currentMillis > waveTimestamp) {
            // Spawn a new enemy
            Asteroid a = asteroidPool.remove(0);
            a.init(gameEngine);


            gameEngine.addGameObject(a);
            enemiesSpawned++;
            return;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(Asteroid asteroid) {
        asteroidPool.add(asteroid);
    }


    public void onGameEvent (GameEvent gameEvent) {

        if (gameEvent == GameEvent.SpaceshipHit) {
            mState = GameController.GameControllerState.StoppingWave;
            mWaitingTime = 0;
        }
        else if (gameEvent == GameEvent.GameOver) {
            mState = GameController.GameControllerState.GameOver;
        }
        else if (gameEvent == GameEvent.LifeAdded) {
            mNumLives++;
        }
    }

}
