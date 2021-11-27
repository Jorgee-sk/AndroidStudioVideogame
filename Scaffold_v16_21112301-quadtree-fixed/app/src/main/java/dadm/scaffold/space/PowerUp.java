package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class PowerUp extends Sprite {

    private final GameController gameController;

    protected double speed;
    protected double speedX;
    protected double speedY;
    protected double rotationSpeed;

    public PowerUp(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.persona,40,40,0,0);
        this.speed = 200d * pixelFactor/1000d;
        this.gameController = gameController;
    }

    public void init(GameEngine gameEngine) {
        // They initialize in a 0 degrees angle
        double angle = gameEngine.random.nextDouble()*Math.PI/3d-Math.PI/6d;
        speedY = 0.7;
        speedX = 0;
        // initialize in the central 50% of the screen horizontally
        positionX = gameEngine.random.nextInt(gameEngine.width)-100;
        // They initialize outside of the screen vertically
        positionY =0-200;
        rotationSpeed = 0 ;
        rotation = 0;

    }

    @Override
    public void startGame() {
    }

    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
       // gameController.returnToPool(this);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;
        rotation += rotationSpeed * elapsedMillis;
        if (rotation > 360) {
            rotation = 0;
        }
        else if (rotation < 0) {
            rotation = 360;
        }
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            //gameController.returnToPool(this);
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof SpaceShipPlayer) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            this.removeObject(gameEngine);
            powerUPtake = true;
            count.start();
            gameEngine.onGameEvent(GameEvent.PowerUpHit);
            // Add some score
        }
    }







}
