package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.database.Cursor;
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
import android.widget.ImageView;
import android.app.Fragment;
import android.view.View.OnTouchListener;
import android.database.sqlite.SQLiteDatabase;

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
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        //if (savedInstanceState == null) {
        //    getFragmentManager().beginTransaction()
        //            .add(R.id.container, new ImageFragment()).commit();
        //}

        db=openOrCreateDatabase("MyDB1",MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Building_Coordinates(building_id INTEGER,building_name VARCHAR,x_coordinate REAL,y_coordinate REAL);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(1,'Flying Saucer',7.840, 4.550);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(2,'Lincoln Theatre',8.030, 8.420);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(3,'Cafe de Los Muertos',7.450,5.050);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(4,'Poole’s Diner',6.150, 6.930);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(5,'Raleigh Times',9.770, 6.770);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(6,'Askew-Taylor Point',8.000, 3.0030);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(7,'Beasley Chicken and Honey',9.500, 7.140);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(8,'Bida Manda',10.180, 7.330);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(9,'Oakwood Cafe',13.240, 6.830);");
        db.execSQL("INSERT INTO Building_Coordinates VALUES(10,'Lincoln Theatre',8.030 , 8.420);");
        Cursor building_name = db.rawQuery("SELECT * from Building_Coordinates", null);
        if (building_name.moveToFirst()){
            do{
                String data = building_name.getString(building_name.getColumnIndex("building_name"));
                Log.i("Home activity print dat",data+" ");
                // do what ever you want here
            }while(building_name.moveToNext());
        }
        building_name.close();


        Log.i("Home activity","You are here in onCreate");
        LayeredImageView v = new LayeredImageView(this);
        v.setImageResource(R.drawable.raleigh_map_background_small);
        Resources res = getResources();
        Bitmap bitmap1 = BitmapFactory.decodeResource(res,R.drawable.palette);
        Bitmap bitmap2 = BitmapFactory.decodeResource(res,R.drawable.building_highlight_example);
        v.addLayer( getResizedBitmap(bitmap1,200));
        v.addLayer(0,getResizedBitmap(bitmap2,38));
        setContentView(v);
        v.setOnTouchListener(this);


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
