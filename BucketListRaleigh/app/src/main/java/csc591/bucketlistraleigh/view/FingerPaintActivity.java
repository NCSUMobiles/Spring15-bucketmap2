package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import csc591.bucketlistraleigh.R;


public class FingerPaintActivity extends Activity {
    private Paint mPaint;
    DrawingView drawingView;
    public BitmapRegionDecoder mDecoder;
    Bitmap bitmap;
    String buildingName="";
    String buildingID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_finger_paint);
        drawingView = new DrawingView(this);
        buildingName = getIntent().getExtras().getString("buildingName");
        buildingID = getIntent().getExtras().getString("buildingID");
        createDecoder();
        showRegion();
        drawingView.setImageBitmap(bitmap);
        // drawingView.setImageResource(R.drawable.doodle);
        drawingView.requestFocus();
        LinearLayout upper = (LinearLayout) findViewById(R.id.LinearLayout01);
        upper.addView(drawingView);



        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }


    public class DrawingView extends ImageView{

        public int width;
        public  int height;
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c){
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh){
            Log.d("Width", "" + w);
            Log.d("Height",""+h);
            Log.d("Old Width",""+oldw);
            Log.d("Old Height",""+oldh);
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y){
            //  mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y){
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        public boolean onTouchEvent(MotionEvent event){
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }//end of drawingview class



    private void createDecoder(){
        InputStream is = null;
        try {
            //here you need to load the image with all the food places highlighted.
            is = this.getAssets().open("raleigh_map_background_medium.png");
            mDecoder = BitmapRegionDecoder.newInstance(new BufferedInputStream(is), true);
        } catch (IOException e) {
            throw new RuntimeException("Could not create BitmapRegionDecoder", e);
        }
    }

    private void showRegion() {
        bitmap = getRegion();
        //  foodMapView.setImageBitmap(bitmap);
    }

    private Bitmap getRegion() {
        Bitmap bitmap = null;

        Log.i("Width", String.valueOf(mDecoder.getWidth()));
        bitmap = mDecoder.decodeRegion(getRectForIndex(), null);
        return bitmap;
    }
    private Rect getRectForIndex() {
        // the resulting rectangle
        Log.i("In get rect Building id", buildingID);
        Log.i("In get rect=-- name", buildingName);
        return new Rect(1500,2000,5000,3000);
    }


}