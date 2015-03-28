package csc591.bucketlistraleigh.view;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.app.Fragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import csc591.bucketlistraleigh.ImageFragment;
import csc591.bucketlistraleigh.R;


public class HomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ImageFragment()).commit();
        }

        Log.i("Home activity","You are here in onCreate");
    }




}
