package dadm.scaffold.engine;

import android.graphics.Canvas;
import android.view.View;
import android.widget.LinearLayout;

import dadm.scaffold.R;
import dadm.scaffold.sound.GameEvent;

public class LivesCounter extends GameObject{

    private final LinearLayout mLayout;
    private int i = 0,j=2;

    public LivesCounter(View view, int viewResId) {

        mLayout = (LinearLayout) view.findViewById(viewResId);

    }
    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {}

    @Override
    public void onDraw(Canvas canvas) {

    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {

        if (gameEvent == GameEvent.LifeLost) {
            if(i==0){
                i++;
                return;
            }else {
                mLayout.post(mRemoveLifeRunnable);
            }

        }
        else if (gameEvent == GameEvent.LifeAdded) {
            mLayout.post(mAddLifeRunnable);
        }

    }
    private Runnable mRemoveLifeRunnable = new Runnable() {
        @Override
        public void run() {
            // Remove one life from the layout

            mLayout.getChildAt(j).setVisibility(View.INVISIBLE);
            j--;

           // mLayout.removeViewAt(mLayout.getChildCount()-1);
        }
    };
    private Runnable mAddLifeRunnable = new Runnable() {
        @Override
        public void run() {
            // Remove one life from the layout
            View spaceship = View.inflate(mLayout.getContext(),
                    R.layout.fragment_game, mLayout);
        }
    };

}
