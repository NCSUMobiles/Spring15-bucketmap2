package csc591.bucketlistraleigh.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import csc591.bucketlistraleigh.R;


public class FunFragment extends Fragment {

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
        showRegion();
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
            is = getActivity().getAssets().open("raleigh_map_background_small.png");
            mDecoder = BitmapRegionDecoder.newInstance(new BufferedInputStream(is), true);
        } catch (IOException e) {
            throw new RuntimeException("Could not create BitmapRegionDecoder", e);
        }
    }

    private void showRegion() {
        Bitmap bitmap = getRegion();
        funMapView.setImageBitmap(bitmap);
    }

    private Bitmap getRegion() {
        Bitmap bitmap = null;

        Log.i("Width", String.valueOf(mDecoder.getWidth()));
        bitmap = mDecoder.decodeRegion(getRectForIndex(), null);
        return bitmap;
    }
    private Rect getRectForIndex() {
        // the resulting rectangle
        return new Rect(2200,811,4000,1800);
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        // public void onFragmentInteraction(Uri uri);
    }

}
