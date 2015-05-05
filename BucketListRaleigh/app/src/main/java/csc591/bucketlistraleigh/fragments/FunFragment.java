package csc591.bucketlistraleigh.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import csc591.bucketlistraleigh.R;
import csc591.bucketlistraleigh.helper.Building;
import csc591.bucketlistraleigh.helper.popup;
import csc591.bucketlistraleigh.helper.touch_zoom;
import csc591.bucketlistraleigh.view.DrinkActivity;
import csc591.bucketlistraleigh.view.FoodActivity;
import csc591.bucketlistraleigh.view.FunActivity;


public class FunFragment extends Fragment {

    Building building = Building.getInstance();
    touch_zoom t = new touch_zoom();
    private BitmapRegionDecoder mDecoder;
    private ImageView funMapView;

    private OnFragmentInteractionListener mListener;



    public FunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fun, container, false);
        funMapView = (ImageView) rootView.findViewById(R.id.fun_imageView);
        createDecoder();

        t.imgView = (ImageView) getActivity().findViewById(R.id.zoom2);// get ImageView
        t.bitmap = getRegion();
        t.imgView.setImageBitmap(t.bitmap);// fill out image
        t.dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(t.dm);// get resolution ratio
        t.minZoom();
        t.center();
        t.imgView.setImageMatrix(t.matrix);
        Button food = (Button) rootView.findViewById(R.id.food_btn);
        Button drinks = (Button) rootView.findViewById(R.id.drinks_btn);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FoodActivity.class);
                getActivity().finish();
                Runtime.getRuntime().gc();
                startActivity(intent);

            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DrinkActivity.class);
                getActivity().finish();
                Runtime.getRuntime().gc();
                startActivity(intent);

            }
        });
       /* createDecoder();
        showRegion();*/

        funMapView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("Touched", "Inside food activity touch");
                ImageView view = (ImageView) v;
                view.setScaleType(ImageView.ScaleType.MATRIX);
                float scale;

                t.dumpEvent(event);


                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    // first finger
                    case MotionEvent.ACTION_DOWN:
                        t.savedMatrix.set(t.matrix);
                        t.prev.set(event.getX(), event.getY());
                        t.mode = t.DRAG;

                        float[] values = new float[9];
                        t.matrix.getValues(values);

                        // values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image,
                        // regardless of the zoom factor.
                        // values[0] and values[4] are the zoom factors for the image's width and height respectively.
                        // If you zoom at the same factor, these should both be the same value.

                        // event is the touch event for MotionEvent.ACTION_UP
                        float absoluteX = (event.getX() - values[2]) / values[0];
                        float absoluteY = (event.getY() - values[5]) / values[4];
                        Log.i("X coordinate", "" + absoluteX);
                        Log.i("Y coordinate", "" + absoluteY);
                        popup p = new popup();

                        //Displaying popup menu for buildings as they are selected
                        if ((absoluteX > 671 && absoluteX < 751) && (absoluteY > 213 && absoluteY < 293)) {
                            building.setBuildingId("b6");
                            building.setBuildingName("Askew-Taylor Paints");
                            p.displayBuildingInfo(getActivity(), view, "Askew-Taylor Paints","b6");
                        }
                        else if ((absoluteX > 669 && absoluteX < 749) && (absoluteY > 430 && absoluteY < 510)) {
                            building.setBuildingId("b1");
                            building.setBuildingName("Flying Saucer Draught Emporium");
                            p.displayBuildingInfo(getActivity(), view, "Flying Saucer Draught Emporium","b1");
                        }
                        else if ((absoluteX > 1150 && absoluteX < 1230) && (absoluteY > 830 && absoluteY < 910)) {
                            building.setBuildingId("b10");
                            building.setBuildingName("Marbles Museum");
                            p.displayBuildingInfo(getActivity(), view, "Marbles Museum","b10");
                        }
                        else if ((absoluteX > 650 && absoluteX < 730) && (absoluteY > 1040 && absoluteY < 1120)) {
                            building.setBuildingId("b2");
                            building.setBuildingName("Lincoln Theatre");
                            p.displayBuildingInfo(getActivity(),view, "Lincoln Theatre","b2");
                        }
                        break;
                    // second finger
                    case MotionEvent.ACTION_POINTER_DOWN:
                        t.dist = t.spacing(event);
                        // if two point is larger then 10, start multi-point touch
                        if (t.spacing(event) > 10f) {
                            t.savedMatrix.set(t.matrix);
                            t.midPoint(t.mid, event);
                            t.mode = t.ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        t.mode = t.NONE;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (t.mode == t.DRAG) {
                            t.matrix.set(t.savedMatrix);
                            t.matrix.postTranslate(event.getX() - t.prev.x, event.getY()
                                    - t.prev.y);
                        } else if (t.mode == t.ZOOM) {
                            float newDist = t.spacing(event);
                            if (newDist > 10f) {
                                t.matrix.set(t.savedMatrix);
                                float tScale = newDist / t.dist;
                                t.matrix.postScale(tScale, tScale, t.mid.x, t.mid.y);
                            }
                        }
                        break;
                }
                t.imgView.setImageMatrix(t.matrix);
                t.CheckView();
                return true;
            }
        });


        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // show first country at start

        // txtCountry.setText(COUNTRY_NAMES[0]);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    private void createDecoder(){
        InputStream is = null;
        try {
            //here you need to load the image with all the food places highlighted.
            is = getActivity().getAssets().open("fun_image1.png");
            mDecoder = BitmapRegionDecoder.newInstance(new BufferedInputStream(is), true);
        } catch (IOException e) {
            throw new RuntimeException("Could not create BitmapRegionDecoder", e);
        }
    }

    private void showRegion() {
        Bitmap bitmap = getRegion();

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 2;
        funMapView.setImageBitmap(bitmap);
    }

    private Bitmap getRegion() {
        Bitmap bitmap = null;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 2;
        Log.i("Width", String.valueOf(mDecoder.getWidth()));
        bitmap = mDecoder.decodeRegion(getRectForIndex(), null);
        return bitmap;
    }
    private Rect getRectForIndex() {
        // the resulting rectangle
        //(left, top, right, bottom)
        return new Rect(500,211,2500,1500);
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        // public void onFragmentInteraction(Uri uri);
    }


}
