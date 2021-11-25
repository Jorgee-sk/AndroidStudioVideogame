package dadm.scaffold.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import dadm.scaffold.R;

public abstract class Sprite extends ScreenGameObject {

    protected double rotation;

    protected double pixelFactor;


    private Bitmap bitmap;

    private final Matrix matrix = new Matrix();

    private GameEngine theGameEngine;

    protected Sprite (GameEngine gameEngine, int drawableRes,double pixelX, double pixelY) {
        this.theGameEngine = gameEngine;

        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);

        this.pixelFactor = gameEngine.pixelFactor;

        this.height = (int) (pixelX * this.pixelFactor);
        this.width = (int) (pixelY * this.pixelFactor);

        this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        radius = Math.max(height, width)/2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (positionX > canvas.getWidth()
                || positionY > canvas.getHeight()
                || positionX < - width
                || positionY < - height) {
            return;
        }
        Paint mPaint = new Paint();
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(mBoundingRect, mPaint);



        matrix.reset();
        matrix.postScale((float) pixelFactor, (float) pixelFactor);
        matrix.postTranslate((float) positionX, (float) positionY);
        matrix.postRotate((float) rotation, (float) (positionX + width/2), (float) (positionY + height/2));
        canvas.drawBitmap(bitmap, matrix, null);

/*
        matrix.reset();
        matrix.postScale((float) pixelFactor/10, (float) pixelFactor/10);
        matrix.postTranslate((float) positionX, (float) positionY);
        matrix.postRotate((float) rotation, (float) (positionX + width/2), (float) (positionY + height/2));
        //Diubjo colision
        canvas.drawBitmap(
                ((BitmapDrawable) theGameEngine.getContext().getResources().getDrawable(R.drawable.circleyellow)).getBitmap()
                , matrix, null);

*/

    }

    public void setBitmap(int drawableRes) {
        Resources r = theGameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);
        this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
    }
}
