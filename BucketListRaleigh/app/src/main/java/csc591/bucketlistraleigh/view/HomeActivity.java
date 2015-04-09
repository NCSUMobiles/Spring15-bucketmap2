package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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


    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    //Create database
    CreateDB dbObject = new CreateDB(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_home);
        //if (savedInstanceState == null) {
        //    getFragmentManager().beginTransaction()
        //            .add(R.id.container, new ImageFragment()).commit();
        //}

        // create database start
        // it will create initial database of coordinates for all buildings


        dbObject.createDatabase();
        // create database end
        Log.i("Home activity", "You are here in onCreate");
        //LayeredImageView v = new LayeredImageView(this);
        setContentView(R.layout.activity_home);
        ImageView v = (ImageView) this.findViewById(R.id.zoom);
        //v.setImageResource(R.drawable.raleigh_map_background_small);
        Resources res = getResources();
        Bitmap bitmap1 = BitmapFactory.decodeResource(res,R.drawable.raleigh_map_background_small);
        //Bitmap bitmap2 = BitmapFactory.decodeResource(res,R.drawable.building_highlight_example);
        /*Bitmap bitmap2 = BitmapFactory.decodeResource(res,R.drawable.ic_food_btn_hdpi);
        Bitmap bitmap3 = BitmapFactory.decodeResource(res,R.drawable.ic_fun_button_hdpi);
        Bitmap bitmap4 = BitmapFactory.decodeResource(res,R.drawable.ic_drinks_btn_hdpi);
        v.addLayer( getResizedBitmap(bitmap1,200));
        v.addLayer(0,getResizedBitmap(bitmap2,200));
        v.addLayer(0,getResizedBitmap(bitmap3,200));
        v.addLayer(0,getResizedBitmap(bitmap4,200));
        //v.addLayer(getResizedBitmap(bitmap3,38));*/
        v.setImageBitmap(bitmap1);
        v.setOnTouchListener(this);
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

    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

    //public boolean onTouchEvent(MotionEvent event) {
        //ImageView view = (ImageView) findViewById(R.id.zoom);


        //This is for zoom
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;

                float[] values = new float[9];
                matrix.getValues(values);

// values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
// values[0] and values[4] are the zoom factors for the image's width and height respectively. If you zoom at the same factor, these should both be the same value.

// event is the touch event for MotionEvent.ACTION_UP
                float absoluteX = (event.getX() - values[2]) / values[0];
                float absoluteY = (event.getY() - values[5]) / values[4];
                Log.i("X coordinate",""+absoluteX);
                Log.i("Y coordinate",""+absoluteY);

                if((absoluteX >1300 && absoluteX<1500) && (absoluteY>700 && absoluteY<900))
                {
                    LayoutInflater layoutInflater =
                            (LayoutInflater)getBaseContext()
                                    .getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup, null);
                    final PopupWindow popupWindow = new PopupWindow(
                            popupView, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

                    //Update TextView in PopupWindow dynamically
                    TextView textOut = (TextView)popupView.findViewById(R.id.buildingDescription);

                    Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
                    btnDismiss.setOnClickListener(new Button.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }});

                    popupWindow.showAsDropDown(view, 100, -200);




                }

                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event)
    {
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
