package csc591.bucketlistraleigh;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageFragment extends Fragment {

    private ImageView mapView;
    private BitmapRegionDecoder mDecoder;

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image,container,false);
        mapView = (ImageView) rootView.findViewById(R.id.imageView);
        try {
            InputStream is = getActivity().getAssets().open("raleigh_map_background_small.png");
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            mapView.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rootView;
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      //  createDecoder();
        // show first country at start
       // showCountry(0);
        // txtCountry.setText(COUNTRY_NAMES[0]);
    }

   /* private void showRegion(int i) {
        Bitmap bitmap = getFlag(i);
        imgCountry.setImageBitmap(bitmap);
    }


    *
     * Decodes the region of bitmap.
     * @param i The index of the flag.
     * @return The bitmap of the specified country flag.
     */
   /* private Bitmap getFlag(int i) {
        Bitmap bitmap = null;

        Log.i("Width", String.valueOf(mDecoder.getWidth()));
        bitmap = mDecoder.decodeRegion(getRectForIndex(i, mDecoder.getWidth()), null);
        return bitmap;
    }

    *
     * Creates BitmapRegionDecoder from an image in assets.

    private void createDecoder(){
        InputStream is = null;
        try {
            is = getActivity().getAssets().open("raleigh_map_background_medium.png");
            mDecoder = BitmapRegionDecoder.newInstance(new BufferedInputStream(is), true);
        } catch (IOException e) {
            throw new RuntimeException("Could not create BitmapRegionDecoder", e);
        }
    }

    */
    /**
     * @param index The index of the flag of one of the 32 countries.
     * @param width The width of the source image.
     * @return

    private Rect getRectForIndex(int index, int width) {
        // check if index is valid
        if (index < 0 && index >= NUM_OF_FLAGS)
            throw new IllegalArgumentException("Index must be between 0 and 31.");

        // calculate one side of a single flag
        int oneSide = (width - ((COUNT_PER_ROW + 1) * SPACE)) / COUNT_PER_ROW;

        // calculate the row and col of the given index
        int row = (index / COUNT_PER_ROW);
        int col = (index % COUNT_PER_ROW);

        // left and right sides of the rectangle
        int left = (oneSide * col) + (SPACE * (col + 1));
        int right = left + oneSide;

        // top and bottom sides of the rectangle
        int top = (oneSide * row) + (SPACE * (row + 1));
        int bottom = top + oneSide;

        Log.i("Left", String.valueOf(left));
        Log.i("Right", String.valueOf(right));
        Log.i("Top", String.valueOf(top));
        Log.i("Bottom", String.valueOf(bottom));



        // the resulting rectangle
        return new Rect(2293,811,4000,1600);
    }
}*/
}
