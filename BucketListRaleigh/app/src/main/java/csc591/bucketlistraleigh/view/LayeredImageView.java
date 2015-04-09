package csc591.bucketlistraleigh.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import static android.util.Log.*;

public class LayeredImageView extends ImageView {
    private final static String TAG = "LayeredImageView";


    ArrayList<Bitmap> mLayers;

    public LayeredImageView(Context context) {
        super(context);
        init();
    }

    public LayeredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mLayers = new ArrayList<Bitmap>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("Program flow", "Layered Image view activity");
        Matrix matrix = getImageMatrix();
        if (matrix != null) {
            int numLayers = mLayers.size();
                Bitmap b = mLayers.get(0);
            float canvasx = (float) getWidth();
            float canvasy = (float) getHeight();
            float bitmapx = (float) b.getWidth();
            float bitmapy = (float) b.getHeight();
            float boardPosX = ((canvasx/2) - (bitmapx / 2));
            float boardPosY = ((canvasy/2) - (bitmapy / 2));
            canvas.drawBitmap(b, boardPosX+58, boardPosY-32, null);


           // Bitmap b1 = mLayers.get(1);
            //canvas.drawBitmap(b1, boardPosX-205, boardPosY-70, null);

           // Bitmap b2 = mLayers.get(1);
            //canvas.drawBitmap(b2, (boardPosX*2-600), boardPosY-400, null);

            //Bitmap b3 = mLayers.get(2);
            //canvas.drawBitmap(b3, (boardPosX*2-400), boardPosY-400, null);

            //Bitmap b4 = mLayers.get(0);
            //canvas.drawBitmap(b4, (boardPosX*2-200), boardPosY-400, null);

        }
    }

    public void addLayer(Bitmap b) {
        mLayers.add(b);
        invalidate();
    }

    public void addLayer(int idx, Bitmap b) {
        mLayers.add(idx, b);
        invalidate();
    }

    public void removeLayer(int idx) {
        mLayers.remove(idx);
    }

    public void removeAllLayers() {
        mLayers.clear();
        invalidate();
    }

    public int getLayersSize() {
        return mLayers.size();
    }
}