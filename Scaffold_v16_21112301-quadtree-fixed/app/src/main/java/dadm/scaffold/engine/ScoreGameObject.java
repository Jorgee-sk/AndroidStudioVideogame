package dadm.scaffold.engine;

import android.content.Intent;
import android.graphics.Canvas;
import android.view.View;
import android.widget.TextView;

import dadm.scaffold.counter.ResultsActivity;
import dadm.scaffold.sound.GameEvent;

public class ScoreGameObject extends GameObject{
    private final TextView mText;
    private int mPoints;
    private boolean mPointsHaveChanged;
    private static final int POINTS_LOSS_PER_ASTEROID_MISSED = 0;
    private static final int POINTS_GAINED_PER_ASTEROID_HIT = 50;
    private static GameEngine gameEngine;

    public ScoreGameObject(View view, int viewResId, GameEngine gameEngine) {
        mText = (TextView) view.findViewById(viewResId);
        this.gameEngine = gameEngine;
    }
    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {}

    @Override
    public void onDraw(Canvas canvas) {
        if (mPointsHaveChanged) {
            mText.post(mUpdateTextRunnable);
            mPointsHaveChanged = false;
        }
    }
    @Override
    public void startGame() {
        mPoints = 0;
        mText.post(mUpdateTextRunnable);
    }
    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.AsteroidHit) {
            mPoints += POINTS_GAINED_PER_ASTEROID_HIT;
            mPointsHaveChanged = true;
        }
        else if (gameEvent == GameEvent.AsteroidMissed) {
            if (mPoints > 0) {
                mPoints -= POINTS_LOSS_PER_ASTEROID_MISSED;
            }
            mPointsHaveChanged = true;
        }else if (gameEvent == GameEvent.GameOver) {
            Intent intent = new Intent(gameEngine.mainActivity, ResultsActivity.class);
            intent.putExtra("puntuacion", mPoints);
            gameEngine.mainActivity.startActivity(intent);
        }




    }
    private Runnable mUpdateTextRunnable = new Runnable() {

    @Override
    public void run() {
            String text = String.format("%06d", mPoints);
            mText.setText(text);
        }
    };


}
