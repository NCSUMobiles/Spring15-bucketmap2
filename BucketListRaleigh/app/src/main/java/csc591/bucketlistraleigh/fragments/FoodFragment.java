package csc591.bucketlistraleigh.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import csc591.bucketlistraleigh.R;
import csc591.bucketlistraleigh.helper.touch_zoom;
import csc591.bucketlistraleigh.view.BuildingImageActivity;
import csc591.bucketlistraleigh.view.BuildingReviewActivity;
import csc591.bucketlistraleigh.view.BuildingVideoActivity;


public class FoodFragment extends Fragment {

    touch_zoom t = new touch_zoom();
    private BitmapRegionDecoder mDecoder;
    private ImageView foodMapView;

    private OnFragmentInteractionListener mListener;



    public FoodFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_food, container, false);
        foodMapView = (ImageView) rootView.findViewById(R.id.food_imageView);
        createDecoder();

        t.imgView = (ImageView) getActivity().findViewById(R.id.zoom1);// get ImageView
        t.bitmap = getRegion();
        t.imgView.setImageBitmap(t.bitmap);// fill out image
        t.dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(t.dm);// get resolution ratio
        t.minZoom();
        t.center();
        t.imgView.setImageMatrix(t.matrix);


        //showRegion();


        foodMapView.setOnTouchListener(new View.OnTouchListener() {
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

                        if ((absoluteX > 390 && absoluteX < 470) && (absoluteY > 490 && absoluteY < 570)) {
                          //  showPopUp(view,"Poole's Dinner");
                            displayBuildingInfo(view,"Poole's Dinner");
                        }
                        else if ((absoluteX > 920 && absoluteX < 1000) && (absoluteY > 440 && absoluteY < 520)) {
                           // showPopUp(view,"Raleigh Times Bar");
                            displayBuildingInfo(view,"Raleigh Times Bar");
                        }
                        else if ((absoluteX > 885 && absoluteX < 965) && (absoluteY > 520 && absoluteY < 600)) {
                           // showPopUp(view,"Beasley's Chicken and Honey");
                            displayBuildingInfo(view,"Beasley's Chicken and Honey");
                        }
                        else if ((absoluteX > 985 && absoluteX < 1065) && (absoluteY > 550 && absoluteY < 630)) {
                           // showPopUp(view,"Bida Manda");
                            displayBuildingInfo(view,"Bida Manda");
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





    private void showRegion() {
        Bitmap bitmap = getRegion();
        foodMapView.setImageBitmap(bitmap);
    }

    private Bitmap getRegion() {
        Bitmap bitmap = null;

        Log.i("Width", String.valueOf(mDecoder.getWidth()));
        bitmap = mDecoder.decodeRegion(getRectForIndex(), null);
        return bitmap;
    }
    private Rect getRectForIndex() {
        // the resulting rectangle

//(left, top, right, bottom)
        return new Rect(500,511,2500,1500);
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        // public void onFragmentInteraction(Uri uri);
    }


    private void createDecoder(){
        InputStream is = null;
        try {
            //here you need to load the image with all the food places highlighted.
            is = getActivity().getAssets().open("raleigh_map_background_small.png");
            mDecoder = BitmapRegionDecoder.newInstance(new BufferedInputStream(is), true);
        } catch (IOException e) {
            throw new RuntimeException("Could not create BitmapRegionDecoder", e);
        }
    }

    private void showPopUp(ImageView view, String msg){

        LayoutInflater layoutInflater =
                (LayoutInflater) getActivity().getBaseContext()
                        .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT,true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
                ""));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view, 150, -350);

        //Update TextView in PopupWindow dynamically
        TextView textOut = (TextView) popupView.findViewById(R.id.buildingDescription);
        textOut.setText(msg);






      /*  Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });*/

        //popupWindow.showAsDropDown(view, 100, -200);


    }





    private void displayBuildingInfo(ImageView view, String buildingName){

        LayoutInflater layoutInflater =
                (LayoutInflater)getActivity().getBaseContext()
                        .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup2, null);

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
                Intent intent = new Intent(getActivity(), BuildingImageActivity.class);
                getActivity().startActivity(intent); //This executes the intent for Photo Button
            }
        });

        //Making Building Video Button Clickable
        ImageButton buildingVideoButton = (ImageButton) popupView.findViewById(R.id.videoButton);
        buildingVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuildingVideoActivity.class);
                getActivity().startActivity(intent); //This executes the intent for Photo Button
            }
        });

        //Making Building Review Button Clickable
        ImageButton buildingReviewButton = (ImageButton) popupView.findViewById(R.id.reviewButton);
        buildingReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuildingReviewActivity.class);
                getActivity().startActivity(intent); //This executes the intent for Photo Button
            }
        });
    }




}