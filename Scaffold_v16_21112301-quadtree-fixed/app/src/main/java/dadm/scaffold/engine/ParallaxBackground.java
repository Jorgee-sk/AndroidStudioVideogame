package dadm.scaffold.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import dadm.scaffold.sound.GameEvent;

public class ParallaxBackground extends GameObject{

    public GameEngine gameEngine;
    public int mScreenHeight,mScreenWidth,mTargetWidth;
    Bitmap mBitmap;
    protected double mPositionY = 0.0;
    Double mPixelFactor, mSpeedY,mImageHeight,mImageWidth;
    private final Matrix mMatrix = new Matrix();
    private Rect mSrcRect = new Rect();
    private Rect mDstRect = new Rect();


    public ParallaxBackground(GameEngine gameEngine, int speed,
    int drawableResId) {
        this.gameEngine = gameEngine;
        Drawable spriteDrawable = gameEngine.getContext().getResources().getDrawable(drawableResId);
        this.mBitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
        this.mPixelFactor = gameEngine.pixelFactor;
        this.mSpeedY = speed*this.mPixelFactor/1000d;
        this.mImageHeight = spriteDrawable.getIntrinsicHeight()*this.mPixelFactor;
        this.mImageWidth = spriteDrawable.getIntrinsicWidth()*this.mPixelFactor;
        this.mScreenHeight = gameEngine.height;
        this.mScreenWidth = gameEngine.width;
        this.mTargetWidth = (int) Math.min(mImageWidth, mScreenWidth);
    }


    @Override
    public void startGame() {

    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        mPositionY += mSpeedY * elapsedMillis;
    }

    @Override
    public void onDraw(Canvas canvas) {
        efficientDraw(canvas);
        /*if (mPositionY > 0) {
            mMatrix.reset();
            mMatrix.postScale(mPixelFactor.floatValue(),
                    mPixelFactor.floatValue());
            mMatrix.postTranslate(0, (float) (mPositionY - mImageHeight));
            canvas.drawBitmap(mBitmap, mMatrix, null);
        }
        mMatrix.reset();
        mMatrix.postScale(mPixelFactor.floatValue(),
                mPixelFactor.floatValue());
        mMatrix.postTranslate(0, mPixelFactor.floatValue());
        canvas.drawBitmap(mBitmap, mMatrix, null);
        if (mPositionY > mScreenHeight) {
            mPositionY -= mImageHeight;
        }*/
    }


    private void efficientDraw(Canvas canvas) {
        if (mPositionY < 0) {
            mSrcRect.set(0,
                    (int) (-mPositionY/mPixelFactor),
                    (int) (mTargetWidth/mPixelFactor),
                    (int) ((mScreenHeight - mPositionY)/mPixelFactor));
            mDstRect.set(0,
                    0,
                    (int) mTargetWidth,
                    (int) mScreenHeight);
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
        }
        else {
            mSrcRect.set(0,
                    0,
                    (int) (mTargetWidth/mPixelFactor),
                    (int) ((mScreenHeight - mPositionY) / mPixelFactor));
            mDstRect.set(0,
                    (int) mPositionY,
                    (int) mTargetWidth,
                    (int) mScreenHeight);
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
            // We need to draw the previous block
            mSrcRect.set(0,
                    (int) ((mImageHeight - mPositionY) / mPixelFactor),
                    (int) (mTargetWidth/mPixelFactor),
                    (int) (mImageHeight/mPixelFactor));
            mDstRect.set(0,
                    0,
                    (int) mTargetWidth,
                    (int) mPositionY);
            canvas.drawBitmap(mBitmap, mSrcRect, mDstRect, null);
        }
        if (mPositionY > mScreenHeight) {
            mPositionY -= mImageHeight;
        }
    }

}
