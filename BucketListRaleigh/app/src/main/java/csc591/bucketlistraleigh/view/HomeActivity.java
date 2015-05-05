package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import csc591.bucketlistraleigh.R;
import csc591.bucketlistraleigh.database.CreateDB;
import csc591.bucketlistraleigh.helper.popup;
import static android.app.PendingIntent.getActivity;

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
    PointF start = new PointF();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create database
      CreateDB dbObject = new CreateDB(this);
        SQLiteDatabase db = dbObject.getWritableDatabase();
        dbObject.onUpgrade(db,1,2);
        dbObject.logDatabase();


        Log.i("Home activity","You are here in onCreate");

        setContentView(R.layout.activity_home);
        //   ImageView v = (ImageView) this.findViewById(R.id.zoom);

        // Resources res = getResources();
        // Bitmap bitmap = BitmapFactory.decodeResource(res,R.drawable.raleigh_map_background_small);


        ImageButton draw = (ImageButton) findViewById(R.id.draw_btn);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,FingerPaintActivity.class);
                startActivity(intent);
            }
        });
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

        InputStream is = null;
        try{
            is = getAssets().open("plain_image1.png");
            bitmap = BitmapFactory.decodeStream(is);
        }catch (IOException e){

        }
        imgView = (ImageView) findViewById(R.id.zoom);// get ImageView

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

        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);


        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // first finger
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                prev.set(event.getX(), event.getY());
                mode = DRAG;

                float[] values = new float[9];
                matrix.getValues(values);

                // values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image,
                // regardless of the zoom factor.
                // values[0] and values[4] are the zoom factors for the image's width and height respectively.
                // If you zoom at the same factor, these should both be the same value.

                // event is the touch event for MotionEvent.ACTION_UP
                float absoluteX = (event.getX() - values[2]) / values[0];
                float absoluteY = (event.getY() - values[5]) / values[4];
                Log.i("X coordinate",""+absoluteX);
                Log.i("Y coordinate",""+absoluteY);
                popup p = new popup();

                if ((absoluteX > 875 && absoluteX < 955) && (absoluteY > 1001 && absoluteY < 1081)) {
                    //  showPopUp(view,"Poole's Dinner");
                    p.displayBuildingInfo(this, view, "Poole's Dinner","b4");
                }
                else if ((absoluteX > 1421 && absoluteX < 1501) && (absoluteY > 973 && absoluteY < 1053)) {
                    // showPopUp(view,"Raleigh Times Bar");
                    p.displayBuildingInfo(this,view, "Raleigh Times Bar","b5");
                }
                else if ((absoluteX > 1390 && absoluteX < 1470) && (absoluteY > 1043 && absoluteY < 1123)) {
                    // showPopUp(view,"Beasley's Chicken and Honey");
                    p.displayBuildingInfo(this,view, "Beasley's Chicken and Honey","b7");
                }
                else if ((absoluteX > 1487 && absoluteX < 1567) && (absoluteY > 1067 && absoluteY < 1147)) {

                    p.displayBuildingInfo(this,view, "Bida Manda","b8");
                }
                else if  ((absoluteX > 1160 && absoluteX < 1240) && (absoluteY > 1240 && absoluteY < 1320)) {

                    p.displayBuildingInfo(this,view, "Lincoln Theatre","b2");
                }
                else if  ((absoluteX > 1097 && absoluteX < 1177) && (absoluteY > 732 && absoluteY < 812)) {

                    p.displayBuildingInfo(this, view, "Cafe de Los Muertos","b3");
                }
                else if  ((absoluteX > 1136 && absoluteX < 1216) && (absoluteY > 635 && absoluteY < 815)) {

                    p.displayBuildingInfo(this, view, "Flying Saucer Draught Emporium","b1");
                }
                else if  ((absoluteX > 1958 && absoluteX < 2038) && (absoluteY > 996 && absoluteY < 1076)) {

                    p.displayBuildingInfo(this, view, "Oakwood Cafe","b9");
                }
                else if  ((absoluteX > 1177 && absoluteX < 1257) && (absoluteY > 424 && absoluteY < 504)) {

                    p.displayBuildingInfo(this, view, "Askew-Taylor Paints","b6");
                }
                else if  ((absoluteX > 1634 && absoluteX < 1714) && (absoluteY > 1040 && absoluteY < 1120)) {

                    p.displayBuildingInfo(this, view, "Marbles Museum","b10");
                }

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

    private void displayBuildingInfo(ImageView view, String buildingName){

        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT,true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),""));
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(view, 150, -350);

        //Update the name of the building in the sticky note dynamically using method parameter buildingName
        TextView textOut = (TextView) popupView.findViewById(R.id.buildingDescription);
        textOut.setText(buildingName);

        //Setting the View

        //Making Building Image Button Clickable
        ImageButton buildingPhotoButton = (ImageButton) popupView.findViewById(R.id.photoButton);
        buildingPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Intent intent = new Intent(v.getContext(), BuildingImageActivity.class);
                  startActivity(intent); //This executes the intent for Photo Button
            }
        });

        //Making Building Review Button Clickable
        ImageButton buildingReviewButton = (ImageButton) popupView.findViewById(R.id.reviewButton);
        buildingReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BuildingReviewActivity.class);
                startActivity(intent); //This executes the intent for Photo Button
            }
        });
    }
}
