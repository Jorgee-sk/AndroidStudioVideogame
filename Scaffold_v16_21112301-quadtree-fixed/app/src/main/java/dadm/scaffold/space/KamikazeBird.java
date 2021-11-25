package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;

public class KamikazeBird extends Asteroid {


    private final GameController gameController;



    public KamikazeBird(GameController gameController, GameEngine gameEngine) {
        super(gameController, gameEngine,R.drawable.ave,50,100);
        this.speed = 200d * pixelFactor/1000d;
        this.gameController = gameController;

    }


    @Override
    public void init(GameEngine gameEngine) {
        // They initialize in a 0 degrees angle
        double angle = gameEngine.random.nextDouble()*Math.PI/3d-Math.PI/6d;
        speedY = 1;
        speedX = 0;
        // initialize in the central 50% of the screen horizontally
        positionX = gameEngine.random.nextInt(gameEngine.width)-100;
        // They initialize outside of the screen vertically
        positionY =0;
        rotationSpeed = 0 ;
        rotation = 180;




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
            gameController.returnToPool(this);
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }



}
