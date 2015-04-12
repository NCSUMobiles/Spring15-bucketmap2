package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Fragment;
import android.view.View.OnTouchListener;
import android.database.sqlite.SQLiteDatabase;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import csc591.bucketlistraleigh.database.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


import csc591.bucketlistraleigh.ImageFragment;
import csc591.bucketlistraleigh.R;


public class HomeActivity extends Activity implements OnTouchListener{

    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    DisplayMetrics dm;
    ImageView imgView;
    Bitmap bitmap;

    float minScaleR ;// min scale
    static final float MAX_SCALE = 3f;// max scale

    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF prev = new PointF();
    PointF mid = new PointF();
    float dist = 1f;

  

    //Create database
    CreateDB dbObject = new CreateDB(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbObject.createDatabase();

        Log.i("Home activity","You are here in onCreate");

        setContentView(R.layout.activity_home);
     //   ImageView v = (ImageView) this.findViewById(R.id.zoom);

       // Resources res = getResources();
       // Bitmap bitmap = BitmapFactory.decodeResource(res,R.drawable.raleigh_map_background_small);



        //For the button clicking
        Button drinks = (Button) findViewById(R.id.drinks_btn);
        Button food = (Button) findViewById(R.id.food_btn);
        Button fun = (Button) findViewById(R.id.fun_btn);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FoodActivity.class);
                startActivity(intent);
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DrinkActivity.class);
                startActivity(intent);
            }
        });
        fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FunActivity.class);
                startActivity(intent);
            }
        });



        imgView = (ImageView) findViewById(R.id.zoom);// get ImageView
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.raleigh_map_background_small);// get image
        imgView.setImageBitmap(bitmap);// fill out image
        imgView.setOnTouchListener(this);// setup listener
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);// get resolution ratio
        minZoom();
        center();
        imgView.setImageMatrix(matrix);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // first finger
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                prev.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            // second finger
            case MotionEvent.ACTION_POINTER_DOWN:
                dist = spacing(event);
                // if two point is larger then 10, start multi-point touch
                if (spacing(event) > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - prev.x, event.getY()
                            - prev.y);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float tScale = newDist / dist;
                        matrix.postScale(tScale, tScale, mid.x, mid.y);
                    }
                }
                break;
        }
        imgView.setImageMatrix(matrix);
        CheckView();
        return true;    }

    /**
     * limit zoom in and zoom out
     */
    private void CheckView() {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < minScaleR) {
                matrix.setScale(minScaleR, minScaleR);
            }
            if (p[0] > MAX_SCALE) {
                matrix.set(savedMatrix);
            }
        }
        center();
    }

    /**
     * zoom out to max 100%
     */
    private void minZoom() {
        minScaleR = Math.min(
                ((float) dm.widthPixels / (float) bitmap.getWidth()),
                ((float) dm.heightPixels / (float) bitmap.getHeight()));
        if (minScaleR < 1.0) {
            matrix.postScale(minScaleR, minScaleR);
        }
    }

    private void center() {
        center(true, true);
    }

    /**
     * center the image
     */
    protected void center(boolean horizontal, boolean vertical) {
        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            // always center
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imgView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }

    /**
     * distance of two points
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * mid point of two points
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }
}
